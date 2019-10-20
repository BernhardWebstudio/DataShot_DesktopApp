#!/bin/sh

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
# increment major version
mvn build-helper:parse-version versions:set \
      -DnewVersion =\${parsedVersion.nextMajorVersion}.0.0-SNAPSHOT \
      versions:commit
# publish
./bin/publish-current.sh;
