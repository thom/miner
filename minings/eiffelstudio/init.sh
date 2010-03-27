#!/bin/bash

repository="/home/thom/infosys/repositories/eiffelstudio"
module_depth=3
commits=4
min_support=4
maximum_items=-1
maximum_commits=50
name=default

# Format of minings: "BRANCH/TAG DATABASE"
minings=(
	"remotes/Eiffel_54 eiffel_54"
	"remotes/Eiffel_55 eiffel_55"
	"remotes/Eiffel_56 eiffel_56"
	"remotes/Eiffel_57 eiffel_57"
	"remotes/Eiffel_60 eiffel_60"
	"remotes/Eiffel_63 eiffel_63"
	"remotes/Eiffel_64 eiffel_64"
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
