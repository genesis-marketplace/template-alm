# GENESIS Support

import datetime
import glob
import io
import json
import os
import re
import shlex
import subprocess
import sys
import logging
from subprocess import Popen, PIPE
from typing import Tuple, List, Union, Any, Dict
from xml.dom import minidom

import genesis

logging.basicConfig(level = logging.DEBUG if 'SCRIPT_VERBOSE_OUTPUT' in os.environ else logging.INFO, \
    format='%(asctime)s:%(name)s:%(levelname)s:%(message)s', datefmt='%Y-%m-%d %H:%M:%S')

DEFAULT_DATETIME_FORMAT = "%Y-%m-%d %H:%M:%S.%f"
JAR_MAJ_MIN_EXPRESSION = re.compile('(\\d+\\.\\d+).\\d+')
DISABLE_MANIFEST_CHECK = os.environ.get("GENESIS_DISABLE_MANIFEST_CHECK", "false")

def read_system_definition():
    file_name = "generated-system-definition.json"
    sys_def_file = os.environ["GENESIS_HOME"] + '/generated/cfg/' + file_name
    sys_def_dict = {}
    if os.path.isfile(sys_def_file):
        with open(sys_def_file) as file:
            try:
                sys_def_dict = json.load(file)
            except:
                logging.error("Unable to read " + file_name)

    return sys_def_dict


# tries to select only the libraries that will be required from a list of modules specified
def buildClasspathForModules(modulesNames: List[str], silent: bool = False, useGenerated: bool = True) -> str:
    system_definition = genesis.read_system_definition()
    classpath = []
    for module in modulesNames:
        classpath += genesis.buildClasspathForModule(module, silent, useGenerated)
    joined_classpath = ':'.join(set(classpath))
    if 'GlobalClasspathAdditions' in system_definition:
        # Split global classpath additions by :, as there could be more than one
        split_global_dependencies = system_definition['GlobalClasspathAdditions'].split(':')
        # Find them using findJar. If a dependency is missing, an exception will be thrown.
        found_global_dependencies = map(findJar, split_global_dependencies)
        # Join everything together.
        joined_classpath = ':'.join(set(found_global_dependencies)) + ':' + joined_classpath
    return joined_classpath


def buildClasspathForModule(moduleName: str, silent: bool = False, useGenerated: bool = True) -> List[str]:

    logging.debug("building classpath for module: " + moduleName)

    ignoreDirectories = ["generated", "runtime"]
    directories = os.listdir(os.environ["GENESIS_HOME"])
    directories = [x for x in directories if x not in ignoreDirectories]
    foundFiles = []

    classpath = []

    genClassPath = []

    for dir in directories:
        expectedBinDir = os.environ["GENESIS_HOME"] + '/' + dir + '/bin'

        if os.path.isdir(expectedBinDir):
            files = glob.glob(expectedBinDir + '/' + moduleName + '-*')

            if len(files) > 1:
                logging.warning("Multiple modules found for " + moduleName)
                return None
            elif len(files) == 1:
                foundFiles.append(files[0])

    if len(foundFiles) == 0:
        logging.info("No modules found for name " + moduleName)
        return None

    if len(foundFiles) > 1:
        # If we have more than one file, get the one from site-specific if exists. Otherwise, refuse to continue.
        siteSpecificFile = get_jar_from_site_specific(foundFiles)
        if siteSpecificFile:
            foundFiles = [siteSpecificFile]
        else:
            logging.warning("Multiple modules for with name '" + moduleName + "' found : " + ",".join(foundFiles))
            return None


    if len(foundFiles) == 1:
        classpath += [foundFiles[0]]
        # WARNIING
        # In case we find a jar in site-specific, that is meant to override some original jar,
        # we want to put it at the beginning of the classpath. Not including the original jar
        # in the classpath is not enough as it's still possible to pick it up.
        # It happens when the original jar is specified in a manifest file of some other dependency
        # coming from the classpath that we're building
        # and exists in the same folder as this dependency.
        # I don't know why it happens but putting site-specific jars at the beginning of the classpath prevents that.
        ss_classpath = []

        jars = getDependenciesForJar(foundFiles[0])

        for jar in jars:
            if not jar.startswith('genesis-generated-dao'):
                jar_path = findJar(jar)
                if jar_path is not None:
                    if "site-specific" not in jar_path:
                        classpath.append(jar_path)
                    else:
                        ss_classpath.append(jar_path)

        classpath = ss_classpath + classpath
        # binJar = foundFiles[0] + ';'
        # genesisBinDir = os.environ['GENESIS_HOME'] + '/genesis/bin/*:'
        # genesisLibDir = os.environ['GENESIS_HOME'] + '/genesis/lib/*:'

        generatedJars = walkForJars()

        for jar in generatedJars:
            genClassPath.append(jar)

        if useGenerated:
            classpath += genClassPath

        logging.debug("final classpath [module=%s]: \n%s", moduleName, '\n'.join(classpath))

        return classpath


