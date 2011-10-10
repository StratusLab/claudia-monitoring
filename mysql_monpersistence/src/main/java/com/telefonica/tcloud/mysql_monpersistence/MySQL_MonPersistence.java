/*
 * This class contains all database related code.
 * 
 */
package com.telefonica.tcloud.mysql_monpersistence;


import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.math.BigDecimal;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public class MySQL_MonPersistence implements MonPersistence {
    private Connection con=null;
    private PreparedStatement insertFQNMap,deleteFQNMap,searchFQN,
            searchFQNPlugNull,deleteFQNMapPlugNull;
    private PreparedStatement selectAssociatedId,insertNodeDirectory;
    private PreparedStatement insertMeasure,markNodeAsInactive;
    private HashMap<String,Long> associatedIdCache;
    
    //FileWriter fw=null;
    
    public MySQL_MonPersistence(String url,String user,String password) throws 
            ClassNotFoundException, SQLException {
       Class driver=Class.forName("com.mysql.jdbc.Driver");
       con=
            DriverManager.getConnection(
               url,user,password);
       con.setAutoCommit(true);
       
       insertFQNMap=con.prepareStatement("REPLACE INTO fqn (fqn,host,plugin) VALUES (?,?,?)");
       deleteFQNMap=con.prepareStatement("DELETE FROM fqn WHERE host=? AND plugin=?");
       deleteFQNMap=con.prepareStatement("DELETE FROM fqn WHERE host=? AND plugin=?");
       deleteFQNMapPlugNull=con.prepareStatement("DELETE FROM fqn WHERE host=? AND plugin IS NULL");
       searchFQN=con.prepareStatement("SELECT fqn FROM fqn WHERE host=? AND plugin=?");
       searchFQNPlugNull=con.prepareStatement("SELECT fqn FROM fqn WHERE host=? AND plugin IS NULL");
       selectAssociatedId=con.prepareStatement("SELECT internalid FROM nodedirectory "
               +"WHERE fqn=?");
       insertNodeDirectory=con.prepareStatement("INSERT INTO nodedirectory "+
                       "(fqn,internalNodeId,status,fechaCreacion,tipo,parent_internalId)"+
                       "values (?,1,1,now(),?,?)",Statement.RETURN_GENERATED_KEYS);
       markNodeAsInactive=con.prepareStatement("UPDATE nodedirectory SET status=0,fechaBorrado=now() "+
               "WHERE (fqn=? OR fqn LIKE ?) AND status<>0");
       insertMeasure=con.prepareStatement("INSERT INTO monitoringsample (datetime,"
               +"day,month,year,hour,minute,value,measure_type,unit,"+
               "associatedObject_internalId) VALUES (?,?,?,?,?,?,?,?,?,?)");
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
            insertNodeDirectory.setInt(2, 10);
            insertNodeDirectory.setBigDecimal(3, BigDecimal.valueOf(id));
            insertNodeDirectory.execute();
            
            result=insertNodeDirectory.getGeneratedKeys();
            if (!result.next()) {result.close(); return null; }
            
        } 
        Long id=result.getLong(1);
        result.close();
        return id;
    }
    public void insertData(Date time,String fqn,String measureType,
            String measureUnit,Number value) throws SQLException {
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
        insertMeasure.setString(8, measureType);
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
        return;      
        
    }
    
    public void insertFQNMap(String fqn,String host,String pluginWithInstance) throws SQLException {

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
            Logger.getLogger(MySQL_MonPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    
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
    public void shutdown() {
        try {
            con.close();
        } catch (SQLException ex) {
            
        }
    }
    
    public static void main(String argv[]) throws SQLException {
        LinkedHashMap<String,String[]> config=new LinkedHashMap<String,String[]>();
        String mysqlurl[]={"jdbc:mysql://localhost:3306/monitoring"};
        String mysqluser[]={"claudia"};
        String mysqlpassword[]={"ClaudiaPass"};
        config.put("mysqlurl",mysqlurl);
        config.put("mysqluser",mysqluser);
        config.put("mysqlpassword",mysqlpassword);
                
        MonPersistence persistence= MonPersistenceFactory.getPersistence(
         "com.telefonica.tcloud.mysql_monpersistence.MySQL_MonPersistenceFactory"
          ,config);
        String host="collector";
        String fqn="es.tid.customers.cc1.services.monitoring.vees.collector.replicas.1";
        persistence.insertData(new Date(),fqn,"cpu","cycles",345799);
        persistence.insertFQNMap(fqn, host, null);
        persistence.insertFQNMap(fqn+".plug1.value1", host, "plug1-value1");
        
        System.out.println("FQN: "+persistence.searchFQN(host,"plug1-value1"));
        System.out.println("FQN: "+persistence.searchFQN(host,null));
        System.out.println("ID: "+((MySQL_MonPersistence)persistence).getAssociatedObjectId(fqn+".plug2.value3"));
        //persistence.deleteFQNMap(host,null);
        //persistence.deleteFQNMap(host, "plug1-value1");
    }


}
