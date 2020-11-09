#!/usr/bin/env bash

if [ "$#" -ne 2 ]; then
  echo "$0 <version> <output-path>"
  exit 0
fi
{
  cd $(dirname "$0") || exit 0
  mvn clean compile
  mvn exec:java -Dmcver="$1" -DoutDir="$2"
}
#java -Dmcver="$1" -DoutDir="$2" -cp target/mc-localization-1.0-SNAPSHOT.jar io.papermc.assets.downloader.LocalizationDownloader
