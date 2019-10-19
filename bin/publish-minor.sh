#!/bin/sh

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
# increment minor version
mvn build-helper:parse-version versions:set \
   release:prepare \
      -DdevelopmentVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}.0-SNAPSHOT \
   release:perform
# publish
./bin/publish.sh;
