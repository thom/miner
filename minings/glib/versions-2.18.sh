#!/bin/bash

./commit-metrics glib_2_18_branch -t tag -mic 2 -mc 50 $@ \
GLIB_2_17_0:GLIB_2_18_0 \
GLIB_2_18_0:GLIB_2_18_4
