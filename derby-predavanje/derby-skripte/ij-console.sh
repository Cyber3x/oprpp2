#! /bin/bash

# Ekstrahiraj direktorij u kojem se nalazi ova skripta:
PRG="$0"
PRGDIR=`dirname "$PRG"`

# Iz tog istog direktorija pozovi postavljanje varijabli okruženja za podatke o derby instalaciji
. "$PRGDIR"/setenv.sh

java -Dfile.encoding=UTF-8 -jar $DERBY_INSTALL/lib/derbyrun.jar ij

