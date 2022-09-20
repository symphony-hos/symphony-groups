#!/bin/bash
set -euxo pipefail

pushd ../symphony-common
  bash ./build.sh
popd

mvn clean package -DskipTests
