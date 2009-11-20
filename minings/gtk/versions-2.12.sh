#!/bin/bash

./commit-metrics gtk_2_12_branch -t tag -mic 2 -mc 60 $@ \
GTK_2_11_0:GTK_2_12_0 \
GTK_2_12_0:GTK_2_12_12
