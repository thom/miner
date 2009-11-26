#!/bin/bash

repository="/home/thom/workspace/repositories/wine"
module_depth=4
commits=4
min_support=10
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"wine-0.9 wine_0_9"
	"wine-0.9.30 wine_0_9_30"
	"wine-1.0 wine_1_0"
	"wine-1.1.0 wine_1_1_0"
	"wine-1.1.15 wine_1_1_15"
	"wine-1.1.30 wine_1_1_30"
	"master wine_master"
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