def get_jar_from_site_specific(found_files: List[str]) -> Union[None, str]:
    for file in found_files:
        if "site-specific" in file:
            return file
    else:
        return None


def getDependenciesForJar(jar_name: str) -> List[str]:
    p = Popen(['unzip', '-p', jar_name, 'META-INF/MANIFEST.MF'], stdin=PIPE, stdout=PIPE, stderr=PIPE)

    logging.debug("get dependencies for jar " + jar_name)

    output_bytes, err = p.communicate()
    output = output_bytes.decode('utf-8')

    idx = output.find('Class-Path')
    cp = output[idx:]

    # Find the start of the classpath
    lines = cp.split('\n')
    started = False
    classpath = ""
    for line in lines:
        if line.startswith("Class-Path"):
            started = True

        if started:
            if line.find(":") > -1 and not line.startswith("Class-Path"):
                started = False
            else:
                classpath += line

    cp = classpath.replace('\n', '').replace('\r ', '')
    # Remove 'Class-Path: ' from string
    cp = cp[12:]
    # split space separated list into array
    jars = cp.split()

    jarWildcards = []
    for jar in jars:
        if (jar.startswith('genesis')) and ('SNAPSHOT' not in jar):
            jarVersion = get_major_and_minor_versions(jar)
            jarWildcard = jar[:(jar.index(jarVersion) + 1) + len(jarVersion)] + "*.jar"
            jarWildcards.append(jarWildcard)
        else:
            jarWildcards.append(jar)

    logging.debug("jarWildcards: [jar name: %s] \n%s", jar_name, '\n'.join(jarWildcards))

    return jarWildcards


def get_major_and_minor_versions(jar_name: str) -> Union[None, str]:
    m = JAR_MAJ_MIN_EXPRESSION.findall(jar_name)
    if m is not None and len(m) > 0:
        return m[len(m) - 1]
    else:
        logging.warning('Jar version could not be extracted for [' + jar_name + ']')


def findJar(jar_name: str) -> Union[None, str]:
    ignoreDirectories = ["generated", "runtime"]
    directories = os.listdir(os.environ["GENESIS_HOME"])
    directories = [x for x in directories if x not in ignoreDirectories]
    foundFiles = []

    for dir in directories:
        binJarFile = glob.glob(os.environ["GENESIS_HOME"] + '/' + dir + '/bin/' + jar_name)
        libJarFile = glob.glob(os.environ["GENESIS_HOME"] + '/' + dir + '/lib/' + jar_name)
        jarFile = binJarFile if binJarFile.__len__() else libJarFile

        if len(jarFile) > 1:
            raise Exception("Multiple modules found for " + jar_name)
        elif len(jarFile) == 1:
            foundFiles.append(jarFile[0])

    if len(foundFiles):
        siteSpecificJar = get_jar_from_site_specific(foundFiles)
        if siteSpecificJar:
            return siteSpecificJar
        else:
            return foundFiles[0]
    if DISABLE_MANIFEST_CHECK.casefold() == 'true':
        valuesToPrint = sys.argv[:] + [' failed to find ' + jar_name + ' (manifest checks are disabled).']
        logAction(' '.join(valuesToPrint))
        return None
    else:
        raise Exception("Failed to find dependency: " + jar_name)


