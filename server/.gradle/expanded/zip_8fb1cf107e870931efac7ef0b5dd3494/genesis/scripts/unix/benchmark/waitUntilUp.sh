#!/usr/bin/env bash

expected=(`mon | egrep '^(?:[0-9]+|n/a)' | wc -l`)
count=`cat *.log | egrep 'Setting process [A-Z_]+ to UP' | wc -l`

while [[ ${count} -lt expected ]]
do
        echo "Processes started $count"
        sleep 5
        count=(`cat *.log | egrep 'Setting process [A-Z_]+ to UP' | wc -l`)
done

echo "Processes started $count"
