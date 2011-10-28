CREATE TABLE nodedirectory (
  internalId serial PRIMARY KEY,
  fqn varchar(255) default NULL,
  internalNodeId bigint NOT NULL,
  status int NOT NULL,
  fechaCreacion timestamp default NULL,
  fechaBorrado timestamp default NULL,
  tipo int NOT NULL,
  parent_internalId bigint default NULL
);

CREATE TABLE monitoringsample (
 id bigserial PRIMARY KEY,
 datetime timestamp default NULL,
 day smallint NOT NULL,
 month smallint NOT NULL,
 year smallint NOT NULL,
 hour smallint NOT NULL,
 minute smallint NOT NULL,
 value varchar(255) default NULL,
 measure_type varchar(30) default NULL,
 unit varchar(30) default NULL,
 associatedObject_internalId bigint default NULL references nodedirectory(internalId)

) ;
create index measure_type_index on monitoringsample (measure_type);

-- CONSTRAINT `FKD30AF432786DFD15` FOREIGN KEY (`associatedObject_internalId`) REFERENCES `nodedirectory` (`internalId`)
create table fqn ( fqn varchar(255) PRIMARY KEY, host varchar(64) not null, plugin varchar(64) default null);
create index hostplug_index ON fqn (host,plugin);

