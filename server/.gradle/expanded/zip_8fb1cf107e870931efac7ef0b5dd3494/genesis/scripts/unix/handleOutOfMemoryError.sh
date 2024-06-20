#!/bin/bash

# Previously we took a heap dump here, but we can no longer do this
# with a process that is non-responsive with JDK tools in JDK 11+.
# We now use JVM args in the startProcess script to control heap dumps,
# but we still need to issue a kill order here otherwise the process may
# continually dump its heap, filling up disk space.
kill -9 $1
