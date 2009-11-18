#!/bin/bash

./commit-metrics gtk -c -t tag -mic 2 -mc 60 \
GTK_2_15_0:GTK_2_15_1 \
GTK_2_15_1:GTK_2_16_0 \
GTK_2_16_0:2.16.1 \
2.16.1:2.17.0
