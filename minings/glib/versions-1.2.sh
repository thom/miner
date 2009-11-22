#!/bin/bash

./commit-metrics glib_1_2_branch -t tag -mic 2 -mc 50 $@ \
GLIB_1_1_0:GLIB_1_2_0 \
GLIB_1_2_0:GLIB_1_2_10
