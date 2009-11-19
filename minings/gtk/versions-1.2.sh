#!/bin/bash

./commit-metrics gtk_1_2_branch -c -t tag -mic 2 -mc 50 \
GTK_0_99_0:GTK_1_0_0 \
GTK_1_0_0:GTK_1_1_0 \
GTK_1_1_0:GTK_1_2_0 \
GTK_1_2_0:GTK_1_2_10
