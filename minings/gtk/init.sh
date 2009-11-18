#!/bin/sh

repository="/home/thom/workspace/repositories/gtk"
module_depth=3
commits=6
maximum_commits=50

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"origin/gtk-1-0 gtk_1_0"
	"origin/gtk-1-2 gtk_1_2"
	"origin/gtk-2-0 gtk_2_0"
	"origin/gtk-2-2 gtk_2_2"
	"origin/gtk-2-4 gtk_2_4"
	"origin/gtk-2-6 gtk_2_6"
	"origin/gtk-2-8 gtk_2_8"
	"origin/gtk-2-10 gtk_2_10"
	"origin/gtk-2-12 gtk_2_12"
	"origin/gtk-2-14 gtk_2_14"
	"origin/gtk-2-16 gtk_2_16"
	"origin/gtk-2-18 gtk_2_18"
	"master gtk_master"
)

for mining in "${minings[@]}"; do
        var=(`echo $mining | tr ' ' ' '`)
        branch=${var[0]}
        database=${var[1]}
        if [ "$1" = "db" ]; then
                ./gitup ${repository} -b ${branch} -d ${database} -o
                ./shiatsu ${database} -m ${module_depth}
        fi
        ./miner ${database} -s -${commits} -c ${commits} -mc ${maximum_commits} -o
done
