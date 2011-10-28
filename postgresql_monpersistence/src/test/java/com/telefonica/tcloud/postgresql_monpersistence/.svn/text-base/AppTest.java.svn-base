package com.telefonica.tcloud.postgresql_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    private boolean enabled=false;
    public AppTest( String testName ) throws SQLException
    {
        super( testName );
        if (!enabled) return;
        LinkedHashMap<String,String[]> config=new LinkedHashMap<String,String[]>();
        String url[]={"jdbc:postgresql://172.30.5.15/monitoring"};
        String user[]={"claudia"};
        String password[]={"ClaudiaPass"};
        config.put("pgsqlurl",url);
        config.put("pgsqluser",user);
        config.put("pgsqlpassword",password);
                
        MonPersistence persistence= MonPersistenceFactory.getPersistence(
         "com.telefonica.tcloud.postgresql_monpersistence.PostgreSQL_MonPersistenceFactory"
          ,config);
        String host="collector";
        String fqn="es.tid.customers.cc1.services.monitoring.vees.collector.replicas.1";
        //persistence.insertFQNMap(fqn, host, null);
        //persistence.insertFQNMap(fqn+".plug1.value1", host, "plug1-value1");
        persistence.insertData(new Date(),fqn,"cpu","cycles",345799);
        persistence.insertData(new Date(), fqn+".net.eth0", "iosent", "packages", 3000);
        persistence.insertData(new Date(), persistence.searchFQN(host,"plug1-value1"),"measure","units",30);
        
        System.out.println("FQN: "+persistence.searchFQN(host,"plug1-value1"));
        System.out.println("FQN: "+persistence.searchFQN(host,null));
        //persistence.shutdown();
        
        try {
            Thread.sleep(100000);
            //System.out.println("ID: "+((PostgreSQL_MonPersistence)persistence).getAssociatedObjectId(fqn+".plug2.value3"));
        } catch (InterruptedException ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
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
