#!/bin/sh

DIR="files/localisation/english"  # Change this to your directory

for file in "$DIR"/*.yml; do
    {  
        IFS=  
        read -r first_line  
        echo "$first_line"  
        while read -r line; do  
            case "$line" in  
                [![:space:]]*) echo " $line" ;;  # Add a space if the line starts without one  
                *) echo "$line" ;;  # Otherwise, print as is  
            esac  
        done  
    } < "$file" > "$file.tmp" && mv "$file.tmp" "$file"
done

echo "Indentation fixed in all YAML files."
