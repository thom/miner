#!/bin/bash

repository="/home/thom/workspace/repositories/rails"
module_depth=4
commits=4
min_support=6
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"origin/1-2-stable rails_1_2_branch"
	"v2.0.0 rails_2_0_0"
	"origin/2-0-stable rails_2_0_branch"
	"v2.1.0_RC1 rails_2_1_0_rc1"
	"origin/2-1-stable rails_2_1_branch"
	"origin/2-2-stable rails_2_2_branch"
	"origin/2-3-stable rails_2_3_branch"
	"master rails_master"
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
