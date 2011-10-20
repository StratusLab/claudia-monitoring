%define _unpackaged_files_terminate_build 0
Name:		collectd
Version:	5.0.1
Release:	1%{?dist}
Summary:        collectd monitoring solution

Group:		daemons
License:	GPL v2.0+
URL:		www.collectd.org
Source0:	http://www.collectd.org/files/collectd-5.0.1.tar.gz
Patch0:         confdir.patch
BuildRoot:	%(mktemp -ud %{_tmppath}/%{name}-%{version}-%{release}-XXXXXX)
BuildRequires:	perl-ExtUtils-MakeMaker, python-devel
#Requires:	

%description

%package common
Summary:  collectd monitoring solution. Common files
%package basicprobes
Summary:  collectd monitoring solution. Basic probes
Requires: collectd-common
%package client
Summary:  collectd monitoring solution. Client package
Requires: basicprobes
%package server
Summary:  collectd monitoring solution. Server package
%package java
Summary: collectd monitoring solution. Java bindings package
Requires: collectd-common

%description common
This package contains a basic installation of collectd monitoring solution, common to server (collector) and client (probes) sides.

%description basicprobes 
This package contains a basic of probes that are part of collectd monitoring solution.

%description java
This package contains the binding to support collectd plugins in Java.

%description client
A basic installation of a collectd with the role of client running the probes and sending the results to another collectd with the server role.

%description server
A basic installation of a collectd runnnig as server that receives probes of other machines running collectd.

%prep
%setup 
cat > collectd.conf << EOF
# This is a very simplified collectd config file: please, add your own
# configuration in files under /opt/collectd/etc/conf.d
#
# See /opt/collectd/etc/collectd.conf.example as reference.
#
Interval 30
TypesDB "/opt/collectd/share/collectd/types.db"
Include "/opt/collectd/etc/conf.d"
EOF

cat > basicprobes.conf << EOF
LoadPlugin cpu
#LoadPlugin csv
LoadPlugin df
LoadPlugin disk
LoadPlugin interface
LoadPlugin load
LoadPlugin memory
LoadPlugin processes
#LoadPlugin unixsocks
LoadPlugin vmem
<Plugin csv>
        DataDir "/opt/collectd/var/lib/collectd/csv"
        StoreRates false
</Plugin>
<Plugin df>
#        Device "/dev/xvda1"
        ReportReserved false
</Plugin>
<Plugin disk>
        Disk "xvda"
#       IgnoreSelected false
</Plugin>
<Plugin interface>
#       Interface "eth0"
#       IgnoreSelected false
</Plugin>
<Plugin processes>
# Process "mysqld"
</Plugin>
<Plugin vmem>
        Verbose false
</Plugin>
<Plugin unixsock>
        SocketFile "/opt/collectd/var/run/collectd-unixsock"
        SocketGroup "www-data"
        SocketPerms "0660"
        DeleteSocket false
</Plugin>
EOF
cat > network-client.conf <<EOF
Loadplugin network
<Plugin network>
        <Server "collectorserver" "25827">
#                Interface "eth0"
        </Server>
</Plugin>
EOF
cat > network-server.conf <<EOF
Loadplugin network
<Plugin network>
        <Listen "0.0.0.0" "25826">
                Interface "eth0"
        </Listen>
</Plugin>
EOF
cat > collectdinitd <<EOF
#!/bin/bash
#
# httpd        Startup script for Collectd daemon
#
# chkconfig: - 30 20
# description: Collectd is a monitoring solution.
# processname: collectdmon

### BEGIN INIT INFO
# Provides:          collectd
# Required-Start:    \$local_fs \$remote_fs \$network \$syslog
# Required-Stop:     \$local_fs \$remote_fs \$network \$syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# X-Interactive:     true
# Short-Description: Start/stop collectd monitoring
### END INIT INFO



#
# collectd              This init.d script is used to start collectd.
#  To create entries, copy this file to /etc/init.d/ and then run
# chkconfig --level 2345 collectd on

# Source function library.
. /etc/rc.d/init.d/functions

PIDFILE=/opt/collectd/var/run/collectd.pid
COLLECTDMON=/opt/collectd/sbin/collectdmon
COLLECTD=/opt/collectd/sbin/collectd

case \$1 in
        start)
        daemon \$COLLECTDMON -c \$COLLECTD -P \$PIDFILE 
        ;;
        stop)
          killproc \$COLLECTDMON
        ;;
        restart)
          killproc \$COLLECTDMON
          daemon \$COLLECTDMON -c \$COLLECTD -P \$PIDFILE 
        ;;
        *)
          echo "Usage: /etc/init.d/collectd start|stop|restart"
                exit 1
        ;;
