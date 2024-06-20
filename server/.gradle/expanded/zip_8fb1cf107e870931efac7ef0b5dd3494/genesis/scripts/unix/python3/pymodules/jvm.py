#!/usr/bin/env python
import os
import re
import sys

from typing import Tuple, List, Union, Any, Dict

import genesis

JVM_PROPERTY_REGEX = '-D\S+=\S+'
GENESIS_HOME = os.environ['GENESIS_HOME']
MAX_HEAP = os.getenv('SCRIPT_MAX_HEAP', '-Xmx256m')
JVM_DEFAULT_ARGS = ' --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED --add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED '


def get_jvm_args():
    system_definition = genesis.read_system_definition()

    if 'SCRIPT_JVM_OPTIONS' in system_definition:
        jvm_args = JVM_DEFAULT_ARGS + system_definition['SCRIPT_JVM_OPTIONS']
    else:
        jvm_args = JVM_DEFAULT_ARGS

    return jvm_args


def find_script(scriptName):
    # Find in site-specific
    siteSpecific = GENESIS_HOME + '/site-specific/scripts/'
    siteSpecificScripts = os.listdir(siteSpecific)

    if scriptName in siteSpecificScripts:
        return siteSpecific + scriptName

    # Find in genesis/scripts/unix/groovy
    genesisGroovy = GENESIS_HOME + '/genesis/scripts/unix/groovy/'
    genesisGroovyScripts = os.listdir(genesisGroovy)

    if scriptName in genesisGroovyScripts:
        return genesisGroovy + scriptName

    # Find in genesis product folders
    productDirs = os.listdir(GENESIS_HOME)
    for dir in productDirs:
        try:
            if 'scripts' in os.listdir(GENESIS_HOME + '/' + dir + '/'):
                productScripts = os.listdir(GENESIS_HOME + '/' + dir + '/scripts/')
                if scriptName in productScripts:
                    return GENESIS_HOME + '/' + dir + '/scripts/' + scriptName
        # In case there is something in the directory that is not a directory!
        except OSError as e:
            print("")

    return None


def execute(search, max_heap=MAX_HEAP):
    genesis.log()

    # vars from cli
    scriptName = None
    cli_args = []
    classpath = ""
    if "-modules=" in sys.argv[1]:
        modules = sys.argv[1].replace("-modules=", "").split(",")
        scriptName = sys.argv[2]
        cli_args = sys.argv[3:]
        classpath = genesis.buildClasspathForModules(modules)
    else:
        scriptName = sys.argv[1]
        cli_args = sys.argv[2:]
        classpath = genesis.buildClasspathForModules(["genesis-environment"])

    os.environ['CLASSPATH'] = classpath

    args = []
    jvmArgs = get_jvm_args()

    for arg in [x for x in cli_args if re.match(JVM_PROPERTY_REGEX, x)]:  # add system properties
        jvmArgs = jvmArgs + ' ' + arg

    for arg in [x for x in cli_args if not re.match(JVM_PROPERTY_REGEX, x)]:  # add script arguments
        args.append("\"" + arg + "\"")

    scriptsPath = scriptName

    is_kts_script = scriptName.endswith("-script.kts")
    is_groovy_script = scriptName.endswith(".groovy")
    is_main_class = (not is_kts_script) and (not is_groovy_script)

    # If running in search mode, find groovy script in genesis folders
    if search and not is_main_class:
        scriptsPath = find_script(scriptName)

    if scriptsPath is None:
        print("No file " + scriptName + " found!")
    else:
        if is_main_class:
            main = scriptsPath
        elif is_kts_script:
            main = 'global.genesis.environment.scripts.pal.GenesisPALScriptEvaluator ' + scriptsPath
        else:
            main = 'groovy.lang.GroovyShell ' + scriptsPath

        startJvm = 'java ' + max_heap + ' ' + jvmArgs + ' ' + main + ' ' + ' '.join(args)
        exit(int(os.system(startJvm) / 256))
