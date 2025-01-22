#!/bin/bash
# Author: Omar Essa
# Date Created: 3/16/2024
# Editors: Omar Essa
# Date Last edited: 3/17/2024
# Purpose: shell file to run other shell files


REPO_ROOT=$(git rev-parse --show-toplevel) #sets top level of repo

# call the other shell scripts
cd "${REPO_ROOT}"
source "${REPO_ROOT}/scripts/settings.sh"

echo "Clearing the build directory..."
rm -rf "${BUILD_DIR}/*" || { echo "Failed to clear build directory"; exit 1; }

# Call other build scripts as separate processes
# bash "${REPO_ROOT}/scripts/make_war_model.sh"
# bash "${REPO_ROOT}/scripts/build_backend.sh"
# bash "${REPO_ROOT}/scripts/build_servlet.sh"
# bash "${REPO_ROOT}/scripts/docker.sh"

echo "Building WAR model..."
bash "${REPO_ROOT}/scripts/make_war_model.sh" || { echo "Failed to build WAR model"; exit 1; }

echo "Building backend..."
bash "${REPO_ROOT}/scripts/build_backend.sh" || { echo "Failed to build backend"; exit 1; }

echo "Building servlet..."
bash "${REPO_ROOT}/scripts/build_servlet.sh" || { echo "Failed to build servlet"; exit 1; }

echo "Running Docker processes..."
bash "${REPO_ROOT}/scripts/docker.sh" || { echo "Failed to execute Docker processes"; exit 1; }