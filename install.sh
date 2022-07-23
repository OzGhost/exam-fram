#!/bin/bash
secret="$1"
if [ -z "$secret" ]
then
    echo "__[xx] Try again with: install.sh <passwd>"
    exit 1
fi
docker rm -f exam-fram-app &> /dev/null
docker rm -f mun-db &> /dev/null
docker network rm mun-net &> /dev/null
docker rmi exam-fram:0.0.1 &> /dev/null
rm -rf build
./gradlew build
if [ "$?" != "0" ]
then
    echo "__[xx] die while build the source code."
    exit 1
fi
docker build -t exam-fram:0.0.1 .
if [ "$?" != "0" ]
then
    echo "__[xx] die while build the docker image."
    exit 1
fi
docker network create mun-net
docker container create --name mun-db \
    -e POSTGRES_USER=mundo \
    -e POSTGRES_PASSWORD=$secret \
    --net mun-net \
    --net-alias mun-db \
    postgres:14.2-alpine

docker container create --name exam-fram-app \
    -e CFG_SECRET=$secret \
    -e QUARKUS_DATASOURCE_PASSWORD=$secret \
    --net mun-net \
    --net-alias exam-fram-app \
    -p 1912:1912 \
    exam-fram:0.0.1

echo "__[o0] Finished. Now you can start the container mun-db and exam-fram-app to have the application running."

