#!/bin/bash

./commit-metrics xserver_master -c -t tag -mic 2 -mc 60 \
xf-4_0_1b:xf86-4_3_0_1 \
xf86-4_3_0_1:xf86-4_3_99_902 \
xf86-4_3_99_902:xf86-4_4_99_1 \
xf86-4_4_99_1:XORG-6_7_99_1 \
XORG-6_7_99_1:XORG_6_8_0 \
XORG_6_8_0:XORG-7_0 \
