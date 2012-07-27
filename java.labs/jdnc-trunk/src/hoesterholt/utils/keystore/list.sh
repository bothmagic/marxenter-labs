#!/bin/bash

if [ $# != 1 ]; then
  echo "list.sh <password>"
  exit 1
fi

PASS=$1

keytool -list -alias hod -keystore keystore.jks -storepass $PASS 
echo ""
keytool -printcert -file hod.cert 

