/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.postgresql_monpersistence;

import com.telefonica.tcloud.jdbc_monpersistence.JDBC_MonPersistence;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author jomar
 */
public class PostgreSQL_MonPersistence extends JDBC_MonPersistence {
    private static final String jdbcDriver="org.postgresql.Driver";
    
    private Date lastManualRetryTimeStamp=null;
    
    public PostgreSQL_MonPersistence(String url,String user,String password) throws
            ClassNotFoundException, SQLException {
       super(url, user, password, jdbcDriver);                     
       super.strTestConnection="SELECT 1";
       
       prepareConnection();                            
       (new MonConnection_TestConnection()).start();
    }
/*
    public boolean retry() {
        // don't retry if there is a recent error
        Connection con=getConnection();
        Date now=new Date();
        if (lastManualRetryTimeStamp!=null) {
          if ((now.getTime()-lastManualRetryTimeStamp.getTime())<
                  PAUSE_MILLISECONDS_BEFORE_MANUAL_RETRIES) return false;
        } 
        lastManualRetryTimeStamp=now;
        // don't retry if there is a valid connection
        try {
            if (con.isValid(2)) return false;
        } catch (SQLException ex) {
        }
        // try to reconnect (and create again PreparedStatements)
        // retry if all is ok.
        try {
            try { con.close(); } catch (Exception ex) {}
            prepareConnection();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }    
    * 
    */
}
