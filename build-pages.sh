#!/bin/bash

BUILD_DIR=assets/build

if [ -d "$BUILD_DIR" ]; then
  rm -r $BUILD_DIR
fi

if hash yarn 2>/dev/null; then
  JS_BUILD_CMD=yarn
elif hash npm 2>/dev/null; then
  JS_BUILD_CMD=npm
else
  echo "yarn or npm is required to build"
  exit 1
fi

if ! hash sbt 2>/dev/null; then
  echo "sbt is required to build"
  exit 1
fi

sbt clean fullOptJS
$JS_BUILD_CMD build
mkdir -p $BUILD_DIR
find target -maxdepth 1 -regex ".*\.\(js\|svg\|woff\|woff2\|ttf\|eot\)" | xargs -i cp {} $BUILD_DIR
