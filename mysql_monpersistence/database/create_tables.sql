DROP TABLE IF EXISTS `nodedirectory`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nodedirectory` (
  `internalId` bigint(20) NOT NULL auto_increment,
  `fqn` varchar(255) default NULL,
  `internalNodeId` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `fechaCreacion` datetime default NULL,
  `fechaBorrado` datetime default NULL,
  `tipo` int(11) NOT NULL,
  `parent_internalId` bigint(20) default NULL,
  PRIMARY KEY  (`internalId`),
  KEY `FKCA54626B3CC47F10` (`parent_internalId`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;



SET character_set_client = utf8;
CREATE TABLE `monitoringsample` (
 `id` bigint(20) NOT NULL auto_increment,
 `datetime` datetime default NULL,
 `day` int(11) NOT NULL,
 `month` int(11) NOT NULL,
 `year` int(11) NOT NULL,
 `hour` int(11) NOT NULL,
 `minute` int(11) NOT NULL,
 `value` varchar(255) default NULL,
 `measure_type` varchar(30) default NULL,
 `unit` varchar(30) default NULL,
 `associatedObject_internalId` bigint(20) default NULL,
 PRIMARY KEY  (`id`),
 KEY `FKD30AF432786DFD15` (`associatedObject_internalId`),
 INDEX `measure_type_index` (`measure_type`),
 CONSTRAINT `FKD30AF432786DFD15` FOREIGN KEY (`associatedObject_internalId`) REFERENCES `nodedirectory` (`internalId`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `fqn` ( fqn varchar(255) not null, host varchar(64) not null, plugin varchar(64) default null, primary key (fqn), INDEX in1 (host,plugin)) ENGINE=InnoDB ;