def walkForJars() -> List[str]:
    jars = []
    sys_def = read_system_definition()
    deployed_product = None

    if 'DEPLOYED_PRODUCT' in sys_def:
        deployed_product = sys_def['DEPLOYED_PRODUCT']
        if deployed_product == "":
            deployed_product = None

    if deployed_product is None:
        for root, dirs, files in os.walk(os.environ['GENESIS_HOME'] + '/generated/'):
            for name in files:
                if name.endswith(".jar"):
                    jars.append(root + '/' + name)

    else:
        for root, dirs, files in os.walk(os.environ['GENESIS_HOME'] + '/' + deployed_product + '/bin/'):
            for name in files:
                if '-generated-' in name:
                    jars.append(root + '/' + name)

    logging.debug("walk for jars - [deployed product: %s] \n%s", deployed_product, '\n'.join(jars))

    return jars


def getProcessAttribute(processName: str, attrName: str) -> List[str]:
    res = []

    process = getProcessDefinition(processName)
    attrs = process.getElementsByTagName(attrName)

    # import pdb; pdb.set_trace()

    if process is None or len(attrs) == 0:
        return []

    for attr in attrs:
        attr = attr.firstChild.nodeValue
        res.append([attr])

    return res


def getProcessConfigFiles(process_name: str) -> List[Tuple[str, List[Any]]]:
    configs = []

    process = getProcessDefinition(process_name)
    cfgs = process.getElementsByTagName('config')

    if process is None or len(cfgs) == 0:
        return []

    for cfg in cfgs:
        cfg = cfg.firstChild.nodeValue
        if str(cfg).lower().endswith(".xml"):
            configs.append(getXmlIncludes(cfg))
        else:  # kts..
            configs.append((cfg, []))

    return configs


def getXmlIncludes(filename) -> Tuple[str, List[Any]]:
    includes = []
    path = os.environ['GENESIS_HOME'] + '/generated/cfg/' + filename
    xml = minidom.parse(path)

    xis = xml.getElementsByTagName('xi:include')

    for xi in xis:
        include_filename = xi.getAttribute('href')
        includes = includes + [getXmlIncludes(include_filename)]

    # base case
    return filename, includes


def getAdditionalServiceInformation(processName: str) -> List[Union[None, Any]]:
    """
    Get the service definition for a given process name
    """
    res = {}
    try:
        xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/global-service-definitions.xml')
        services = xmlDoc.getElementsByTagName('service')

        service = [s for s in services if s.getAttribute('name') == processName][0]
        for key in ["port", "secure"]:
            if service.hasAttribute(key):
                res[key] = service.getAttribute(key)
            else:
                res[key] = None
    except:
        res["port"] = "????"
        res["secure"] = False

    return res


def getAdditionalServicesInformation(processNames: List[str]) -> List[Union[None, Any]]:
    """
    Get the service definition for a given list of processes name
    """
    xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/global-service-definitions.xml')
    services = xmlDoc.getElementsByTagName('service')

    servicesInformation = [None] * len(processNames)

    counter = 0

    for processName in processNames:
        res = {}
        try:
            service = [s for s in services if s.getAttribute('name') == processName][0]
            for key in ["port", "secure"]:
                if service.hasAttribute(key):
                    res[key] = service.getAttribute(key)
                else:
                    res[key] = None

        except:
            res["port"] = "????"
            res["secure"] = False
        servicesInformation[counter] = res
        counter = counter + 1

    return servicesInformation


def getProcessDefinition(processName: str):
    """
    Get the process definition for a given process name
    """

    xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/processes.xml')
    processList = xmlDoc.getElementsByTagName('process')

    for process in processList:
        if process.attributes['name'].value == processName:
            return process
    return None


def user_name() -> str:
    """
    :return: the current os user
    """
    return subprocess.Popen('whoami', stdout=subprocess.PIPE).stdout.read().decode('utf-8')


def checkProcessIsRunning(processName: str) -> Union[str, None]:
    """
    Check a process with the given name is running
    """

    pid = None
    userName = user_name()
    cmd = 'ps -lf -u ' + userName
    args = shlex.split(cmd)
    output, error = subprocess.Popen(args, stdout=subprocess.PIPE, stderr=subprocess.PIPE).communicate()
    processes = output.decode('utf-8').split('\n')

    for process in processes:
        if (process.find(" " + processName + " ") > -1 and
                process.find(str(os.getpid())) == -1 and
                process.find('killProcess') == -1 and
                process.find('startProcess') == -1 and
                process.find('GenesisRun') == -1):
            processSplit = process.split()
            pid = processSplit[3]

    return pid

