/*
 * This class contains all database related code.
 * 
 */
package com.telefonica.tcloud.mysql_monpersistence;


import com.telefonica.tcloud.jdbc_monpersistence.JDBC_MonPersistence;
import java.sql.SQLException;

/**
 *
 * @author jomar
 */
public class MySQL_MonPersistence extends JDBC_MonPersistence {
    public static final String driver="com.mysql.jdbc.Driver";
    /* --- jicg22 obsolete!
    private Connection con=null;
    private PreparedStatement insertFQNMap,deleteFQNMap,searchFQN,
            searchFQNPlugNull,deleteFQNMapPlugNull;
    private PreparedStatement selectAssociatedId,insertNodeDirectory;
    private PreparedStatement insertMeasure,markNodeAsInactive;
    private HashMap<String,Long> associatedIdCache;
    
    //FileWriter fw=null;
*/    
    public MySQL_MonPersistence(String url,String user,String password) throws 
            ClassNotFoundException, SQLException {
        super(url, user, password, driver);
        strInsertFQNMap="REPLACE INTO fqn (fqn,host,plugin) VALUES (?,?,?)";
        prepareConnection();
        //monitorizationStart(this);
       /* 
        * Class driver=Class.forName("com.mysql.jdbc.Driver");
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
       * 
       */
    }
    
    /* private Long getAssociatedObjectId(String fqn) throws SQLException {
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
    } */
    
    /* public void insertData(Date time,String fqn,String measureType,
            String measureUnit,Number value) throws SQLException {
                      
        --- Subido a la clase padre
        
    } */
    
    /* public void insertFQNMap(String fqn,String host,String pluginWithInstance) throws SQLException {

       --- Subido a la clase padre
                  
    }*/
    /* public void deleteFQNMap(String host, String pluginWithInstance) {
        
       --- Subido a la clase padre
      
    } */    
    /*
    public String searchFQN(String host,String pluginWithInstance) throws SQLException {
      ---- Subido a la clase padre.   
    }
    */
    /* 
    public void shutdown() {
       --- Subido a la clase padre!!!
    } */
    /*
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
        //System.out.println("ID: "+((MySQL_MonPersistence)persistence).getAssociatedObjectId(fqn+".plug2.value3"));
        //persistence.deleteFQNMap(host,null);
        //persistence.deleteFQNMap(host, "plug1-value1");
        * 
        
    }*/


}
