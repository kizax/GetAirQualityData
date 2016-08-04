DROP DATABASE IF EXISTS busdb;

CREATE DATABASE busdb CHARACTER SET utf8;

USE busdb;

DROP TABLE IF EXISTS busdata;

CREATE TABLE busdata (
  ID int NOT NULL AUTO_INCREMENT,
  datatime datetime,
  cartype INT,
  providerid float(10,1),
  providername VARCHAR(10),
  carid INT,
  
  busid VARCHAR(10),
  dutystatus INT,
  busstatus INT,
  routeid float(10,1),
  routename VARCHAR(25),

  goback INT,
  longitude DECIMAL(10,6),
  latitude DECIMAL(10,6),
  speed DECIMAL(10,6),
  azimuth DECIMAL(10,6),
  
  stopid INT,
  stopLocationName VARCHAR(30),
  PRIMARY KEY (ID)
)ENGINE = MyISAM ;

ALTER TABLE `busdb`.`busdata` 
ADD INDEX `datatime` USING BTREE (`datatime` ASC),
ADD INDEX `providerid` USING BTREE (`providerid` ASC),
ADD INDEX `providername` USING BTREE (`providername` ASC),
ADD INDEX `carid` USING BTREE (`carid` ASC),
ADD INDEX `busid` USING BTREE (`busid` ASC),
ADD INDEX `routeid` USING BTREE (`routeid` ASC),
ADD INDEX `routename` USING BTREE (`routename` ASC),
ADD INDEX `longitude` USING BTREE (`longitude` ASC),
ADD INDEX `latitude` USING BTREE (`latitude` ASC),
ADD INDEX `speed` USING BTREE (`speed` ASC),
ADD INDEX `azimuth` USING BTREE (`azimuth` ASC),
ADD INDEX `stopid` USING BTREE (`stopid` ASC),
ADD INDEX `stopLocationName` USING BTREE (`stopLocationName` ASC),
ADD INDEX `goback` USING BTREE (`goback` ASC);


INSERT INTO busdata (datatime,cartype,providerid,providername,carid,busid,dutystatus,busstatus,routeid,routename,goback,longitude,latitude,speed,azimuth,stopid,stopLocationName) VALUES ('2016/2/3 14:13:44',1,1100,'大南汽車',222233972,'078-FR',1,0,157863,'266(北投-市府)',0,121.524755,25.084855,11,185.2,41527,'捷運劍潭站(基河)');
INSERT INTO busdata (datatime,cartype,providerid,providername,carid,busid,dutystatus,busstatus,routeid,routename,goback,longitude,latitude,speed,azimuth,stopid,stopLocationName) VALUES ('2016/2/3 14:13:45',1,1100,'大南汽車',222233972,'078-FR',1,0,157863,'266(北投-市府)',0,121.524755,25.084855,11,185.2,41527,NULL);