def checkScriptIsRunning(scriptName) -> Union[str, None]:
    """
    Check a script with the given name is running.
    """

    pid = None
    userName = user_name()
    cmd = 'ps -lf -u ' + userName
    args = shlex.split(cmd)
    output,error = subprocess.Popen(args,stdout=subprocess.PIPE,stderr=subprocess.PIPE).communicate()
    processes = output.decode('utf-8').split('\n')

    for process in processes:
        if (process.endswith(scriptName) and
                process.find(str(os.getpid())) == -1 and
                process.find('killProcess') == -1 and
                process.find('startProcess') == -1 and
                process.find('GenesisRun') == -1):
            processSplit = process.split()
            pid = processSplit[3]

    return pid



def checkDaemonIsRunning() -> Union[None, int]:
    """
    Check if daemon is running
    """
    pid = None
    userName = user_name()
    cmd = 'ps -lf -u ' + userName
    args = shlex.split(cmd)
    output = subprocess.check_output(args)
    processes = output.decode('utf-8').split('\n')

    for process in processes:
        if (process.find("ResourceDaemonStarter") > -1 and
                process.find(str(os.getpid())) == -1):
            processSplit = process.split()
            pid = processSplit[3]

    return pid


def checkProcessesAreRunning(givenProcesses) -> List[Union[None, str]]:
    """
    Check a process with the given name is running
    """
    # myPid = str(os.getpid())
    pids = [None] * len(givenProcesses)
    # This has better compatibility with other linux systems
    # cmd = 'ps -lf -u ' + userName
    # This is faster in CentOs
    cmd = 'ps -o pid,cmd -u ' + user_name()
    args = shlex.split(cmd)
    output, error = subprocess.Popen(args, stdout=subprocess.PIPE, stderr=subprocess.PIPE).communicate()
    foundProcesses = output.decode('utf-8').split('\n')

    i = 0
    for givenProcess in givenProcesses:
        processString = " " + givenProcess + " "
        for foundProcess in foundProcesses:
            if processString in foundProcess and 'java' in foundProcess:
                processSplit = foundProcess.strip().split(' ', 2)
                # Sometimes there is a space before the PID for padding purposes
                if processSplit[0] != '':
                    pids[i] = processSplit[0]
                else:
                    pids[i] = processSplit[1]
                break

        i = i + 1

    return pids


def getListOfProcessNames() -> List[str]:
    """
    Get list of startable processes
    """
    processes = []
    xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/processes.xml')
    processList = xmlDoc.getElementsByTagName('process')
    processNames = [i.attributes['name'].value for i in processList]
    pids = genesis.checkProcessesAreRunning(processNames)
    counter = 0
    for process in processList:
        start = process.getElementsByTagName('start')
        if start is None:
            start = 'true'
        else:
            start = start[0].firstChild.nodeValue
        if start == 'true':
            processes.append(processNames[counter])
        if start == 'false' and pids[counter] is not None:
            processes.append(processNames[counter])
        counter = counter + 1

    return processes


def getCoreProcesses():
    """
    Get list of processes belongs to 'AUTH' and 'GENESIS' groupId
    :return: coreProcesses
    """
    coreProcesses = (getListOfProcessNamesByGroupId('AUTH') + getListOfProcessNamesByGroupId('GENESIS'))
    coreProcesses.sort()
    return coreProcesses


def getNonCoreProcesses():
    """
    Get list of processes does not belong to 'AUTH' and 'GENESIS' groupId
    :return: nonCoreProcesses
    """
    nonCoreProcesses = []
    coreProcesses = getCoreProcesses()
    processes = getListOfProcessNames()

    for process in processes:
        if process not in coreProcesses:
            nonCoreProcesses.append(process)
    nonCoreProcesses.sort()
    return nonCoreProcesses


