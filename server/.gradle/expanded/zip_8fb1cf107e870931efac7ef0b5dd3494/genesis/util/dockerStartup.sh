#!/bin/bash

while IFS='=' read -r -d '' n v; do
  if [[ $n == GENESIS_SYSDEF_* ]];
  then
    echo "export $n=\"$v\"" >> ~/.bashrc
  fi
done < <(env -0)

source ~/.bashrc
shopt -s expand_aliases

if [ "$GENESIS_PRIMARY" == "true" ]; then
  set_primary=true
fi
REMAP_COMMAND=remap

if [ "$GENESIS_DB_DRY_RUN" != "true" ]; then
  REMAP_COMMAND="yes | $REMAP_COMMAND --commit"
fi

if [ "$GENESIS_DB_INSTALL" == "true" ]; then
  eval $REMAP_COMMAND || exit $?
  if [ "$GENESIS_DB_DRY_RUN" != "true" ]; then
    genesisInstallHooks --init
  fi
elif [ "$GENESIS_DB_UPGRADE" == "true" ]; then
  if [ "$GENESIS_DB_DRY_RUN" != "true" ]; then
    genesisInstallHooks || exit $?
  fi
  eval $REMAP_COMMAND
else
  waitForProcess() {
    processName=$1
    TERM=xterm
    status=""
    statusCount=0
    while :; do
      newStatus=$(mon | grep "$processName" | grep -v "Daemon missing" | sed -E "s/.*$processName +[0-9]+[^A-Z]+([A-Z_]+).*/\1/g")
      case $newStatus in
      RUNNING)
        echo "$processName: $status"
        break
        ;;
      MISSING)
        if [[ $statusCount != 0 ]]; then
          echo "$processName exited unexpectedly"
          exit 1
        fi
        ;;
      *)
        if [[ $status != "$newStatus" ]]; then
          status=$newStatus
          statusCount=1
          echo "$processName: $status"
        fi
        ;;
      esac
      sleep 0.5
    done
  }

  start() {
    startServer --verbose

    if [[ -v set_primary ]]; then
      waitForProcess "GENESIS_CLUSTER"
      echo y | SetPrimary
    fi

    sudo -i nginx -g 'daemon off;' &
    nginxPid=$!
    wait "$nginxPid"
  }

  shutdown() {
    echo 'Initiating graceful shutdown'
    nginx -s stop
    killServer --all --force
    exit
  }

  trap shutdown SIGTERM
  start
fi
