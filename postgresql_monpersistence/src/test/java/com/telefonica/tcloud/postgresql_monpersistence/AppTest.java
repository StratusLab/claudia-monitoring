package com.telefonica.tcloud.postgresql_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import com.telefonica.tcloud.jdbc_monpersistence.JDBC_MonPersistence;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest   extends TestCase
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
        Properties config=new Properties();
        config.setProperty("pgsqlurl","jdbc:postgresql://localhost/monitoring");
        config.setProperty("pgsqluser","claudia");
        config.setProperty("pgsqlpassword","ClaudiaPass");
                
        MonPersistence pers= MonPersistenceFactory.getPersistence(
         "com.telefonica.tcloud.postgresql_monpersistence.PostgreSQL_MonPersistenceFactory"
          ,config);
        
        JDBC_MonPersistence persistence=(JDBC_MonPersistence)pers;
        
        //1º Comprobamos cuantos elementos hay en la base de datos.
        long nInicial=persistence.count("");
        System.out.println("La base de datos tiene:"+nInicial+" elementos");
        persistence.purge("");
        long n0=persistence.count("");
        assert(n0==0);
        System.out.println("La base de datos tiene:"+n0+" elementos despues de purge");

        // Primer insert;
        String [] hosts={"collector", "desplegator"};
        
        String [] fqns={"es.tid.customers.cc1.services.monitoring.vees.collector.replicas.2",
            "com.telefonica.servers.cc2.services.monitoring.vees.desplegator.replicas.2" };
        String [] testNames={"cpu", "mem", "hd_speed", "hd_capacity","bd_cuota"};
        String [] plugs={"", ".plug1.value1"};
        String [] testUnits={"hertz","bips","cycles","wat","Mb","percent"};
        
        persistence.insertFQNMap(fqns[0], hosts[0], null);
        persistence.insertFQNMap(fqns[0]+".plug1.value1", hosts[0], "plug1-value1");
        persistence.insertFQNMap(fqns[1], hosts[1], null);
        persistence.insertFQNMap(fqns[1]+".plug1.value1", hosts[1], "plug1-value1");
                
        n0=persistence.count("");
        assert(n0==4);
        System.out.println("La base de datos tiene:"+n0+" elementos despues de insert");
        int np=0;
        // Metemos datos...
        for (int i=0; i<20000; i++) {
            String fqn=fqns[(int)Math.round(Math.random())];
            String host=hosts[(int)Math.round(Math.random())];
            String plug=plugs[(int)Math.round(Math.random())];
            String testUnit=testUnits[(int) (Math.random() * testUnits.length) ];
            String testWhat=testNames[(int) (Math.random() * testNames.length) ];
            try {            
                persistence.insertData(new Date(), fqn, testWhat, testUnit, (long)(Math.random()*100000l));                   
            } catch (SQLException sqle) {
                System.out.println("ErrCode: " + sqle.getErrorCode() + " Perdidas: " + (++np));
                try { Thread.sleep(3000); } catch (Exception e) {}
            }
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
