#!/bin/bash
DIR=../../../../www/demos/hoesterholt
DEMO1DIR=jxmlnote
DEMO2DIR=splittable
DEMO1=JXMLNoteDemo.jar
DEMO2=JXSplitTableDemo.jar
D1=$DIR/$DEMO1DIR/$DEMO1
D2=$DIR/$DEMO2DIR/$DEMO2
echo "signing $D1..."
./sign.sh swinglabs $D1 $D1.1
echo "moving $D1..."
mv $DIR/$DEMO1DIR/$DEMO1.1 $DIR/$DEMO1DIR/$DEMO1
echo "signing $D2..."
./sign.sh swinglabs $DIR/$DEMO1DIR/$DEMO1 $DIR/$DEMO1DIR/$DEMO1.1
echo "moving $D2..."
mv $DIR/$DEMO1DIR/$DEMO1.1 $DIR/$DEMO1DIR/$DEMO1
