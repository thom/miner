#!/bin/bash

repository="/home/thom/infosys/repositories/gimp"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"GIMP_1_0_0 gimp_beginning_to_1_0_0"
	"GIMP_1_0_0..GIMP_1_1_0 gimp_1_0_0_to_1_1_0"
	"GIMP_1_1_0..GIMP_1_1_8 gimp_1_1_0_to_1_1_8"
	"GIMP_1_1_8..GIMP_1_1_16 gimp_1_1_8_to_1_1_16"
	"GIMP_1_1_16..GIMP_1_2_0 gimp_1_1_16_to_1_2_0"
	"GIMP_1_2_0..GIMP_1_3_0 gimp_1_2_0_to_1_3_0"
	"GIMP_1_3_0..GIMP_1_3_3 gimp_1_3_0_to_1_3_3"
	"GIMP_1_3_3..GIMP_1_3_15 gimp_1_3_3_to_1_3_15"
	"GIMP_1_3_15..GIMP_1_3_20 gimp_1_3_15_to_1_3_20"
	"GIMP_1_3_20..GIMP_2_0_0 gimp_1_3_20_to_2_0_0"
	"GIMP_2_0_0..GIMP_2_1_0 gimp_2_0_0_to_2_1_0"
	"GIMP_2_1_0..GIMP_2_2_0 gimp_2_1_0_to_2_2_0"
	"GIMP_2_2_0..GIMP_2_3_0 gimp_2_2_0_to_2_3_0"
	"GIMP_2_3_0..GIMP_2_4_0 gimp_2_3_0_to_2_4_0"
	"GIMP_2_4_0..GIMP_2_5_0 gimp_2_4_0_to_2_5_0"
	"GIMP_2_5_0..GIMP_2_6_0 gimp_2_5_0_to_2_6_0"
	"GIMP_2_6_0..GIMP_2_7_0 gimp_2_6_0_to_2_7_0"
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
