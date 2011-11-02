/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.telefonica.tcloud.jdbc_monpersistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trabajo
 */
public class MonConnection extends Thread {
    public static final int PAUSE_MILLISECONDS_BEFORE_MANUAL_RETRIES=60000;
    public static final int PAUSE_MILLISECONDS_BEFORE_AUTOMATIC_CHECKS=60000;
    
      private boolean shutdown=false;
      
      private JDBC_MonPersistence jdbcMP=null;
            
      public MonConnection(JDBC_MonPersistence jdbcMP) {
          this.jdbcMP=jdbcMP;          
      }

    public MonConnection() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
      
      public void shutdown() {
          shutdown=true;
          this.interrupt();
      }
            
      @Override
      @SuppressWarnings("SleepWhileInLoop")
      public void run() {
          while (!shutdown) {
                try {
                    Thread.sleep(PAUSE_MILLISECONDS_BEFORE_AUTOMATIC_CHECKS);
                } catch (InterruptedException ex) {
                    continue;
                }
                try {
                    if (!jdbcMP.testConnection()) jdbcMP.reconnect();
                     //if (testConnection()) continue;
                     //try { con.close(); } catch (Exception ex) {}
                     //prepareConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(JDBC_MonPersistence.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException cnfe) {
                    Logger.getLogger(JDBC_MonPersistence.class.getName()).log(Level.SEVERE, null, cnfe);
                }             
          }
      }  
    }  
    