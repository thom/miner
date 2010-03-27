#!/bin/bash

repository="/home/thom/infosys/repositories/xserver"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"xf86-4_3_99_902 xserver_beginning_to_xf86_4_3_99_902"
	"xf86-4_3_99_902..XORG-6_7_0 xserver_xf86_4_3_99_902_to_XORG_6_7_0"
	"XORG-6_7_0..XORG-6_8_0 xserver_XORG_6_7_0_to_XORG_6_8_0"
	"XORG-6_8_0..XORG-7_0 xserver_XORG_6_8_0_to_XORG_7_0"
	"XORG-7_0..xorg-server-1_1_0 xserver_XORG_7_0_to_xorg_server_1_1_0"
	"xorg-server-1_1_0..xorg-server-1.2.0 xserver_xorg_server_1_1_0_to_xorg_server_1_2_0"
	"xorg-server-1.2.0..xorg-server-1.3.0.0 xserver_xorg_server_1_2_0_to_xorg_server_1_3_0_0"
	"xorg-server-1.3.0.0..xorg-server-1.4 xserver_xorg_server_1_3_0_0_to_xorg_server_1_4"
	"xorg-server-1.4..xorg-server-1.5.0 xserver_xorg_server_1_4_to_xorg_server_1_5_0"
	"xorg-server-1.5.0..xorg-server-1.6.0 xserver_xorg_server_1_5_0_to_xorg_server_1_6_0"
	"xorg-server-1.6.0..xorg-server-1.7.0 xserver_xorg_server_1_6_0_to_xorg_server_1_7_0"
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
