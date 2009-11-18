#!/bin/sh

module_depth=4
commits=6
maximum_commits=60

# tag: xf86-4_3_0_1
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b xf86-4_3_0_1 -d xserver_xf86_4_3_0_1 -o
	./shiatsu xserver_xf86_4_3_0_1 -m ${module_depth}
fi
./miner xserver_xf86_4_3_0_1 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: xf86-4_3_99_902
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b xf86-4_3_99_902 -d xserver_xf86_4_3_99_902 -o
	./shiatsu xserver_xf86_4_3_99_902 -m ${module_depth}
fi
./miner xserver_xf86_4_3_99_902 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: XORG-6_7_0
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b XORG-6_7_0 -d xserver_XORG_6_7_0 -o
	./shiatsu xserver_XORG_6_7_0 -m ${module_depth}
fi
./miner xserver_XORG_6_7_0 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: XORG-6_8_0
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b XORG-6_8_0 -d xserver_XORG_6_8_0 -o
	./shiatsu xserver_XORG_6_8_0 -m ${module_depth}
fi
./miner xserver_XORG_6_8_0 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: XORG-7_0
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b XORG-7_0 -d xserver_XORG_7_0 -o
	./shiatsu xserver_XORG_7_0 -m ${module_depth}
fi
./miner xserver_XORG_7_0 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: XORG-7_1
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b XORG-7_1 -d xserver_XORG_7_1 -o
	./shiatsu xserver_XORG_7_1 -m ${module_depth}
fi
./miner xserver_XORG_7_1 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: xorg-server-1.4
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b xorg-server-1.4 -d xserver_xorg_server_1_4 -o
	./shiatsu xserver_xorg_server_1_4 -m ${module_depth}
fi
./miner xserver_xorg_server_1_4 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# branch: origin/server-1.6-branch
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b origin/server-1.6-branch -d xserver_xorg_server_1_6_branch -o
	./shiatsu xserver_xorg_server_1_6_branch -m ${module_depth}
fi
./miner xserver_xorg_server_1_6_branch -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# tag: xorg-server-1.7.0
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b xorg-server-1.7.0 -d xserver_xorg_server_1_7_0 -o
	./shiatsu xserver_xorg_server_1_7_0 -m ${module_depth}
fi
./miner xserver_xorg_server_1_7_0 -s -${commits} -c ${commits} -mc ${maximum_commits} -o

# branch: master
if [ "$1" = "db" ]; then
	./gitup /home/thom/workspace/repositories/xserver -b master -d xserver_master -o
	./shiatsu xserver_master -m ${module_depth}
fi
./miner xserver_master -s -${commits} -c ${commits} -mc ${maximum_commits} -o
mysql -u root --password=root xserver_master < minings/xserver/fix-tag.sql
