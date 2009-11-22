#!/bin/bash

./commit-metrics gimp_2_0_branch -t tag -mic 2 -mc 50  $@ \
GIMP_1_3_0:GIMP_2_0_RC1 \
GIMP_2_0_RC1:GIMP_2_0_6
