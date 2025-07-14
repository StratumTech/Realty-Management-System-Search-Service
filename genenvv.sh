#!/bin/bash

generate_random_string() {
    local length=$1
    tr -dc 'a-zA-Z0-9' </dev/urandom | head -c "$length"
}

ELASTIC_HOST=localhost
ELASTIC_PORT=9200
ES_JAVA_OPTS="-Xms512m -Xmx512m"
ELASTIC_PASSWORD=$(generate_random_string 20)
KAFKA_BOOTSTRAP_SERVERS=localhost:29092

cat <<EOF > .env
ELASTIC_HOST=${ELASTIC_HOST}
ELASTIC_PORT=${ELASTIC_PORT}
ES_JAVA_OPTS=${ES_JAVA_OPTS}
ELASTIC_PASSWORD=${ELASTIC_PASSWORD}
KAFKA_BOOTSTRAP_SERVERS=${KAFKA_BOOTSTRAP_SERVERS}
EOF

echo "The .env file has been created successfully"