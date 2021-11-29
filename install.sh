#!/bin/bash

VERSION=0.8.2

# docker build -f ObserverBuilder.dockerfile . -t observertc/webrtc-observer:$VERSION
./gradlew dockerBuild
docker push gavin0127/observer:$VERSION
