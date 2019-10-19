#!/bin/sh

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
# jump version
mvn release:prepare -B release:perform
# publish
cd "$SCRIPT_DIR" || exit;
./bin/publish.sh;
