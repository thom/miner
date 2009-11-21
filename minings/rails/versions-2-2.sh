#!/bin/bash

./commit-metrics rails_2_2_branch -t tag -mic 2 -mc 50 $@ \
v2.1.0:v2.2.0 \
v2.2.0:v2.2.1 \
v2.2.1:v2.2.2
