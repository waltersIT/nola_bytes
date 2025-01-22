#!/bin/bash
# Author: Omar Essa
# Date Created: 3/16/2024
# Editors: Omar Essa
# Date Last edited: 3/20/2024
# Purpose: shell file with environment variables

REPO_ROOT=$(git rev-parse --show-toplevel)
# the name of the war file
: ${WARNAME:="bytes"}

# the name of the war folder
: ${WARFOLDER:="$(find ${WARNAME})"}

# TCAT_CONTNR is the name we want for the container we get from `docker run`
: ${TCAT_CONTNR:="nolabytes"}

# TCAT_IMAGE is the name we want to give the Docker image created when we `docker build`
: ${TCAT_IMAGE:="${TCAT_CONTNR}-image"}

# the port we want to use for the docker container
: ${TCAT_PORT_MAP:="-p 8080:8080"}

# the path of the Dockerfile which will be used to copy the war zip file into the tomcat container
: ${TCAT_DOCKER_FILE:="${REPO_ROOT}/src/NolaBytes/front_end/Dockerfile"}

: ${PSQL_CONTAINER:="nolapsql"}

: ${PSQL_IMAGE:="${PSQL_CONTAINER}-image"}

: ${PSQL_PORT_MAP:="-p 5432:5432"}

: ${PSQL_DOCKER_FILE:="${REPO_ROOT}/src/NolaBytes/back_end/Dockerfile"}

CONFIG_FILE="${REPO_ROOT}/src/NolaBytes/config.properties"
: ${PSQL_PASSWORD:="POSTGRES_PASSWORD=$(grep "DB_PASSWORD" "${CONFIG_FILE}" | cut -d'=' -f2)"}

: ${PG_DUMP:="nolabased.sql"}

: ${PG_DUMP_DIR:= "${REPO_ROOT}/scripts/${PG_DUMP}"}

: ${MSYS_NO_PATHCONV:=1}

: ${REPO_ROOT:=$(git rev-parse --show-toplevel)}

: ${BUILD_DIR:="${REPO_ROOT}/build"}
if [ ! -d "${BUILD_DIR}" ]; then
    mkdir -p "${BUILD_DIR}"
fi

: ${WARMODEL:="${BULD_DIR}/bytes"}

: ${WARMODEL_IMAGES:= "${WARMODEL}/images"}

: ${WARMODEL_WEB_INF:= "${WARMODEL}/WEB-INF"}

: ${WARMODEL_CLASSES:= "${WARMODEL_WEB_INF}/classes"}

: ${WARMODEL_LIB:= "${WARMODEL_WEB_INF}/lib"}

: ${SRC_PAGES:= "${REPO_ROOT}/pages"}

: ${SRC_IMAGES:= "${SRC_PAGES}/images"}

: ${DOCKER_NET_NAME:= "nolanet"}

# build path depends on operating system
# uses semicolon on windows and colon on mac
if uname | grep -iq MINGW # it's windows
then 
  JAVAC_CLASSPATH="build;lib/*"
else # MacOS and Linux
  JAVAC_CLASSPATH="build:lib/*"
fi

export JAVAC_CLASSPATH 
export WARNAME 
export WARFOLDER
export MSYS_NO_PATHCONV
export TCAT_CONTNR
export TCAT_IMAGE 
export TCAT_PORT_MAP
export TCAT_DOCKER_FILE
export BUILD_DIR
export WARMODEL
export WARMODEL_IMAGES
export WARMODEL_WEB_INF
export WARMODEL_CLASSES
export WARMODEL_LIB
export SRC_PAGES
export SRC_IMAGES
export REPO_ROOT
export PSQL_CONTAINER
export PSQL_IMAGE
export PSQL_PORT_MAP
export PSQL_DOCKER_FILE
export PSQL_PASSWORD
export PG_DUMP
export PG_DUMP_DIR
export DOCKER_NET_NAME
export PSQL_PASSWORD


echo "end of settings.sh"
pwd