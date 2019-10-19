#!/bin/sh

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
# increment major version
mvn build-helper:parse-version versions:set \
   release:prepare \
      -DdevelopmentVersion =\${parsedVersion.nextMajorVersion}.0.0-SNAPSHOT \
   release:perform
# publish
./bin/publish.sh;
