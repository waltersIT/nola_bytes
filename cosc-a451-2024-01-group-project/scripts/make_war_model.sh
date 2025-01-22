#!/bin/bash
# Author: Matthew Levin
# Date Created: 4/10/2024
# Editors: Matthew Levin
# Date Last edited: 4/10/2024
# Purpose: Build Warmodel

REPO_ROOT=$(git rev-parse --show-toplevel)
source "${REPO_ROOT}/scripts/settings.sh"

cd "${REPO_ROOT}/build"


mkdir "bytes"
mkdir "bytes/images"
mkdir "bytes/WEB-INF"
mkdir "bytes/WEB-INF/classes"
mkdir "bytes/WEB-INF/lib"

echo "end of build_backend.sh"
pwd
