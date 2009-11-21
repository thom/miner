#!/bin/bash

./commit-metrics rails_2_0_branch -t tag -mic 2 -mc 50 $@ \
v2.0.0_PR:v2.0.0_RC1 \
v2.0.0_RC1:v2.0.0_RC2 \
v2.0.0_RC2:v2.0.1 \
v2.0.1:v2.0.3 \
v2.0.3:v2.0.4 \
v2.0.4:v2.0.5
