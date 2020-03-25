#!/bin/sh

# remove snapshot. Requires maven versions >= 2.10
mvn versions:set -DremoveSnapshot

# fetch new version
MVN_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
./bin/set-changelog.sh;
git commit -am "Update changelog to publish Version v$MVN_VERSION"

git tag -a "v$MVN_VERSION" -m "Version $MVN_VERSION" #\n\nChanges:\n$GIT_HISTORY"
git push --follow-tags
