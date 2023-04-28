#!/bin/bash

: "${MIN_MEMORY:=256M}}"
: "${MAX_MEMORY:=4G}}"

echo "[init] Copying Hyreos jar"
cp /usr/app/Hyreos.jar /hyreos

echo "[init] Starting process..."
exec java -Xms${MIN_MEMORY} -Xmx${MAX_MEMORY} -jar Hyreos.jar