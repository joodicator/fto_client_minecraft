#!/bin/bash
cd `dirname $0` && \
rm -rf newbin/* && \
javac -cp newsrc -d newbin newsrc/mineshafter/proxy/MineProxy.java && \
rm -rf dist && \
cp -R olddist dist && \
jar uf dist/fto_client.jar -C newbin mineshafter
