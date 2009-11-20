#!/bin/bash

repository="/home/thom/workspace/repositories/linux-2.6"
module_depth=4
commits=5
min_support=20
maximum_commits=60

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"v2.6.24 linux_2_6_24"
	"v2.6.25 linux_2_6_25"
	"v2.6.26 linux_2_6_26"
	"v2.6.27 linux_2_6_27"
	"v2.6.28 linux_2_6_28"
	"v2.6.29 linux_2_6_29"
	"v2.6.30 linux_2_6_30"
	"v2.6.31 linux_2_6_31"
	"master linux_2_6_master"
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
	echo "Running ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -o"
        ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -o
done
