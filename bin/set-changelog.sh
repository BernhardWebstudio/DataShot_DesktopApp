#!/bin/sh
# replace a placeholder for next v content with next v content

# setup variables
SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
MVN_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)

# required script. Additionally: sed
if [ -x "$(changelog-maker -h)" ]; then
  echo "Error: changelog-maker is not installed." >&2
  echo "Install by 'npm i changelog-maker -g'" >&2
  exit 1;
fi
# construct the changelog since the last change
CHANGES=$( changelog-maker --commit-url=https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/{ref} )
CHANGES="##$MVN_VERSION\n$CHANGES"
# switch to directory with changelog file
cd "$SCRIPT_DIR/../" || exit;
EXISTING_CHANGES=$(cat CHANGELOG.md)
EXISTING_CHANGES=$(echo "$EXISTING_CHANGES" | sed "s/# Changelog\n//g" )
printf "# Changelog\n\n%s\n\n%s" "$CHANGES" "$EXISTING_CHANGES" > CHANGELOG.md;
