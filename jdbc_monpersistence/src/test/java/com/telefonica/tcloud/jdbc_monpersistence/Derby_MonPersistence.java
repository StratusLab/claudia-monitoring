/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.telefonica.tcloud.jdbc_monpersistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author trabajo
 */
public class Derby_MonPersistence extends JDBC_MonPersistence  {
    
    private Connection con;
        
    private final String strCreateNodeDirectory="CREATE TABLE nodedirectory ("
            + "  internalId bigINT NOT NULL GENERATED ALWAYS AS IDENTITY,"
            + "  fqn varchar(255) default NULL,"
            + "  internalNodeId int NOT NULL,"
            + "  status int NOT NULL,"
            + "  fechaCreacion timestamp default NULL,"
            + "  fechaBorrado timestamp default NULL,"
            + "  tipo int NOT NULL,"
            + "  parent_internalId int default NULL)";
    public final String strCreateMonitoringSample="CREATE TABLE monitoringsample ("  
            + "id  bigINT NOT NULL GENERATED ALWAYS AS IDENTITY,"
            + " datetime timestamp default NULL,"
            + " day int NOT NULL,"
            + " month int NOT NULL, \n"
            + " d_year int NOT NULL,"
            + " d_hour int NOT NULL,"
            + " d_minute int NOT NULL,"
            + " value varchar(255) default NULL,"
            + " measure_type varchar(30) default NULL,"
            + " unit varchar(30) default NULL,"
            + " associatedObject_internalId  bigint default NULL)"; 
    private String strCreateFQN= "create table fqn ("       
            + "fqn varchar(255) not null primary key, "
            + "host varchar(64) not null, "
            + "plugin varchar(64) default null)";
    public Derby_MonPersistence(String url, String user, String password) 
            throws ClassNotFoundException, SQLException 
    {            
        super (url, user, password,"org.apache.derby.jdbc.EmbeddedDriver");
        //prepareConnection();
        con=getConnection();            
        createTables();
        populateTables();
        strInsertMeasure= "INSERT INTO monitoringsample (datetime,"
               +"day,month,d_year,d_hour,d_minute,value,measure_type,unit,"+
               "associatedObject_internalId) VALUES (?,?,?,?,?,?,?,?,?,?)";
        prepareConnection();
    }
    
    private void populateTables() throws SQLException {
        con.createStatement().execute("INSERT INTO nodedirectory "
                + "(fqn,internalNodeId,status,fechaCreacion,tipo) "
                + "values ('es.tid.customers.cc1.services.monitoring.vees.collector.replicas.1', "
                + "2,1,'" + ( new Timestamp(new Date().getTime()) ). toString()  + "',4)");
        System.err.println("Hecho inser 1");
        con.createStatement().execute("INSERT INTO nodedirectory "
                + "(fqn,internalNodeId,status,fechaCreacion,tipo) "
                + "values ('es.tid.customers.cc1.services.demo.vees.vee1.replicas."
                + "1', 3,1,'"+ ( new Timestamp(new Date().getTime()) ). toString() +"',4)");
        System.err.println("Hecho inser 2");
        con.createStatement().execute("INSERT INTO nodedirectory "
                + "(fqn,internalNodeId,status,fechaCreacion,tipo) "
                + "values ('tid.customers.cc1.services.demo.vees.vee1.replicas.1', "
                + "3,1, '"+ ( new Timestamp(new Date().getTime()) ). toString() +"',4)");
        con.createStatement().execute("INSERT INTO fqn (fqn,host,plugin) "
                + "values ('es.tid.customers.cc1.services.monitoring.vees.collector.replicas.1', "
                + "'collector',NULL)");
        con.createStatement().execute("INSERT INTO fqn (fqn,host,plugin) "
                + "values ('es.tid.customers.cc1.services.demo.vees.vee1.replicas.1',"
                + "'monitorized2',NULL)");
    }
    
    /* */
    private void createTables() throws SQLException {
        try {
            con.createStatement().execute("drop table NODEDIRECTORY");
        } catch (Exception e) {}
        try {
            con.createStatement().execute("drop table MONITORINGSAMPLE");
        } catch (Exception e) {}
        try {
            con.createStatement().execute("drop table FQN");
        } catch (Exception e) {}
        
            con.createStatement().execute(strCreateNodeDirectory);            
            System.out.println("Creado NODEDIRECTORY");
            con.createStatement().execute(strCreateMonitoringSample);            
            System.out.println("Creado MONITORINGSAMPLE");
            con.createStatement().execute(strCreateFQN);
            System.out.println("Creado FQN");                        
    }
        
}
