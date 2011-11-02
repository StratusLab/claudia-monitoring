/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

/**
 *
 * @author jomar
 */
public class MeasureType {
    private String measureType;
    private String measureUnit;
    public MeasureType(String measureType,String measureUnit) {
        this.measureType=measureType; this.measureUnit=measureUnit;
        
    }

    public String getMeasureType() {
        return measureType;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }
}
