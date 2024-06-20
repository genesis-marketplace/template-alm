#!/usr/bin/env bash

if [[ "$#" -ne 2 ]]; then
        echo "Please provide number of benchmark tests to run"
        exit
fi

runs=$1
name=$2

echo "Starting $runs benchmark runs, with name $name"
memory_file="/home/chat/${name}_memory.txt"
startup_file="/home/chat/${name}_startup.txt"

echo 'Setup Process Memory Run' > ${memory_file}
echo 'Setup Process Startup Run' > ${startup_file}

for (( i=1 ; i<=$runs ; i++))
do
        echo "Benchmark run $i"
        echo "Starting server"
        startServer
        ./waitUntilUp.sh
        echo "Getting memory usage"
        ./fancyMemory.sh ${name} ${i} >> ${memory_file}
        echo "Getting startup timings"
        cat ${GENESIS_HOME}/runtime/logs/*.log | egrep 'Setting process [A-Z_]+ to UP' | egrep -o '[A-Z_]+ to UP, startup time: [0-9]+'  | awk -v arg=${name} -v id=${i} '{ print arg " " $1 " " $6 " " id }' >> ${startup_file}
        killServer -f
        killProcess GENESIS_CLUSTER

        if [[ ${i} -ne ${runs} ]]; then
            sleep 15
        fi

done
