#!/bin/bash
# Author: Omar Essa
# Date Created: 3/16/2024
# Editors: Omar Essa
# Date Last edited: 3/17/2024
# Purpose: shell file to build backend files

# make sure you are in the root directory
REPO_ROOT=$(git rev-parse --show-toplevel)
source "${REPO_ROOT}/scripts/settings.sh"

pushd "${REPO_ROOT}"
# `pushd` changes directory after putting the current directory on the "directory stack"

# compile every backend Java file and put all the classes into the `build` folder
javac $(find src/ -name "*.java") -d build -classpath "${JAVAC_CLASSPATH}"

popd

echo "end of build_backend.sh"
pwd
