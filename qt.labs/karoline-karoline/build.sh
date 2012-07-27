#!/bin/sh
make clean;
rm Makefile;
qmake -makefile -nocache -o Makefile karoline.pro;
make;
