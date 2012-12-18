#!/bin/bash
javac -cp newsrc -d newbin newsrc/mineshafter/proxy/MineProxy.java
jar uf dist/fto_client.jar -C newbin mineshafter
