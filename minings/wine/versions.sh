#!/bin/bash

./commit-metrics -mic 2 -mc 50  $@ \
wine_0_9_to_0_9_20 \
wine_0_9_20_to_0_9_40 \
wine_0_9_40_to_0_9_61 \
wine_0_9_61_to_1_0 \
wine_1_0_to_1_1_0 \
wine_1_1_0_to_1_1_20 \
wine_1_1_20_to_1_1_33
