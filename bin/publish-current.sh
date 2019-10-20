#!/bin/sh
MVN_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd "$SCRIPT_DIR/.." || exit;
./bin/set-changelog.sh;
git commit -am "Update changelog & publish Version $MVN_VERSION"

git tag -a "v$MVN_VERSION" -m "Version $MVN_VERSION" #\n\nChanges:\n$GIT_HISTORY"
git push --follow-tags
