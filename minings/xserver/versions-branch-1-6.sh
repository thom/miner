#!/bin/sh

./commit-metrics xserver_xorg_server_1_6_branch -c -t tag -mic 2 -mc 60 \
xorg-server-1.2.99.0:xorg-server-1.3.99.0 \
xorg-server-1.3.99.0:xorg-server-1.5.99.3 \
xorg-server-1.5.99.3:xorg-server-1.5.99.901 \
xorg-server-1.5.99.901:xorg-server-1.6.0 \
xorg-server-1.6.0:xorg-server-1.6.2 \
