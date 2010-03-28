#!/bin/bash

repository="/home/thom/infosys/repositories/gimp"
module_depth=3
commits=4
min_support=10
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"remotes/origin/gimp-1-0 gimp_1_0_branch"
	"remotes/origin/gimp-1-0-gtk-1-2 gimp_1_0_gtk_1_2_branch"
	"remotes/origin/gimp-1-2 gimp_1_2_branch"
	"remotes/origin/gimp-2-0 gimp_2_0_branch"
	"remotes/origin/gimp-2-2 gimp_2_2_branch"
	"remotes/origin/gimp-2-4 gimp_2_4_branch"
	"remotes/origin/gimp-2-6 gimp_2_6_branch"
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
