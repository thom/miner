#!/bin/bash

./commit-metrics xserver_xorg_server_1_6_branch -c -t tag -mic 2 -mc 50 \
xorg-server-1.5.99.1:xorg-server-1.5.99.901 \
xorg-server-1.6.0:xorg-server-1.6.5
