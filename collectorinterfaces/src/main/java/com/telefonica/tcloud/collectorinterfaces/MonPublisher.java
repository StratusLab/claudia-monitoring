/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

import java.util.Date;

/**
 *
 * @author jomar
 */
public interface MonPublisher {
    public void publish(Date time,String fqn,String measureName,
            String measureUnit, Number value);
    public void shutdown();

}
