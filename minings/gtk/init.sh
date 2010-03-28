#!/bin/bash

repository="/home/thom/infosys/repositories/gtk"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"GTK_0_99_0..GTK_1_0_0 gtk_0_99_0_to_1_0_0"
	"GTK_1_0_0..GTK_1_1_0 gtk_1_0_0_to_1_1_0"
	"GTK_1_1_0..GTK_1_2_0 gtk_1_1_0_to_1_2_0"
	"GTK_1_2_0..GTK_1_3_1 gtk_1_2_0_to_1_3_1"
	"GTK_1_3_1..GTK_2_0_0 gtk_1_3_1_to_2_0_0"
	"GTK_2_0_0..GTK_2_1_0 gtk_2_0_0_to_2_1_0"
	"GTK_2_1_0..GTK_2_2_0 gtk_2_1_0_to_2_2_0"
	"GTK_2_2_0..GTK_2_3_0 gtk_2_2_0_to_2_3_0"
	"GTK_2_3_0..GTK_2_4_0 gtk_2_3_0_to_2_4_0"
	"GTK_2_4_0..GTK_2_5_0 gtk_2_4_0_to_2_5_0"
	"GTK_2_5_0..GTK_2_6_0 gtk_2_5_0_to_2_6_0"
	"GTK_2_6_0..GTK_2_7_0 gtk_2_6_0_to_2_7_0"
#	"GTK_2_7_0..GTK_2_8_0 gtk_2_7_0_to_2_8_0"
	"GTK_2_8_0..GTK_2_9_0 gtk_2_8_0_to_2_9_0"
	"GTK_2_9_0..GTK_2_10_0 gtk_2_9_0_to_2_10_0"
	"GTK_2_10_0..GTK_2_11_0 gtk_2_10_0_to_2_11_0"
	"GTK_2_11_0..GTK_2_12_0 gtk_2_11_0_to_2_12_0"
#	"GTK_2_12_0..GTK_2_13_0 gtk_2_12_0_to_2_13_0"
	"GTK_2_13_0..GTK_2_14_0 gtk_2_13_0_to_2_14_0"
	"GTK_2_14_0..GTK_2_15_0 gtk_2_14_0_to_2_15_0"
	"GTK_2_15_0..GTK_2_16_0 gtk_2_15_0_to_2_16_0"
#	"GTK_2_16_0..2.17.0 gtk_2_16_0_to_2_17_0"
	"2.17.0..2.18.0 gtk_2_17_0_to_2_18_0"
	"2.18.0..2.19.0 gtk_2_18_0_to_2_19_0"
	"2.19.0..2.20.0 gtk_2_19_0_to_2_20_0"

)

for mining in "${minings[@]}"; do
        var=(`echo $mining | tr ' ' ' '`)
        branch=${var[0]}
        database=${var[1]}
        if [ "$1" = "db" ]; then
		echo "Running ./gitup ${repository} -b ${branch} -d ${database} -o"
                ./gitup ${repository} -b ${branch} -d ${database} -o
		echo ""
		echo "Running ./shiatsu ${database} -m ${module_depth} -ep \"\./demos/.*|\./examples/.*|\./tests/.*\""
                ./shiatsu ${database} -m ${module_depth} -ep "\./demos/.*|\./examples/.*|\./tests/.*"
		echo ""
        fi
	echo "Running ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -mi ${maximum_items} -n ${name} -o"
        ./miner ${database} -s -${min_support} -c ${commits} -mc ${maximum_commits} -mi ${maximum_items} -n ${name} -o
done
