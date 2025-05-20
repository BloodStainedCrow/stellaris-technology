#!/usr/bin/env bash

if [ -z "$1" ]; then
  echo "Usage: $0 <directory>"
  exit 1
fi

TARGET_DIR="$1"

find "$TARGET_DIR" -type f | while read -r file; do
  # Read raw bytes (disable globbing and preserve special chars)
  content=$(LC_ALL=C cat "$file")

  # Strip UTF-8 BOM if present
  bom_removed="${content#$'\xEF\xBB\xBF'}"

  # Check if remaining content is only whitespace
  if [[ -z "$bom_removed" || "$bom_removed" =~ ^[[:space:]]*$ ]]; then
    echo "Deleting: $file"
    rm "$file"
  fi
done
