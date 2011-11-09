package com.telefonica.tcloud.jdbc_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    private void empty_tables(Connection c) throws SQLException{
        c.createStatement().execute("delete from nodedirectory ");        
        c.createStatement().execute("delete from fqn");
        c.createStatement().execute("delete from monitoringsample");
    }
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) throws ClassNotFoundException, SQLException
    {        
        super( testName );
        
        String dbURL="jdbc:derby:testdb;create=true";
        
        MonPersistence persistence = new Derby_MonPersistence(dbURL, "user", "password");
        
        try {
            empty_tables( ((JDBC_MonPersistence)persistence).getConnection());
        } catch (Exception e) {
            //Esto son pruebas. No es lugar para tirar exceptiones.
        }
        
        String host="collector";
        String fqn="es.tid.customers.cc1.services.monitoring.vees.collector.replicas.2";
        persistence.insertData(new Date(),fqn,"cpu","cycles",345799);
        persistence.insertFQNMap(fqn, host, null);
        persistence.insertFQNMap(fqn+".plug1.value1", host, "plug1-value1");
        
        System.out.println("FQN: "+persistence.searchFQN(host,"plug1-value1"));
        System.out.println("FQN: "+persistence.searchFQN(host,null));
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
