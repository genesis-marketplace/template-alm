#!/usr/bin/env python
# encoding: utf-8

import sys
import urllib.request
from http.client import BadStatusLine
from urllib.error import URLError

import daemonCommon

port = daemonCommon.get_daemon_port()
try:
    try:
        urllib.request.urlopen("http://localhost:" + port + "/admin/stop", {}).read()
    except BadStatusLine:
        # When daemon stops status is '' and BadStatusLine is raised. Ignoring console output.
        sys.exit(0)
    sys.exit(0)
except URLError:
    sys.exit(1)
