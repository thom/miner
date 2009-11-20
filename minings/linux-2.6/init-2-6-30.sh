#!/bin/bash

repository="/home/thom/workspace/repositories/linux-2.6.30.y"
module_depth=4
commits=5
min_support=25
maximum_commits=50

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"master linux_2_6_30_master"
)

for mining in "${minings[@]}"; do
        var=(`echo $mining | tr ' ' ' '`)
        branch=${var[0]}
        database=${var[1]}
        if [ "$1" = "db" ]; then
                ./gitup ${repository} -b ${branch} -d ${database} -o
                ./shiatsu ${database} -m ${module_depth}
        fi
        ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -o
done
