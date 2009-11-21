#!/bin/bash

./commit-metrics rails_2_1_branch -t tag -mic 2 -mc 50 $@ \
v2.0.1:v2.1.0_RC1 \
v2.1.0_RC1:v2.1.0 \
v2.1.0:v2.1.1 \
v2.1.1:v2.1.2
