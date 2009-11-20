#!/bin/bash

repository="/home/thom/workspace/repositories/linux-2.6"
module_depth=4
commits=5
min_support=25
maximum_commits=50

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"v2.6.19 linux_2_6_19"
	"v2.6.20 linux_2_6_20"
	"v2.6.21 linux_2_6_21"
	"v2.6.22 linux_2_6_22"
	"v2.6.23 linux_2_6_23"
	"v2.6.24 linux_2_6_24"
	"v2.6.25 linux_2_6_25"
	"v2.6.26 linux_2_6_26"
	"v2.6.27 linux_2_6_27"
	"v2.6.28 linux_2_6_28"
	"v2.6.29 linux_2_6_29"
	"v2.6.30 linux_2_6_30"
	"v2.6.31 linux_2_6_31"
	"master linux_2_6_master"
)

for mining in "${minings[@]}"; do
        var=(`echo $mining | tr ' ' ' '`)
        branch=${var[0]}
        database=${var[1]}
        if [ "$1" = "db" ]; then
                ./gitup ${repository} -b ${branch} -d ${database} -o
                ./shiatsu ${database} -m ${module_depth}
        fi
        ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -o
done
