#!/bin/bash

./commit-metrics wine_master -t tag -mic 2 -mc 50  $@ \
wine-0.9:wine-0.9.61 \
wine-0.9.61:wine-1.0-rc1 \
wine-1.0-rc1:wine-1.0-rc2 \
wine-1.0-rc2:wine-1.0-rc3 \
wine-1.0-rc3:wine-1.0-rc4 \
wine-1.0-rc4:wine-1.0-rc5 \
wine-1.0-rc5:wine-1.0 \
wine-1.0:wine-1.1.0 \
wine-1.1.0:wine-1.1.33
