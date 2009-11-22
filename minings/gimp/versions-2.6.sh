#!/bin/bash

./commit-metrics gimp_2_6_branch -t tag -mic 2 -mc 50  $@ \
GIMP_2_5_0:GIMP_2_6_0 \
GIMP_2_6_0:GIMP_2_6_7
