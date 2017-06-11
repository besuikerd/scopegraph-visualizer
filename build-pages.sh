#!/bin/bash
rm -r assets/
yarn build
mkdir -p assets
find target -maxdepth 1 -regex ".*\.\(js\|svg\|woff\|woff2\|ttf\|eot\)" | xargs -i cp {} assets
