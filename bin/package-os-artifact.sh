#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 2 ]]; then
  echo "Usage: $0 <platform-suffix> <version>" >&2
  exit 1
fi

platform="$1"
version="$2"

jar_path="$(ls target/DataShot*-jar-with-dependencies.jar | head -n 1)"
if [[ -z "$jar_path" ]]; then
  echo "No jar-with-dependencies artifact found under target/" >&2
  exit 1
fi

bundle_dir="dist/DataShot-${version}-${platform}"
mkdir -p "$bundle_dir"
cp "$jar_path" "$bundle_dir/"

if [[ -f "src/main/resources/qrscan_chromium.html" ]]; then
  cp "src/main/resources/qrscan_chromium.html" "$bundle_dir/"
fi
if [[ -f "src/main/resources/qrscan_using_python.py" ]]; then
  cp "src/main/resources/qrscan_using_python.py" "$bundle_dir/"
fi

helper_os=""
case "$platform" in
  macos-*) helper_os="macos" ;;
  windows-*) helper_os="windows" ;;
  linux-*) helper_os="linux" ;;
esac

# Optional helper binaries if present in repository layout.
if [[ -n "$helper_os" ]]; then
  for helper in \
    "native/bin/qr-native-${helper_os}" \
    "native/bin/qr-native-${helper_os}.exe" \
    "native/bin/qr-native-${platform}" \
    "native/bin/qr-native-${platform}.exe"; do
    if [[ -f "$helper" ]]; then
      cp "$helper" "$bundle_dir/"
    fi
  done
fi

printf '%s\n' "$bundle_dir"
