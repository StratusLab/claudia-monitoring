/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.collectd.api.Collectd;
import org.collectd.api.CollectdWriteInterface;
import org.collectd.api.ValueList;

/**
 *
 * @author jomar
 */
public class PluginDebug implements CollectdWriteInterface {
    Logger logger=null;
    public PluginDebug() throws IOException {
        
        Collectd.registerWrite("collector_debug",this);
        logger=Logger.getLogger("monitoring.PluginDebug");
        FileHandler fileHandler=new FileHandler("/var/log/monitoring.txt");
        fileHandler.setFormatter(new SimpleFormatter());
        Logger.getLogger("").addHandler(fileHandler);
        Logger.getLogger("").setLevel(Level.ALL);
        
    }

    public int write(ValueList vl) {
        Date timestamp=new Date(vl.getTime());
        logger.log(Level.FINEST, "DEBUG {0};{1}_{2};{3}_{4} (Thread id:{5})",
                    new Object[]{timestamp, vl.getPlugin(), vl.getPluginInstance(),
                        vl.getType(), vl.getTypeInstance(),Thread.currentThread().getId()});
        return 0;
    }
}
