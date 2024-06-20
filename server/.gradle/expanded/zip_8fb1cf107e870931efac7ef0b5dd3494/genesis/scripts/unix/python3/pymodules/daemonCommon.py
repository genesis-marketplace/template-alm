import json
import urllib.error
import urllib.parse
import urllib.request
from urllib.error import URLError

import genesis


def check_daemon_script_controller_is_enabled():
    system_definition = genesis.read_system_definition()
    if system_definition.get('EnableDaemonScriptController', "").lower() != 'true':
        print("Since genesis 6.5.0 the remote script endpoint is disabled by default")
        print("To enable cluster control from a single node you must set EnableDaemonScriptController "
              "to true in your system definition config")
        exit(1)


def get_daemon_port():
    system_definition = genesis.read_system_definition()
    return system_definition['DaemonServerPort']


def execute_service_call(host, cont):
    obj = json.dumps(cont)
    contents = urllib.request.Request("http://" + host + ":" + str(get_daemon_port()) + "/scripts/execute", obj)
    f = urllib.request.urlopen(contents)
    response = f.read()
    execution_code = json.loads(response)['executionCode']
    f.close()
    return execution_code


def get_command_name(argv):
    command_canonical_path = argv[0]
    return command_canonical_path[command_canonical_path.rindex('/') + 1:]


def read_hostnames_from_definition():
    system_definition = genesis.read_system_definition()
    return system_definition['HOSTS']


def execute_remote_call(args, host_list, argv):
    check_daemon_script_controller_is_enabled()
    exit_code = 0
    cont = {"args": args, "command": get_command_name(argv)}

    try:
        for host in host_list:
            exit_code += execute_service_call(host, cont)

    except URLError as e:
        print(e.args)
        exit_code += 1

    return exit_code


def daemon_status() -> str:
    if genesis.checkDaemonIsRunning() is None:
        return 'MISSING'

    port = get_daemon_port()
    try:
        urllib.request.urlopen("http://localhost:" + port + "/heartbeat").read()
    except:
        return 'STARTING'
    return 'OK'