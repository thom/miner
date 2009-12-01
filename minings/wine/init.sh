#!/bin/bash

repository="/home/thom/workspace/repositories/wine"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"wine-0.9..wine-0.9.20 wine_0_9_to_0_9_20"
	"wine-0.9.20..wine-0.9.40 wine_0_9_20_to_0_9_40"
	"wine-0.9.40..wine-0.9.61 wine_0_9_40_to_0_9_61"
	"wine-0.9.61..wine-1.0 wine_0_9_61_to_1_0"
	"wine-1.0..wine-1.1.0 wine_1_0_to_1_1_0"
	"wine-1.1.0..wine-1.1.20 wine_1_1_0_to_1_1_20"
	"wine-1.1.20..wine-1.1.33 wine_1_1_20_to_1_1_33"
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
