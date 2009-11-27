#!/bin/bash

repository="/home/thom/workspace/repositories/linux-2.6"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"v2.6.24..v2.6.25 linux_2_6_24_to_25"
	"v2.6.25..v2.6.26 linux_2_6_25_to_26"
	"v2.6.26..v2.6.27 linux_2_6_26_to_27"
	"v2.6.27..v2.6.28 linux_2_6_27_to_28"
	"v2.6.28..v2.6.29 linux_2_6_28_to_29"
	"v2.6.29..v2.6.30 linux_2_6_29_to_30"
	"v2.6.30..v2.6.31 linux_2_6_30_to_31"
)

for mining in "${minings[@]}"; do
        var=(`echo $mining | tr ' ' ' '`)
        branch=${var[0]}
        database=${var[1]}
        if [ "$1" = "db" ]; then
		echo "Running ./gitup ${repository} -b ${branch} -d ${database} -o"
                ./gitup ${repository} -b ${branch} -d ${database} -o
		echo ""
		echo "Running ./shiatsu ${database} -m ${module_depth}"
                ./shiatsu ${database} -m ${module_depth}
		echo ""
        fi
	echo "Running ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -mi ${maximum_items} -n ${name} -o"
        ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -mi ${maximum_items} -n ${name} -o
done
