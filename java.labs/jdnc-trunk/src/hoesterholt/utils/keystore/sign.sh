#!/bin/bash

PASS=$1
IN=$2
OUT=$3

if [ $# != 3 ]; then
  echo "sign.sh <password> <jar-in> <jar-out>"
  exit 1
fi

 
jarsigner -keystore keystore.jks -signedjar $OUT -storepass $PASS -keypass $PASS $IN hod
jarsigner -verify $3

 
