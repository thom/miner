#!/bin/bash

repository="/home/thom/workspace/repositories/samba"
module_depth=4
commits=7
maximum_commits=50

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"origin/v3-0-stable samba_3_0"
	"origin/v3-2-stable samba_3_2"
	"origin/v3-3-stable samba_3_3"
	"origin/v3-4-stable samba_3_4"
	"master samba_master"
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
