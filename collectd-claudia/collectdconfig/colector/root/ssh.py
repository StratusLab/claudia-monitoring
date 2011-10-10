#!/usr/bin/python
import pexpect
import sys
ip='109.231.78.227'
password='Dce7LBeEbWbMXkUa'
child=pexpect.spawn('scp -o StrictHostKeyChecking=no /root/bindist.tgz ubuntu@'+ip+":")
child.expect('assword:')
child.sendline(password)
child.expect(pexpect.EOF)
child=pexpect.spawn('ssh -o StrictHostKeyChecking=no ubuntu@'+ip)
child.expect('assword:')
child.sendline(password)
child.expect_exact('$')
child.sendline('exec sudo su -')
child.waitnoecho()
child.sendline(password)
child.expect('#')
child.sendline('cd /; tar xzf /home/ubuntu/bindist.tgz')
child.expect('#')
child.logfile_read=sys.stdout
child.sendline('/root/install_colector.sh ; exit ')
child.expect(pexpect.EOF,timeout=300)
