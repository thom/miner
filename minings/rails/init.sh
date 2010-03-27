#!/bin/bash

repository="/home/thom/infosys/repositories/rails"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"v1.0.0..v1.1.0 rails_v1_0_0_to_v1_1_0"
	"v1.1.0..v1.1.6 rails_v1_1_0_to_v1_1_6"
	"v1.1.6..v1.2.0 rails_v1_1_6_to_v1_2_0"
	"v1.2.0..v1.2.6 rails_v1_2_0_to_v1_2_6"
	"v1.2.6..v2.0.0 rails_v1_2_6_to_v2_0_0"
	"v2.0.0..v2.0.5 rails_v2_0_0_to_v2_0_5"
	"v2.0.5..v2.1.0 rails_v2_0_5_to_v2_1_0"
	"v2.1.0..v2.1.2 rails_v2_1_0_to_v2_1_2"
	"v2.1.2..v2.2.0 rails_v2_1_2_to_v2_2_0"
	"v2.2.0..v2.2.3 rails_v2_2_0_to_v2_2_3"
	"v2.2.3..v2.3.0 rails_v2_2_3_to_v2_3_0"
	"v2.3.0..v2.3.5 rails_v2_3_0_to_v2_3_5"
	"v2.3.5..v3.0.0.beta1 rails_v2_3_5_to_v3_0_0_beta1"
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
