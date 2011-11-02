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
    
    
// - pgsql: protected static String strInsertFQNMap="INSERT INTO fqn (fqn,host,plugin) VALUES (?,?,?)";
    private Connection con=null;
    private PreparedStatement insertFQNMap,deleteFQNMap,searchFQN,
            searchFQNPlugNull,deleteFQNMapPlugNull;
    private PreparedStatement selectAssociatedId,insertNodeDirectory;
    private PreparedStatement insertMeasure,markNodeAsInactive;
    private PreparedStatement testConnection=null;
    private HashMap<String,Long> associatedIdCache;
    
    //FileWriter fw=null;
    private String user;
    private String password;
    private String url;
    private String driverName;
    
    public JDBC_MonPersistence (String url,String user, String password, 
                                String driverName) 
           throws ClassNotFoundException, SQLException {
        
        this.user=user;
        this.password=password;
        this.url=url;
        this.driverName=driverName;
        con=connect();
    }
    
    protected void prepareConnection() throws SQLException {
        insertFQNMap=con.prepareStatement(strInsertFQNMap);
        deleteFQNMap=con.prepareStatement(strDeleteFQNMap);
        deleteFQNMapPlugNull=con.prepareStatement(strDeleteFQNMapPlugNull);
        searchFQN=con.prepareStatement(strSearchFQN);
        searchFQNPlugNull=con.prepareStatement(strSearchFQNPlugNull);
        selectAssociatedId=con.prepareStatement(strSelectAssociatedId);
        insertNodeDirectory=con.prepareStatement(strInsertNodeDirectory,Statement.RETURN_GENERATED_KEYS);
        markNodeAsInactive=con.prepareStatement(strMarkNodeAsInactive);
        insertMeasure=con.prepareStatement(strInsertMeasure);
        if (strTestConnection!=null) testConnection=con.prepareStatement(strTestConnection);
        associatedIdCache=new HashMap<String,Long>();        
    }

        private Long getAssociatedObjectId(String fqn) throws SQLException {
        selectAssociatedId.setString(1, fqn);
        ResultSet result=selectAssociatedId.executeQuery();
        
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
        Long id=result.getLong(1);
        result.close();
        return id;
    }
    
    public void insertData(Date time, String fqn, String measuredType, 
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
        
        /*
        StringBuilder insert=new StringBuilder(
            "INSERT INTO monitoringsample (datetime,day,month,year,hour,minute,"
           +"value,measure_type,unit,associatedObject_internalId) VALUES ('");
        insert.append(new java.sql.Timestamp(time.getTime()));
        insert.append("',").append(calendar.get(Calendar.DAY_OF_MONTH));
        insert.append(',').append(calendar.get(Calendar.MONTH)).append(',');
        insert.append(calendar.get(Calendar.YEAR)).append(',');
        insert.append(calendar.get(Calendar.HOUR)).append(',');
        insert.append(calendar.get(Calendar.MINUTE)).append(",'").append(value);
        insert.append("','").append(measureType).append("','");
        insert.append(measureUnit).append("',");
        insert.append(id).append(");");

        Statement st=con.createStatement();
        st.executeUpdate(insert.toString());
        st.close();
         * 
         */        
    }

    public void insertFQNMap(String fqn, String host, String pluginWithInstance) throws SQLException {
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

    public String searchFQN(String host, String pluginWithInstance) throws SQLException {
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
    public void shutdown() {
        try {
            con.close();
        } catch (SQLException ex) {
            
        }
        con=null;
    }
    
    /**
     * Obtiene la conexión actual.
     * 
     * @return 
     */
    protected Connection getConnection() { return con; }
    
    /**
     * Realiza la conexion con JDBC
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private Connection connect()  throws ClassNotFoundException, SQLException {
        if (con != null) shutdown();
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
    protected Connection reconnect() throws ClassNotFoundException, SQLException {
        shutdown();
        return connect();
    }
    
    /**
     * 
     * @return 
     */
    protected boolean testConnection()  {
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
    
    private MonConnection daemonThread=null;
    /**
     *  
     */
    protected void monitorizationStart() {
        daemonThread=new MonConnection();
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}
