from xml.dom import minidom
import os
import genesis


def validate_hosts_exists(host_name_arr):
    if not host_name_arr or len(host_name_arr) == 0:
        return False

    system_definition = genesis.read_system_definition()
    host_elems = system_definition['HOSTS']

    if host_elems and len(host_elems):
        intersection = [value for value in host_name_arr if value in host_elems]
        return len(intersection) == len(host_name_arr)

    else:
        return False


def validate_processes_exists(process_name_arr):
    if not process_name_arr or len(process_name_arr) == 0:
        return False

    processes_elems = _get_elements('processes.xml', 'process')
    proc_name_attrs = []

    if processes_elems and len(processes_elems):
        for proc in processes_elems:
            proc_name_attrs.append(str(proc.getAttribute('name')))

        intersection = [value for value in process_name_arr if value in proc_name_attrs]
        return len(intersection) == len(process_name_arr)

    else:
        return False


def validate_process_groups_exists(group_id_arr):
    if not group_id_arr or len(group_id_arr) == 0:
        return False

    group_id_elems = _get_elements('processes.xml', 'groupId')
    group_name_values = []

    if group_id_elems and len(group_id_elems):
        for gId in group_id_elems:
            group_name_values.append(str(gId.firstChild.data))

        intersection = [value for value in group_id_arr if value in group_name_values]
        return len(intersection) == len(group_id_arr)

    else:
        return False


def _get_elements(file_name, tag):
    config_file = os.environ['GENESIS_HOME'] + '/generated/cfg/' + file_name
    return minidom.parse(config_file).getElementsByTagName(tag)
