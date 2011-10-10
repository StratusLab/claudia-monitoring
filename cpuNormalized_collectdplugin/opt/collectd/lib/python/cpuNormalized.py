#!/usr/bin/env python
import time
import os
import collectd

useGauges=True
jiffiesbysecond=os.sysconf("SC_CLK_TCK")
cpus=os.sysconf("SC_NPROCESSORS_ONLN")

def config(conf):
 global useGauges
 if conf.children[0].key=='useGauges' :
    useGauges=conf.children[0].values[0]

def write_metric(name,value):
 metric= collectd.Values()
 metric.plugin= 'cpuNormalized'
 metric.type= 'cpuNormalizedGauge'
 metric.type_instance=name
 metric.values.append(value)
 metric.dispatch()

def write_metric_derive(name,value):
 metric= collectd.Values()
 metric.plugin= 'cpuNormalized'
 metric.type= 'cpuNormalizedDerive'
 metric.type_instance=name
 metric.values.append(value)
 metric.dispatch()

def read(data=None):
 fields1_bycpu=[]
 f=open("/proc/stat")
 if useGauges:
   f2=open("/proc/stat")
 time.sleep(0.1) # to minimize influence of program in result
 fields1=f.readline().split()[1:]
 if cpus>1:
   for cpunum in range(cpus):
       line=f.readline().split()[1:]
       fields1_bycpu.append(line)
 if useGauges:
   time.sleep(10)
   fields2=f2.readline().split()[1:]
   if cpus>1:
     fields2_bycpu=[]
     for cpunum in range(cpus):
       line=f.readline().split()[1:]
       fields2_bycpu.append(line)
   user=(float(fields2[0])-float(fields1[0]))/jiffiesbysecond/10/cpus
  nice=(float(fields2[1])-float(fields1[1]))/jiffiesbysecond/10/cpus
   system=(float(fields2[2])-float(fields1[2]))/jiffiesbysecond/10/cpus
   idle=(float(fields2[3])-float(fields1[3]))/jiffiesbysecond/10/cpus
   iowait=(float(fields2[4])-float(fields1[4]))/jiffiesbysecond/10/cpus
   irq=(float(fields2[5])-float(fields1[5]))/jiffiesbysecond/10/cpus
   softirq=(float(fields2[6])-float(fields1[6]))/jiffiesbysecond/10/cpus
   idleOrNice=idle+nice
   write_metric('user',user)
   write_metric('system',system)
   write_metric('nice',nice)
   write_metric('idle',idle)
   write_metric('idleOrNice',idleOrNice)
   write_metric('iowait',iowait)
   write_metric('irq',irq)
   write_metric('softirq',softirq)
   if (len(fields2)>=8) :
     steal=(float(fields2[7])-float(fields1[7]))/jiffiesbysecond/10/cpus
     write_metric('steal',steal)
     if (len(fields2)>=9) :
       guest=(float(fields2[8])-float(fields1[8]))/jiffiesbysecond/10/cpus
       write_metric('guest',guest)

   user=(float(fields2[0])-float(fields1[0]))/jiffiesbysecond/10/cpus
   nice=(float(fields2[1])-float(fields1[1]))/jiffiesbysecond/10/cpus
   system=(float(fields2[2])-float(fields1[2]))/jiffiesbysecond/10/cpus
   idle=(float(fields2[3])-float(fields1[3]))/jiffiesbysecond/10/cpus
   iowait=(float(fields2[4])-float(fields1[4]))/jiffiesbysecond/10/cpus
   irq=(float(fields2[5])-float(fields1[5]))/jiffiesbysecond/10/cpus
   softirq=(float(fields2[6])-float(fields1[6]))/jiffiesbysecond/10/cpus
   idleOrNice=idle+nice

 # always write derive metrics
 write_metric_derive('user',float(fields1[0])/jiffiesbysecond/cpus)
 write_metric_derive('nice',float(fields1[1])/jiffiesbysecond/cpus)
 write_metric_derive('system',float(fields1[2])/jiffiesbysecond/cpus)
 write_metric_derive('idle',float(fields1[3])/jiffiesbysecond/cpus)
 write_metric_derive('iowait',float(fields1[4])/jiffiesbysecond/cpus)
 write_metric_derive('irq',float(fields1[5])/jiffiesbysecond/cpus)
 write_metric_derive('softirq',float(fields1[6])/jiffiesbysecond/cpus)
 if (len(fields2)>=8) :
   steal=float(fields1[7])/jiffiesbysecond/cpus
   write_metric_derive('steal',steal)
   if (len(fields2)>=9) :
     guest=float(fields1[8])/jiffiesbysecond/cpus
     write_metric_derive('guest',guest)
 f.close()
 if useGauges:
   f2.close()


collectd.register_config(config)
collectd.register_read(read)

