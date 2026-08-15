#!/usr/bin/env bash

set -euo pipefail

CHANGELOG_FILE=""
VERSION=""

# Handle positional arguments flexibly:
# Supports `extract-changelog.sh [file] [version]`, `extract-changelog.sh [version] [file]`,
# or `extract-changelog.sh [version]` (defaults file to CHANGELOG.md).
if [ $# -ge 2 ]; then
  if [ -f "$1" ]; then
    CHANGELOG_FILE="$1"
    VERSION="$2"
  elif [ -f "$2" ]; then
    CHANGELOG_FILE="$2"
    VERSION="$1"
  else
    CHANGELOG_FILE="$1"
    VERSION="$2"
  fi
elif [ $# -eq 1 ]; then
  if [ -f "$1" ]; then
    CHANGELOG_FILE="$1"
    VERSION=""
  else
    CHANGELOG_FILE="CHANGELOG.md"
    VERSION="$1"
  fi
else
  CHANGELOG_FILE="CHANGELOG.md"
  VERSION=""
fi

if [ ! -f "$CHANGELOG_FILE" ]; then
  echo "Error: Changelog file '$CHANGELOG_FILE' not found." >&2
  exit 1
fi

awk -v ver="$VERSION" '
BEGIN {
    sub(/^refs\/tags\//, "", ver);
    clean_ver = ver;
    sub(/^v/, "", clean_ver);
    gsub(/\./, "\\.", clean_ver);
    gsub(/\+/, "\\+", clean_ver);
    if (clean_ver != "") {
        pattern = "^##[[:space:]]+(\\[)?(Version[[:space:]]+)?v?" clean_ver "(\\])?([[:space:]\\(-]|:|$|$)";
    } else {
        pattern = "^##[[:space:]]+";
    }
    found = 0;
    content = "";
}
/^## / {
    if (found) {
        exit;
    }
    if ($0 ~ pattern) {
        found = 1;
        next;
    }
}
found {
    if (content == "" && $0 ~ /^[[:space:]]*$/) {
        next;
    }
    content = (content == "" ? $0 : content "\n" $0);
}
END {
    sub(/[[:space:]]+$/, "", content);
    if (content != "") {
        print content;
    }
}
' "$CHANGELOG_FILE"
