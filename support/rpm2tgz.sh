#!/bin/sh
cd $(dirname $0)
install -d files
rm -rf tmp
for name 
do
  DESTFILE=files/$(basename $name .rpm).tgz
  mkdir tmp 
  rpm2cpio $name | (cd tmp ; cpio -i -d )
  tar cvf $DESTFILE -C tmp .
  rm -rf tmp
done
