package com.telefonica.tcloud.collectdprotocol;

import com.telefonica.tcloud.collectorexternalinterface.CollectorI;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A standalone program that replace collectd + java plugin to
 * invoke the collector code.
 * 
 * This software only implements the
 * part required to receive data. There are not support to
 * decrypt data or check the signature.
 * 
 * The documentation about binary protocol of collectd is in
 * http://www.collectd.org/wiki/index.php/Binary_protocol
 *
 */
public class App 
{
    public static final int defaultPort=25826;
    public static final int maxLength=1452;
    private CollectorI collector=null;
    private HashMap<String,String[]> dataSourcesByType=null;
    private DatagramSocket udpSocket=null;
    
    public App(CollectorI collector,HashMap<String,String[]> dataSourcesByType,
            DatagramSocket udpSocket) {
       this.collector=collector;
       this.dataSourcesByType=dataSourcesByType;
       this.udpSocket=udpSocket;
    }
    
    public void start() {
        byte buffer[]=new byte[2000];
        DatagramPacket packet=new DatagramPacket(buffer,2000);
        while (true) {
            try {
                udpSocket.receive(packet);
                parsePackage(buffer);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
    }
    
    private void parsePackage(byte stream[]) {
        String host=null;
        String plugin=null,pluginInstance=null;
        String type=null,typeInstance=null;
        ArrayList<Number> values=new ArrayList<Number>();

        long timestamp=0;
        long interval=0;
        int pos=0;
        
        while (pos<stream.length) {
            int partType=stream[pos]*256+stream[pos+1];
            int length=stream[pos+2]*256+stream[pos+3];
            switch (partType) {
                case 0: host=new String(stream,pos+4,length-4); break;
                case 1: /* time */ break;
                case 2: plugin= new String(stream,pos+4,length-4); break;
                case 3: pluginInstance= new String(stream,pos+4,length-4); break;
                case 4: type= new String(stream,pos+4,length-4); break;
                case 5: typeInstance= new String(stream,pos+4,length-4); break;
                case 6: /* values */ int entries=stream[pos+4]*256+stream[pos+5];
                    for(int counter=0;counter<entries;counter++) {
                        switch (stream[pos+6+9*counter]) {
                            case 0: // COUNTER 
                            case 2: // DERIVE
                            case 3: // ABSOLUTE
                                values.add(ByteBuffer.wrap(stream,
                                        pos+6+9*counter,8).getLong());
                            case 1: // GAUGE
                                values.add(ByteBuffer.wrap(stream,
                                        pos+6+9*counter,8).getDouble());
                        }
                    }
                case 7: /*interval*/ break;
                case 8: timestamp=ByteBuffer.wrap(stream,pos+4,8).getLong()<<30;
                        break;
                case 9: interval=ByteBuffer.wrap(stream,pos+4,8).getLong();
                        break;
                case 0x100: /* notification */
                case 0x101: /* severity */
                case 0x200: /* signature */
                case 0x210: /* encryption */
                default:
            }
            pos+=length;
            
        }
        try {
            collector.write(host, plugin, pluginInstance, type, typeInstance, 
                    dataSourcesByType.get(type), values, new Date(timestamp));
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
