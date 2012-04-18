/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.telefonica.tcloud.jdbc_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trabajo
 */
public class JDBC_MonPersistence implements MonPersistence
{
    public static final int PAUSE_MILLISECONDS_BEFORE_MANUAL_RETRIES=6000;
    public static final int PAUSE_MILLISECONDS_BEFORE_AUTOMATIC_CHECKS=6000;
    
    protected String strInsertFQNMap="INSERT INTO fqn (fqn,host,plugin) VALUES (?,?,?)";
    protected String strDeleteFQNMap="DELETE FROM fqn WHERE host=? AND plugin=?";
    protected String strDeleteFQNMapPlugNull="DELETE FROM fqn WHERE host=? AND plugin IS NULL";
    protected String strSearchFQN="SELECT fqn FROM fqn WHERE host=? AND plugin=?";
    protected String strSearchFQNPlugNull="SELECT fqn FROM fqn WHERE host=? AND plugin IS NULL";
    protected String strSelectAssociatedId="SELECT internalid FROM nodedirectory "
               +"WHERE fqn=?";
    /* - - jicg22 Esto es para MySQL, PostgreSQL - - 
    protected String strInsertNodeDirectory="INSERT INTO nodedirectory "+
                       "(fqn,internalNodeId,status,fechaCreacion,tipo,parent_internalId)"+
                       "values (?,1,1,now(),?,?)"; - - */
    protected String strInsertNodeDirectory="INSERT INTO nodedirectory "+
                       "(fqn,internalNodeId,status,fechaCreacion,tipo,parent_internalId)"+
                       "values (?,1,1,?,?,?)";    
    /* - - jicg22 Esto es para MySQL, PostgreSQL - - 
     protected String strMarkNodeAsInactive="UPDATE nodedirectory SET status=0,fechaBorrado=now() "+
               "WHERE (fqn=? OR fqn LIKE ?) AND status<>0"; */
    protected String strMarkNodeAsInactive="UPDATE nodedirectory SET status=0,fechaBorrado=? "+
               "WHERE (fqn=? OR fqn LIKE ?) AND status<>0";
    protected String strInsertMeasure="INSERT INTO monitoringsample (datetime,"
               +"day,month,year,hour,minute,value,measure_type,unit,"+
               "associatedObject_internalId) VALUES (?,?,?,?,?,?,?,?,?,?)";
    protected String strTestConnection=null;
    
    protected String strGetFNQMapCount="select count(1) as n from fqn";
    
    protected String strDeleteAllFQNMap="delete from fqn";
    protected String strDeleteAllNodeDirectory="delete from nodedirectory";
    protected String strDeleteAllMonitoringSample="delete from monitoringsample";
    
// - pgsql: protected static String strInsertFQNMap="INSERT INTO fqn (fqn,host,plugin) VALUES (?,?,?)";
    private Connection con=null;
    private PreparedStatement insertFQNMap,deleteFQNMap,searchFQN,
            searchFQNPlugNull,deleteFQNMapPlugNull;
    private PreparedStatement selectAssociatedId,insertNodeDirectory;
    private PreparedStatement insertMeasure,markNodeAsInactive, getFNQMapCount;
    private PreparedStatement deleteAllFQNMap, deleteAllNodeDirectory, deleteAllMonitoringSample;
    private PreparedStatement testConnection=null;
    private HashMap<String,Long> associatedIdCache;
    
    //FileWriter fw=null;
    private String user=null;
    private String password=null;
    private String url=null;
    private String driverName=null;
    
    public JDBC_MonPersistence (String url,String user, String password, 
                                String driverName) 
           throws ClassNotFoundException, SQLException {
        
        this.user=user;
        this.password=password;
        this.url=url;
        this.driverName=driverName;
        con=connect();
    }
    
    synchronized protected void prepareConnection() throws SQLException {
        insertFQNMap=con.prepareStatement(strInsertFQNMap);
        deleteFQNMap=con.prepareStatement(strDeleteFQNMap);
        deleteFQNMapPlugNull=con.prepareStatement(strDeleteFQNMapPlugNull);
        searchFQN=con.prepareStatement(strSearchFQN);
        searchFQNPlugNull=con.prepareStatement(strSearchFQNPlugNull);
        selectAssociatedId=con.prepareStatement(strSelectAssociatedId);
        insertNodeDirectory=con.prepareStatement(strInsertNodeDirectory,Statement.RETURN_GENERATED_KEYS);
        markNodeAsInactive=con.prepareStatement(strMarkNodeAsInactive);
        insertMeasure=con.prepareStatement(strInsertMeasure);
        if (strTestConnection!=null) {
               testConnection=con.prepareStatement(strTestConnection);
               System.out.println("textConnection es "+strTestConnection);
        }
        
        associatedIdCache=new HashMap<String,Long>();      
        
        deleteAllFQNMap=con.prepareStatement(strDeleteAllFQNMap);
        deleteAllMonitoringSample=con.prepareStatement(strDeleteAllMonitoringSample);
        deleteAllNodeDirectory=con.prepareStatement(strDeleteAllNodeDirectory);
        getFNQMapCount=con.prepareStatement(strGetFNQMapCount);                
    }

