#!/bin/bash

repository="/home/thom/workspace/repositories/xserver"
module_depth=4
commits=6
maximum_commits=60

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"xf86-4_3_0_1 xserver_xf86_4_3_0_1"
	"xf86-4_3_99_902 xserver_xf86_4_3_99_902"
	"XORG-6_7_0 xserver_XORG_6_7_0"
	"XORG-6_8_0 xserver_XORG_6_8_0"
	"XORG-7_0 xserver_XORG_7_0"
	"XORG-7_1 xserver_XORG_7_1"
	"xorg-server-1.4 xserver_xorg_server_1_4"
	"origin/server-1.6-branch xserver_xorg_server_1_6_branch"
	"xorg-server-1.7.0 xserver_xorg_server_1_7_0"
	"master xserver_master"
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
	mysql -u root --password=root ${database} < minings/xserver/fix-tag.sql
done
