#!/bin/bash

./commit-metrics gtk_2_0_branch -t tag -mic 2 -mc 50 $@ \
GTK_1_3_1:GTK_2_0_0 \
GTK_2_0_0:GTK_2_0_9
