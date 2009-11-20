#!/bin/bash

./commit-metrics xserver_xorg_server_1_5_branch -t tag -mic 2 -mc 60 $@ \
xorg-server-1.4.99.901:xorg-server-1.5.0 \
xorg-server-1.5.0:xorg-server-1.5.3
