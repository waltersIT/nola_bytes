#!/bin/bash
# Author: Omar Essa
# Date Created: 3/16/2024
# Editors: Omar Essa
# Date Last Edited: 3/17/2024
# Purpose: shell file to build the servlet files and add them to the war folder

REPO_ROOT=$(git rev-parse --show-toplevel)



# Set current directory repo root
pushd "${REPO_ROOT}"

source "${REPO_ROOT}/scripts/settings.sh"

pwd

# compile every servlet Java source file and put all the classes into the `build` folder


cp -vR build/NolaBytes build/bytes/WEB-INF/classes

pwd 
cp -vR lib/* build/bytes/WEB-INF/lib

cp -v pages/* build/bytes

cp src/NolaBytes/config.properties build/bytes/WEB-INF

cp -v pages/images/* build/bytes/images

cd "${REPO_ROOT}/build/bytes"

pwd

jar cvf ../"${WARNAME}.war" *   # jar everything there (asterisk) into WAR in parent folder


find "${WARMODEL}" build -name "*.java" -exec rm -f '{}' \;

cd "${REPO_ROOT}"

