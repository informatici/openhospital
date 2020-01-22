#!/bin/bash

curr_version=$(git tag --sort -version:refname | head -n 1 | tr '.' '_' | tr -d 'v')
versions_range=$(printf "%s...%s" $(git tag --sort -version:refname | head -n 2 | tac))

git log --date=short --format=medium --no-merges $versions_range > doc/changelog_$curr_version.txt
