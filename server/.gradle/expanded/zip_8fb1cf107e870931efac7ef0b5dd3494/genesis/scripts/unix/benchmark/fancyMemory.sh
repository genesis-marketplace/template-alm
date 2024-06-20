#!/usr/bin/env bash
# will get a snapshot of the memory usage of any active DTA processes

files=(`mon | egrep '^[0-9]+\s+[A-Z_]+' -o `)
count=(`cat *.log | egrep 'Setting process [A-Z_]+ to UP' | wc -l`)

if [[ "$#" -ne 2 ]]; then
        echo "Please provide number of benchmark tests to run"
        exit
fi

for ((l=0; l<${#files[*]}; l=l+2));
do
        pid="${files[l]}"
        process="${files[l+1]}"
        memory=(`ps -orss --no-headers -q ${pid}`)
        echo "$1 $process $memory $2"
done