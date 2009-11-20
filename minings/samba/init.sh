#!/bin/bash

repository="/home/thom/workspace/repositories/samba"
module_depth=4
commits=7
min_support=7
maximum_commits=50

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"release-1-9-17 samba_1_9_17"
	"release-2-0-0 samba_2_0_0"
	"release-2-2-0 samba_2_2_0"
	"origin/v3-0-stable samba_3_0_branch"
	"origin/v3-2-stable samba_3_2_branch"
	"origin/v3-3-stable samba_3_3_branch"
	"origin/v3-4-stable samba_3_4_branch"
	"origin/v4-0-stable samba_4_0_branch"
	"master samba_master"
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
