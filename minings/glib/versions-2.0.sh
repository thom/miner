#!/bin/bash

./commit-metrics glib_2_0_branch -t tag -mic 2 -mc 50 $@ \
GLIB_1_3_0:GLIB_2_0_0 \
GLIB_2_0_0:GLIB_2_0_7
