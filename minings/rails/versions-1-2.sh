#!/bin/bash

./commit-metrics rails_1_2_branch -t tag -mic 2 -mc 50 $@ \
v1.1.1:v1.2.0_RC1 \
v1.2.0_RC1:v1.2.0_RC2 \
v1.2.0_RC2:v1.2.1 \
v1.2.1:v1.2.2 \
v1.2.2:v1.2.3 \
v1.2.3:v1.2.5 \
v1.2.5:v1.2.6
