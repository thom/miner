#!/bin/bash

./commit-metrics glib_2_20_branch -t tag -mic 2 -mc 50 $@ \
GLIB_2_19_0:GLIB_2_20_0 \
GLIB_2_20_0:2.20.5
