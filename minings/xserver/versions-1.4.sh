#!/bin/bash

./commit-metrics xserver_xorg_server_1_4_branch -t tag -mic 2 -mc 50 $@ \
xorg-server-1.3.99.0:xorg-server-1.4 \
xorg-server-1.4:xorg-server-1.4.2
