/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.postgresql_monpersistence;

import com.telefonica.tcloud.jdbc_monpersistence.JDBC_MonPersistence;
import com.telefonica.tcloud.jdbc_monpersistence.MonConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public class PostgreSQL_MonPersistence extends JDBC_MonPersistence {
    private static final String jdbcDriver="org.postgresql.Driver";
    
    //public static final int PAUSE_MILLISECONDS_BEFORE_MANUAL_RETRIES=60000;
    //public static final int PAUSE_MILLISECONDS_BEFORE_AUTOMATIC_CHECKS=60000;
    
    //private Connection con=null;
    /*private PreparedStatement insertFQNMap,deleteFQNMap,searchFQN,
            searchFQNPlugNull,deleteFQNMapPlugNull;
    private PreparedStatement selectAssociatedId,insertNodeDirectory;
    private PreparedStatement insertMeasure,markNodeAsInactive;*/
    private PreparedStatement testConnection; 
    private HashMap<String,Long> associatedIdCache;
    // use this to decect when an error is persistent and we should not retry
    // the connection in a while.
    private Date lastManualRetryTimeStamp=null;
    //private String url,user,password;
    
    
    
    public PostgreSQL_MonPersistence(String url,String user,String password) throws
            ClassNotFoundException, SQLException {
       super(url, user, password, jdbcDriver);
               
       // url pattern is jdbc:postgresql:[//host[:port]/]database
       // store values; a restart may be needed.
       // this.url=url;this.user=user;this.password=password;
       
       super.strTestConnection="SELECT 1";
       
       prepareConnection();              
       associatedIdCache=new HashMap<String,Long>();
       //monitorizationStart();
       
    }
    
    /* @Override
    protected void prepareConnection() 
            throws SQLException {
       Connection conTmp=
            DriverManager.getConnection(
               url,user,password); 
       
       //conTmp.setAutoCommit(true);
       
       
       // super.prepareConnection();
       
       //insertFQNMap=conTmp.prepareStatement("INSERT INTO fqn (fqn,host,plugin) VALUES (?,?,?)");
       
       /* deleteFQNMap=conTmp.prepareStatement("DELETE FROM fqn WHERE host=? AND plugin=?");
       deleteFQNMapPlugNull=conTmp.prepareStatement("DELETE FROM fqn WHERE host=? AND plugin IS NULL");
       searchFQN=conTmp.prepareStatement("SELECT fqn FROM fqn WHERE host=? AND plugin=?");
       searchFQNPlugNull=conTmp.prepareStatement("SELECT fqn FROM fqn WHERE host=? AND plugin IS NULL");
       selectAssociatedId=conTmp.prepareStatement("SELECT internalid FROM nodedirectory "
               +"WHERE fqn=?");
       insertNodeDirectory=conTmp.prepareStatement("INSERT INTO nodedirectory "+
                       "(fqn,internalNodeId,status,fechaCreacion,tipo,parent_internalId)"+
                       "values (?,1,1,now(),?,?)",Statement.RETURN_GENERATED_KEYS);
       markNodeAsInactive=conTmp.prepareStatement("UPDATE nodedirectory SET status=0,fechaBorrado=now() "+
               "WHERE (fqn=? OR fqn LIKE ?) AND status<>0");
       insertMeasure=conTmp.prepareStatement("INSERT INTO monitoringsample (datetime,"
               +"day,month,year,hour,minute,value,measure_type,unit,"+
               "associatedObject_internalId) VALUES (?,?,?,?,?,?,?,?,?,?)");  
       //testConnection=getConnection().prepareStatement("SELECT 1");
       //con=conTmp;
    } */
    

/*
    public void insertData(Date time, String fqn, String measuredType, String measureUnit, Number value) throws SQLException {
  ---- En la clase base.       
    }
*/
    /*
    public void insertFQNMap(String fqn, String host, String pluginWithInstance) throws SQLException {
        insertFQNMap.setString(1, fqn);
        insertFQNMap.setString(2,host);
        insertFQNMap.setString(3,pluginWithInstance);
        insertFQNMap.execute();      
        
        insertNodeDirectory.setString(1, fqn);
        insertNodeDirectory.setInt(2, 4);
        insertNodeDirectory.setBigDecimal(3, null);
        insertNodeDirectory.execute();
        return;
    }
*/
    /*
    public String searchFQN(String host,String pluginWithInstance) throws SQLException {
        String fqn=null;
        ResultSet rs=null;
        if (pluginWithInstance==null||pluginWithInstance.isEmpty()) {
            searchFQNPlugNull.setString(1,host);
            searchFQNPlugNull.execute();
            rs=searchFQNPlugNull.getResultSet();
        } else {
          searchFQN.setString(1, host);
          searchFQN.setString(2, pluginWithInstance);
          searchFQN.execute();
          rs=searchFQN.getResultSet();
        }
        if (rs.next()) {
            fqn=rs.getString(1);
        } 
        rs.close();
        return fqn;
        
    }
*//*
    public void deleteFQNMap(String host, String pluginWithInstance) {
        try {
          String fqn=searchFQN(host,pluginWithInstance);  
          if (pluginWithInstance==null ||pluginWithInstance.isEmpty()) {
           deleteFQNMapPlugNull.setString(1, host);
           deleteFQNMapPlugNull.executeUpdate();

          } else {
           deleteFQNMap.setString(1, host);
           deleteFQNMap.setString(2, pluginWithInstance);
           deleteFQNMap.executeUpdate();
          }
          markNodeAsInactive.setString(1, fqn);
          markNodeAsInactive.setString(2, fqn+".%");
          markNodeAsInactive.executeUpdate();

         } catch (SQLException ex) {
            Logger.getLogger(PostgreSQL_MonPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
/*
    public void shutdown() {
        try {
            con.close();
            
        } catch (SQLException ex) {
          
        }
        if (daemonThread!=null)
            daemonThread.shutdown();
    }
    */
    /* public boolean retry() {
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
    } */
    /*
    public class MonConnection extends Thread {
      private boolean shutdown=false;
      public void shutdown() {
          shutdown=true;
          this.interrupt();
      }
      
      private boolean testConnection() {
        try {
                //return con.isValid(2);
                ResultSet rs=testConnection.executeQuery();
                boolean result=rs.next();
                rs.close();
                return result;
        } catch (SQLException ex) {
                return false;
        }
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
                    if (!testConnection()) reconnect();
                     //if (testConnection()) continue;
                     //try { con.close(); } catch (Exception ex) {}
                     //prepareConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(PostgreSQL_MonPersistence.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException cnfe) {
                    Logger.getLogger(PostgreSQL_MonPersistence.class.getName()).log(Level.SEVERE, null, cnfe);
                }
             
          }
      }  
    }   */
    
}
