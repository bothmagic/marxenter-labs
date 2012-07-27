#!/bin/bash

if [ $# != 1 ]; then
  echo "mkkeystore <password>"
  exit 1
fi

rm -f keystore.jks
PASS=$1

keytool -genkey -alias hod -keyalg RSA -keypass $PASS -keystore keystore.jks -storepass $PASS -validity 36500 -keysize 4096
keytool -selfcert -alias hod -keystore keystore.jks -keypass $PASS  -storepass $PASS -validity 36500 -v
keytool -export -alias hod -file hod.cert -keystore keystore.jks -storepass $PASS -v

./list.sh $PASS


