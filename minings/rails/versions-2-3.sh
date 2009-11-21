#!/bin/bash

./commit-metrics rails_2_3_branch -t tag -mic 2 -mc 50 $@ \
v2.2.1:v2.3.0 \
v2.3.0:v2.3.1 \
v2.3.1:v2.3.2.1 \
v2.3.2.1:v2.3.3.1 \
v2.3.3.1:v2.3.4