def getListOfProcessNamesByGroupId(gid: str) -> List[str]:
    """
    Get list of startable processes which share the same groupId
    """
    gid_processes = getListOfProcessNames()

    # filter the ones from in the specified groupId

    xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/processes.xml')
    processList = xmlDoc.getElementsByTagName('process')

    for process in processList:
        if len(process.getElementsByTagName('groupId')) == 0 \
                or process.getElementsByTagName('groupId')[0].firstChild.nodeValue != gid:
            if process.attributes['name'].value in gid_processes:
                gid_processes.remove(process.attributes['name'].value)

    return gid_processes


def getDictOfProductDetails() -> Dict[str, str]:
    """
    Get list of product names and versions
    """
    processes = {}

    xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/global-product-details.xml')
    processList = xmlDoc.getElementsByTagName('details')
    """
    XML is generated and handled during genesisInstall and should be fine.
    No need for checking for null or wrong values.
    """
    for process in processList:
        name = process.getElementsByTagName('name')[0].firstChild.nodeValue
        version = process.getElementsByTagName('version')[0].firstChild.nodeValue
        processes[name] = version
    return processes


def getUser() -> str:
    """
    Get username that is being used
    """
    current_id = subprocess.getoutput("id -u -n").strip(' \t\n\r')
    original_id = subprocess.getoutput("logname").strip(' \t\n\r')
    # during init.d execution there is no output for logname so we much capture that
    if 'no login name' in original_id:
        return current_id + " (init.d)"

    if current_id == original_id:
        return current_id
    else:
        # if they differ, that means someone SSHd into a different account
        #   and then su-ed into the current account
        return current_id + " (SSHd as " + original_id + ")"


def getSSHHostname() -> str:
    """
    Get hostname of the user that is accessing the machine by SSH
    """
    try:
        return subprocess.getoutput("who am i | awk '{print $5}'").strip(' \t\n\r()')
    except Exception:
        return "UNKNOWN"


def getLogDirectory() -> str:
    return subprocess.getoutput("echo $L").strip(' \t\n\r')


def getLogFile() -> str:
    return getLogDirectory() + '/system.log'


def getServerUptime() -> str:
    # if getLastServerStart() is n/a we can't give an uptime
    try:
        last_start = datetime.datetime.strptime(getLastServerStart(), DEFAULT_DATETIME_FORMAT)
        current_time = datetime.datetime.now()
        delta_time = current_time - last_start
        uptime = ""
        if delta_time.days > 0:
            uptime += "{d} days ".format(d=delta_time.days)
        if delta_time.seconds >= 0:
            m, s = divmod(delta_time.seconds, 60)
            h, m = divmod(m, 60)
            if h > 0:
                uptime += "{h} hours ".format(h=h)
            if m > 0:
                uptime += "{m} minutes ".format(m=m)
            # e.g.: display something even if server came up less than a minute ago
            if delta_time.seconds < 60:
                uptime += "{s} seconds ".format(s=s)

        return uptime.strip()
    except Exception:
        return "n/a"


def getLastServerStart():
    """
    Get server last start. Since the 'startServer' script moves all .logs, the
    last time the server was started is the first line in $L/system.log which
    contains the first 'startServer' use.
    """
    # if the log file we cannot know when it was started last time, so we return n/a
    try:
        with open(getLogFile(), 'r') as f:
            for line in f:
                if "startServer" in line:
                    last_start = " ".join(line.split()[:2])
                    f.close()
                    return last_start
    except Exception:
        pass
    return "n/a"


def log():
    logAction(' '.join(sys.argv[:]))


def logAction(cmd):
    if cmd != '':
        cmds = cmd.split()
        cmds[0] = cmds[0].split("/")[-1]
        action = ' '.join(cmds[:])

        log_file = getLogFile()
        log_msg = "{date} [{user} @ {useraddr}]: {cmd}\n".format(
            date=datetime.datetime.now().strftime(DEFAULT_DATETIME_FORMAT),
            cmd=action,
            user=getUser(),
            useraddr=getSSHHostname(),
        )
        with io.open(log_file, 'a') as f:
            f.write(str(log_msg))
            f.close()


def isServerDown():
    processes = getListOfProcessNames()
    isDown = True
    for process in processes:
        pid = genesis.checkProcessIsRunning(process)
        if pid is not None:
            isDown = False
            break
    if isDown == True:
        return 0
    else:
        return 1