   synchronized private Long getAssociatedObjectId(String fqn) throws SQLException {
        selectAssociatedId.setString(1, fqn);
        ResultSet result=selectAssociatedId.executeQuery();
        
        if (!result.next()) {result.close(); return null; }
        /* Old code: create entry automatically if exists fqn with the same
         * preffix (all until .replica.number inclusive).
         *
        
        if (!result.next()) {
            // not found; if preffix is hostname, create entry, if error, 
            // ignore entry.
            result.close();
            int pos=fqn.indexOf("replicas.");
            if (pos==-1) return null;
            pos=fqn.indexOf(".",pos+9);
            if (pos==-1) return null;
            String host=fqn.substring(0,pos);
            selectAssociatedId.setString(1,host);
            result=selectAssociatedId.executeQuery();
            
            if (!result.next()) { result.close(); return null; }
            Long id=result.getLong(1);
            result.close();
            
            insertNodeDirectory.setString(1, fqn);
            insertNodeDirectory.setTimestamp(2, new Timestamp(new Date().getTime()));
            insertNodeDirectory.setInt(3, 10);            
            insertNodeDirectory.setBigDecimal(4, BigDecimal.valueOf(id));
            insertNodeDirectory.execute();
            
            result=insertNodeDirectory.getGeneratedKeys();
            if (!result.next()) {result.close(); return null; }
            
        } 
        * 
        */
        Long id=result.getLong(1);
        result.close();
        return id;
    }
    
    synchronized public void insertData(Date time, String fqn, String measuredType, 
                        String measureUnit, Number value) throws SQLException {
        Long id=associatedIdCache.get(fqn);
        if (id==null) {
            id=getAssociatedObjectId(fqn);
            if (id==null) return;
        }
        
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(time);
        
        insertMeasure.setTimestamp(1, new java.sql.Timestamp(time.getTime()));
        insertMeasure.setInt(2,calendar.get(Calendar.DAY_OF_MONTH));
        insertMeasure.setInt(3,calendar.get(Calendar.MONTH)+1);
        insertMeasure.setInt(4,calendar.get(Calendar.YEAR));
        insertMeasure.setInt(5,calendar.get(Calendar.HOUR));
        insertMeasure.setInt(6,calendar.get(Calendar.MINUTE));
        insertMeasure.setString(7, value.toString());
        insertMeasure.setString(8, measuredType);
        insertMeasure.setString(9, measureUnit);
        insertMeasure.setBigDecimal(10, BigDecimal.valueOf(id));
        insertMeasure.executeUpdate();
             
    }

    synchronized public void insertFQNMap(String fqn, String host, String pluginWithInstance) throws SQLException {
        insertFQNMap.setString(1, fqn);
        insertFQNMap.setString(2,host);
        insertFQNMap.setString(3,pluginWithInstance);
        insertFQNMap.execute();      
        
        insertNodeDirectory.setString(1, fqn);
        insertNodeDirectory.setTimestamp(2, new Timestamp(new Date().getTime()));
        insertNodeDirectory.setInt(3, 4);
        insertNodeDirectory.setBigDecimal(4, null);
        insertNodeDirectory.execute();        
    }

    synchronized public String searchFQN(String host, String pluginWithInstance) throws SQLException {
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

    /**
     * 
     * @param host
     * @param pluginWithInstance 
     */
    synchronized public void deleteFQNMap(String host, String pluginWithInstance) {
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
          markNodeAsInactive.setTimestamp(1, new Timestamp(new Date().getTime()));
          markNodeAsInactive.setString(2, fqn);
          markNodeAsInactive.setString(3, fqn+".%");
          markNodeAsInactive.executeUpdate();

         } catch (SQLException ex) {
            Logger.getLogger(JDBC_MonPersistence.class.getName()).log(Level.SEVERE, null, ex);
         } 
    }

    /**
     * Cierra la conexión.
     */
    synchronized public void shutdown() {
        try {
            con.close();
        } catch (Exception ex) {
            
        }
        //con=null;
    }
    
    /**
     * Obtiene la conexión actual.
     * 
     * @return 
     */
    synchronized public Connection getConnection() { return con; }
    
    /**
     * Realiza la conexion con JDBC
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    synchronized private Connection connect()  throws ClassNotFoundException, SQLException {
        if (con != null && !con.isClosed()) {
             shutdown();
        }
        Class driver=Class.forName(driverName);                          
        con= DriverManager.getConnection(
               url,user,password);        
        con.setAutoCommit(true);       
        return con;
    }
    
    /**
     * Desconecta y vuelve a conectar - Interesante en temas de reconexión.
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    synchronized protected Connection reconnect() throws ClassNotFoundException, SQLException {
        shutdown();
        connect();
        prepareConnection();
        return con;
    }
    
    /**
     * 
     * @return 
     */
    synchronized protected boolean testConnection()  {       
        if (con==null) return false;
        try {
            if (testConnection==null) {
                return con.isValid(2);
            } else {
                try {               
                    ResultSet rs=testConnection.executeQuery();
                    boolean result=rs.next();
                    rs.close();
                    return result;
                } catch (SQLException ex) {
                    return false;
                }
            }   
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Metodo para preubas unitarias - Se le pasa un FQN y devuelve el número de
     * elementos que hay
     * 
     * @param fqn
     * @return 
     */
    public long count(String fqn) throws SQLException {
        long nfqn=-1l;
        ResultSet rs=null;
            
        getFNQMapCount.execute();
        rs=getFNQMapCount.getResultSet();
        
        if (rs.next()) {
            nfqn=rs.getLong(1);
        } 
        
        rs.close();
        return nfqn;        
    }
    /**
     * Metodo para pruebas unitarias - Se le pasa un FQN y devuelve el número de
     * elementos que había en la tabla antes de borrarlos.
     * 
     * @param fqn
     * @return 
     */
    public long purge(String fqn) throws SQLException {        
        long res=count(fqn);
        
        deleteAllMonitoringSample.execute();        
        deleteAllFQNMap.execute();                  
        deleteAllNodeDirectory.execute();
        
        return res;
    }
    
     /**
     * Clase interna de PostgreSQL para
     */
    public class MonConnection_TestConnection extends Thread {
      private boolean shutdown=false;
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
                    if (!testConnection()) reconnect();
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException cnfe) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, cnfe);
                }
          }
      }
    }
}
