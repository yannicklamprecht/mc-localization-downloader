#!/usr/bin/env bash

if [ "$#" -ne 2 ]; then
    echo "$0 <version> <output-path>"
    exit 0
fi

# mvn exec:java -Dmcver="$1" -DoutDir="$2" -Dexec.mainClass="LocalizationDownloader"
java -Dmcver="$1" -DoutDir="$2" -cp target/mc-localization-1.0-SNAPSHOT.jar LocalizationDownloader