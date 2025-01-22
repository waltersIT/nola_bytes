REPO_ROOT=$(git rev-parse --show-toplevel)
source "${REPO_ROOT}/scripts/settings.sh"
cd "${REPO_ROOT}"
docker network prune -f
docker network create --subnet=172.20.0.0/16 ${DOCKER_NET_NAME} || echo "Network ${DOCKER_NET_NAME} already exists because a container is using it"
# Stop and remove the Tomcat container
docker stop "$TCAT_CONTNR" 2> /dev/null
docker rm -f "$TCAT_CONTNR" 2> /dev/null
# Build and run the Tomcat container
docker build -t "${TCAT_IMAGE}" --no-cache -f "${TCAT_DOCKER_FILE}" .
docker run -d --name "${TCAT_CONTNR}" ${TCAT_PORT_MAP} "${TCAT_IMAGE}"
#mikey helped with this :)
# Check if the PostgreSQL container exists and manage it accordingly
psql_container_exists=$(docker ps -aq -f name="^nolapsql$")
psql_container_running=$(docker inspect -f '{{.State.Running}}' "nolapsql" 2>/dev/null)
if [ ! -z "$psql_container_exists" ]; then
    echo "PostgreSQL container 'nolapsql' exists."
    if [ "$psql_container_running" == "true" ]; then
        echo "The nolapsql container is already running."
    else
        echo "Starting the nolapsql container..."
        docker network connect --ip 172.20.128.2 ${DOCKER_NET_NAME} "nolapsql"
        docker start nolapsql || {
            echo "Failed to start nolapsql container. Checking logs..."
            docker logs "nolapsql"
            exit 1  # Exit script with an error status
        }
    fi
else
    echo "No existing nolapsql container found. Building and running a new one."
    docker build -t "${PSQL_IMAGE}" --no-cache -f "${PSQL_DOCKER_FILE}" .
    docker run -d --name "nolapsql" -e "${PSQL_PASSWORD}" -d ${PSQL_PORT_MAP} "${PSQL_IMAGE}" postgres
    docker exec -i "nolapsql" apt-get update && apt-get install net-tools iputils-ping -y
    docker exec -i "nolapsql" psql -U postgres -d postgres -f bytesDB.sql
    docker network connect --ip 172.20.128.2 ${DOCKER_NET_NAME} "nolapsql"
fi

# Connect containers to the network

docker network connect --ip 172.20.128.8 ${DOCKER_NET_NAME} "${TCAT_CONTNR}"
# start http://localhost:8080/bytes
# open http://localhost:8080/bytes

# Determine the operating system
OS=$(uname -s)

case "$OS" in
    Darwin)
        # Commands for macOS
        echo "Opening in browser (macOS)..."
        open "http://localhost:8080/bytes"
        ;;
    Linux)
        # Commands for Linux, if applicable
        echo "Opening in browser (Linux)..."
        xdg-open "http://localhost:8080/bytes"
        ;;
    CYGWIN*|MINGW32*|MSYS*|MINGW*)
        # Commands for Windows, assuming Git Bash is used
        echo "Opening in browser (Windows)..."
        cmd /c start "http://localhost:8080/bytes"
        ;;
    *)
        echo "OS not supported for automatic opening. Please manually open http://localhost:8080/bytes"
        ;;
esac