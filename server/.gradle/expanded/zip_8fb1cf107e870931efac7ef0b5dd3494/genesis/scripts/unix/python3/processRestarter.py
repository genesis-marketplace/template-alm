#!/usr/bin/env python3

import genesis
from xml.dom import minidom
import subprocess
import os
import sys
import time
from datetime import datetime

def getListOfRestartableProcesses(tagName: str):
    """
    Get list of startable processes which share the same groupId
    """
    tagName_processes = []
    print(str(datetime.now()) + " Getting list of processes set for scheduled restart")

    xmlDoc = minidom.parse(os.environ['GENESIS_HOME'] + '/generated/cfg/processes.xml')
    processList = xmlDoc.getElementsByTagName('process')

    for process in processList:
        if len(process.getElementsByTagName(tagName)) != 0 and process.getElementsByTagName(tagName)[0].firstChild.nodeValue == "true":
            tagName_processes.append(process.attributes['name'].value)
    
    return tagName_processes
    
def startProcessesIfDead(processRestartCountDictionary, deadProcesses):

    for processName in processRestartCountDictionary.keys():
        pid = genesis.checkProcessIsRunning(processName)
        processRestartDir = os.environ["GENESIS_HOME"] + "runtime/processRestartFlags/"
        
        if pid is None and os.path.isfile(processRestartDir + processName + ".txt") is False:
            if processRestartCountDictionary[processName] <= 2:
                processRestartCountDictionary[processName] = processRestartCountDictionary[processName] + 1
                subprocess.call(['startProcess', processName] + sys.argv[1:])
                print(str(datetime.now()) + "Restarting process " + processName + " count " + str(processRestartCountDictionary[processName]))
            else:
                print(str(datetime.now()) + "Process " + processName + " is crashing!!!. Process crashed after trying to restart it for 3 times")
                deadProcesses.append(processName)


if genesis.checkScriptIsRunning("processRestarter.py"):
    print('Process restarter is already running')
else:
    deadProcesses = []
    while True:
        processNames = getListOfRestartableProcesses('scheduleRestart')

        if deadProcesses:
            for processName in deadProcesses:
                processNames.remove(processName)

        processRestartCountDictionary = dict.fromkeys(processNames, 0)

        i = 0
        while i <= 3:
            startProcessesIfDead(processRestartCountDictionary, deadProcesses)
            time.sleep(60)
            i = i+1

