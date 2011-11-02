/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import com.telefonica.tcloud.collectorinterfaces.MonPublisher;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 *
 * @author jomar
 */
public class PublisherForTesting implements MonPublisher {
    private LinkedHashSet set=new LinkedHashSet();
    boolean storeAll=true;
    public PublisherForTesting() {
        
    }
    public PublisherForTesting(boolean storeAll) {
        this.storeAll=storeAll;
    }
    @Override
    public void publish(Date time,String fqn,String measureName,
            String measureUnit, Number value) {
        StringBuilder builder=new StringBuilder();
        builder.append(fqn);
        builder.append(",");
        builder.append(measureName);
        builder.append("=");
        builder.append(value);
        
        if (storeAll) {
            builder.append(' ');
            builder.append(measureUnit);
            builder.append(";");
            builder.append(time);
        }
        set.add(builder.toString());
    }
    @Override
    public void shutdown() {}
    
    public LinkedHashSet getPublishedData() {
        return set;
    }
}
