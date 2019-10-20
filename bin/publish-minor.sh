#!/bin/sh

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
# increment minor version
mvn build-helper:parse-version versions:set \
      -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}.0-SNAPSHOT \
      versions:commit
# publish
./bin/publish-current.sh;
