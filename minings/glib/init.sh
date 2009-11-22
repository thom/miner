#!/bin/bash

repository="/home/thom/workspace/repositories/glib"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"origin/glib-1-2 glib_1_2_branch"
	"origin/glib-2-0 glib_2_0_branch"
	"origin/glib-2-2 glib_2_2_branch"
	"origin/glib-2-4 glib_2_4_branch"
	"origin/glib-2-6 glib_2_6_branch"
	"origin/glib-2-8 glib_2_8_branch"
	"origin/glib-2-10 glib_2_10_branch"
	"origin/glib-2-12 glib_2_12_branch"
	"origin/glib-2-14 glib_2_14_branch"
	"origin/glib-2-16 glib_2_16_branch"
	"origin/glib-2-18 glib_2_18_branch"
	"origin/glib-2-20 glib_2_20_branch"
	"origin/glib-2-22 glib_2_22_branch"
	"master glib_master"
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
