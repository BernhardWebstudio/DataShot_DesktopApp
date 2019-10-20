#!/bin/sh

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
# jump version
mvn build-helper:parse-version versions:set \
      -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}-SNAPSHOT \
      versions:commit
# publish
cd "$SCRIPT_DIR" || exit;
./bin/publish-current.sh;
