#!/bin/sh

DIR="files/localisation/english"  # Change this to your directory


for file in "$DIR"/*.yml; do
    awk -F':' '!($2 ~ /\\n/)' "$file" | expand > "$file.tmp" && mv "$file.tmp" "$file"
done

echo "Removed all lines where values contain \\n."