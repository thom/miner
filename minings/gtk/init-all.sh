#!/bin/bash

repository="/home/thom/infosys/repositories/gtk"
database="gtk_all"
module_depth=3
commits=4
min_support=12
maximum_items=-1
maximum_commits=50
name=default

if [ "$1" = "db" ]; then
	echo "Running ./gitup ${repository} -d ${database} -a -o"
	./gitup ${repository} -d ${database} -a -o
	echo ""
	echo "Running ./shiatsu ${database} -m ${module_depth}"
	./shiatsu ${database} -m ${module_depth}
	echo ""
fi
echo "Running ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -mi ${maximum_items} -n ${name} -o"
./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -mi ${maximum_items} -n ${name} -o
