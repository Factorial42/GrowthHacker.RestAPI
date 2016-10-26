#!/bin/bash
host="localhost"

if [ ! -z "$1" ]; then
  host=$1
fi

filename=""
for filepath in $PROJECT_HOME/config/index-templates/*
do
  filename=${filepath##*/}
  echo -e "creating index template \""$filename"\" using the file @$filepath"
  curl --fail -XPUT http://$host:9200/_template/$filename -d @$filepath || exit 1
  echo -e "\nDone.\n"
done
