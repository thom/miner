#!/bin/bash

./commit-metrics gimp_2_4_branch -t tag -mic 2 -mc 50  $@ \
GIMP_2_3_0:GIMP_2_4_0_RC1 \
GIMP_2_4_0_RC1:GIMP_2_4_0_RC3 \
GIMP_2_4_0_RC3:GIMP_2_4_7