esac
EOF

%patch0

%build
test -n "$JAVA_HOME" || export JAVA_HOME=/usr/java/jdk1.6.0_27
./configure --with-python=/usr/bin/python2.6 --with-java=$JAVA_HOME
make %{?_smp_mflags}

%install
rm -rf $RPM_BUILD_ROOT
make install DESTDIR=$RPM_BUILD_ROOT
install -d $RPM_BUILD_ROOT/opt/collectd/etc/conf.d
install -d $RPM_BUILD_ROOT/etc/init.d
mv $RPM_BUILD_ROOT/opt/collectd/etc/collectd.conf $RPM_BUILD_ROOT/opt/collectd/etc/collectd.conf.sample
cp collectd.conf $RPM_BUILD_ROOT/opt/collectd/etc/collectd.conf
cp basicprobes.conf $RPM_BUILD_ROOT/opt/collectd/etc/conf.d
cp network-client.conf $RPM_BUILD_ROOT/opt/collectd/etc/conf.d
cp network-server.conf $RPM_BUILD_ROOT/opt/collectd/etc/conf.d
cp collectdinitd $RPM_BUILD_ROOT/etc/init.d/collectd
chmod 755 $RPM_BUILD_ROOT/etc/init.d/collectd


%clean
rm -rf $RPM_BUILD_ROOT

%files common
%defattr(-,root,root,-)
%doc
/etc/init.d/collectd
/opt/collectd/lib/pkgconfig/*
/opt/collectd/lib/libcollectdclient.la
/opt/collectd/lib/libcollectdclient.so
/opt/collectd/lib/libcollectdclient.so.0
/opt/collectd/lib/libcollectdclient.so.0.0.0
/opt/collectd/lib/collectd/network.la
/opt/collectd/lib/collectd/network.so
/opt/collectd/lib/collectd/exec.la
/opt/collectd/lib/collectd/exec.so
/opt/collectd/lib/collectd/unixsock.la
/opt/collectd/lib/collectd/unixsock.so
/opt/collectd/include/*
/opt/collectd/var/*
/opt/collectd/share/*
/opt/collectd/bin/*
/opt/collectd/sbin/*
/opt/collectd/etc/collectd.conf
/opt/collectd/etc/collectd.conf.sample
%dir /opt/collectd/etc/conf.d

%files java
%defattr(-,root,root,-)
/opt/collectd/lib/collectd/java.la
/opt/collectd/lib/collectd/java.so
/opt/collectd/share/collectd/java/*


%files basicprobes
%defattr(-,root,root,-)

/opt/collectd/etc/conf.d/basicprobes.conf
/opt/collectd/lib/collectd/cpu.la
/opt/collectd/lib/collectd/cpu.so
/opt/collectd/lib/collectd/df.la
/opt/collectd/lib/collectd/df.so
/opt/collectd/lib/collectd/disk.la
/opt/collectd/lib/collectd/disk.so
/opt/collectd/lib/collectd/filecount.la
/opt/collectd/lib/collectd/filecount.so
/opt/collectd/lib/collectd/fscache.la
/opt/collectd/lib/collectd/fscache.so
/opt/collectd/lib/collectd/hddtemp.la
/opt/collectd/lib/collectd/hddtemp.so
/opt/collectd/lib/collectd/interface.la
/opt/collectd/lib/collectd/interface.so
/opt/collectd/lib/collectd/iptables.la
/opt/collectd/lib/collectd/iptables.so
/opt/collectd/lib/collectd/load.la
/opt/collectd/lib/collectd/load.so
/opt/collectd/lib/collectd/logfile.la
/opt/collectd/lib/collectd/logfile.so
/opt/collectd/lib/collectd/memory.la
/opt/collectd/lib/collectd/memory.so
/opt/collectd/lib/collectd/python.la
/opt/collectd/lib/collectd/python.so
/opt/collectd/lib/collectd/swap.la
/opt/collectd/lib/collectd/swap.so
/opt/collectd/lib/collectd/tail.la
/opt/collectd/lib/collectd/tail.so
/opt/collectd/lib/collectd/users.la
/opt/collectd/lib/collectd/users.so
/opt/collectd/lib/collectd/vmem.la
/opt/collectd/lib/collectd/vmem.so

%files client
%defattr(-,root,root,-)
/opt/collectd/etc/conf.d/network-client.conf

%files server
%defattr(-,root,root,-)
/opt/collectd/etc/conf.d/network-server.conf

%post common
/sbin/chkconfig --add collectd

%post client
/etc/init.d/collectd restart

%post server
/etc/init.d/collectd restart
%changelog

