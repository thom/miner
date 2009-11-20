#!/bin/bash

repository="/home/thom/workspace/repositories/gtk"
module_depth=3
commits=6
min_support=6
maximum_commits=50

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"origin/gtk-1-0 gtk_1_0_branch"
	"GTK_1_1_0 gtk_1_1_0"
	"origin/gtk-1-2 gtk_1_2_branch"
	"origin/gtk-2-0 gtk_2_0_branch"
	"origin/gtk-2-8 gtk_2_8_branch"
	"origin/gtk-2-12 gtk_2_12_branch"
	"origin/gtk-2-18 gtk_2_18_branch"
	"master gtk_master"
)

for mining in "${minings[@]}"; do
        var=(`echo $mining | tr ' ' ' '`)
        branch=${var[0]}
        database=${var[1]}
        if [ "$1" = "db" ]; then
                ./gitup ${repository} -b ${branch} -d ${database} -o
                ./shiatsu ${database} -m ${module_depth} -ep "\./examples/.*"
        fi
        ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -o
done
