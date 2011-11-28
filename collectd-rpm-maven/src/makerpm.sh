#!/bin/sh

test_system() {
   perl "-MExtUtils::Command::MM" .
   res=$?
   [ $res != 0 ] && echo "Instale perl-ExtUtils-MakeMaker" && return 1
   echo "Encontrado perl Command:MM"
   type rpmbuild > /dev/null
   res=$?
   [ $res != 0 ] && return 1
   echo "Encontrado rpmbuild"
   gcc --version > /dev/null
   res=$?
   [ $res != 0 ] && return 1
   echo "Encontrado gcc"
   echo "..... podemos continuar"
   return 0
}
build_collectd() {
   [ ! -e target/SOURCES ] && mkdir target/SOURCES
   cd target/SOURCES && wget http://www.collectd.org/files/collectd-5.0.1.tar.gz && tar zxvf collectd-5.0.1.tar.gz
   res=$?
   [ $res != 0 ] ; return 1
   echo $PWD
   #cd specs && rpmbuild -ba --define "_topdir $(pwd)/.." collectd.spec 
}

test_system
res=$?
echo $res es el resultado de la operacion
[ $res != 0 ] && echo "El sistema no esta preparado" && exit 0
build_collectd
[ $res != 0 ] && echo "Error contruyendo collectd" && exit 0
echo "----------- id ------------"
id
echo "-------- SET --------------"
#set
exit 0
