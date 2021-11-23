#!/bin/bash

VERSION=0.0.1

docker login

# docker build -f ObserverBuilder.dockerfile . -t observertc/webrtc-observer:$VERSION
./gradlew dockerBuild
docker push gavin0127/observer:$VERSION
