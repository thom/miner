#!/bin/bash

repository="/home/thom/workspace/repositories/xserver"
module_depth=4
commits=5
min_support=5
maximum_items=-1
maximum_commits=60
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"xf86-4_3_0_1 xserver_xf86_4_3_0_1"
	"xf86-4_3_99_902 xserver_xf86_4_3_99_902"
	"XORG-6_7_0 xserver_XORG_6_7_0"
	"XORG-6_8_0 xserver_XORG_6_8_0"
	"XORG-7_0 xserver_XORG_7_0"
	"XORG-7_1 xserver_XORG_7_1"
	"origin/server-1_0-branch xserver_xorg_server_1_0_branch"
	"origin/server-1_1-branch xserver_xorg_server_1_1_branch"
	"origin/server-1.2-branch xserver_xorg_server_1_2_branch"
	"origin/server-1.3-branch xserver_xorg_server_1_3_branch"
	"origin/server-1.4-branch xserver_xorg_server_1_4_branch"
	"origin/server-1.5-branch xserver_xorg_server_1_5_branch"
	"origin/server-1.6-branch xserver_xorg_server_1_6_branch"
	"origin/server-1.7-branch xserver_xorg_server_1_7_branch"
	"master xserver_master"
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
	mysql -u root --password=root ${database} < minings/xserver/fix-tag.sql
done
