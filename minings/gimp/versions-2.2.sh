#!/bin/bash

./commit-metrics gimp_2_2_branch -t tag -mic 2 -mc 50  $@ \
GIMP_2_1_0:GIMP_2_1_7 \
GIMP_2_1_7:GIMP_2_2_PRE1 \
GIMP_2_2_PRE1:GIMP_2_2_PRE2 \
GIMP_2_2_PRE2:GIMP_2_2_0 \
GIMP_2_2_0:GIMP_2_2_17
