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

    
    public MySQL_MonPersistence(String url,String user,String password) throws 
            ClassNotFoundException, SQLException {
        super(url, user, password, driver);
        strInsertFQNMap="REPLACE INTO fqn (fqn,host,plugin) VALUES (?,?,?)";
        prepareConnection();
        (new MonConnection_TestConnection()).start();
        
    }

}
