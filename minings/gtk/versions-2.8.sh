#!/bin/bash

./commit-metrics gtk_2_8_branch -t tag -mic 2 -mc 60 $@ \
GTK_2_7_0:GTK_2_8_0 \
GTK_2_8_0:GTK_2_8_20
