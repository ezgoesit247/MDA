CREATE TABLE `aifmda`.`telemetrydata`( # TelemetryData
  `TelemetryNdx` BIGINT(20) NOT NULL AUTO_INCREMENT
,  `VariableNdx` int NULL
,  `Value` decimal(18, 6) NULL
,  `Timestamp` datetime NULL
,  `ReelNdx` int NOT NULL
,  `ReelIdentifier` nvarchar(50) NULL
,  `SendStatus` nvarchar(20) NULL
,PRIMARY KEY (`TelemetryNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `aifmda`.`telemetryvalue`( # TelemetryValue
  `TelemetryValueNdx` BIGINT(20) NOT NULL AUTO_INCREMENT
 , `VariableNdx` int NOT NULL
 , `Value` decimal(18, 6) NOT NULL
,PRIMARY KEY (`TelemetryValueNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  

CREATE TABLE `aifmda`.`workorder` ( # WorkOrder
`WorkOrderNdx` int unsigned NOT NULL AUTO_INCREMENT
,`WorkOrderIdentifier` nvarchar(50) NOT NULL UNIQUE
,`Timestamp` DATETIME NOT NULL
,PRIMARY KEY (`WorkOrderNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `aifmda`.`telemetryheader` ( # TelemetryHeader
`TelemetryNdx` BIGINT(20) NOT NULL AUTO_INCREMENT
,`VariableNdx` int NULL
,`Value` decimal(18,6) NULL
,`Timestamp` DATETIME NULL
,`ReelNdx` int NOT NULL
,`ReelIdentifier` nvarchar(50) NULL
,`SendStatus` nvarchar(50) NULL
,PRIMARY KEY (`TelemetryNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `aifmda`.`telemetrymetadata` ( # TelemetryMetadata
`VariableNdx` int unsigned NOT NULL AUTO_INCREMENT
,`VariableName` nvarchar(50) NOT NULL UNIQUE
,`VariableType` nvarchar(50) NOT NULL
,`VariableShortName` nvarchar(50) NOT NULL UNIQUE
,`LineMachine` nvarchar(50) NULL
,`Equipment` nvarchar(50) NULL
,PRIMARY KEY (`VariableNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `aifmda`.`telemetryvariablevalues` ( # TelemetryVariableValues
`telemetry_telemetryndx` BIGINT(20) NOT NULL
,`variables_telemetryvaluendx` BIGINT(20) NOT NULL UNIQUE
,PRIMARY KEY (`telemetry_telemetryndx`,`variables_telemetryvaluendx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `aifmda`.`reel`( # Reel
  `ReelNdx` int unsigned NOT NULL AUTO_INCREMENT
,  `ReelIdentifier` nvarchar(50) NOT NULL UNIQUE
,  `WorkOrderNdx` int NOT NULL
,  `WorkOrderIdentifier` nvarchar(50) NULL
,  `Type` nvarchar(20) NOT NULL
,PRIMARY KEY (`ReelNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `aifmda`.`fftdetails`( # FFTDetails
  `FFTDetailsNdx` BIGINT(20) NOT NULL
,  `WorkOrderIdentifier` nvarchar(50) NULL
,  `ReelNdx` int NOT NULL
,  `WorkOrderNdx` int NOT NULL
,  `D1Freq` decimal(19, 0) NULL
,  `D1Amp` int NULL
,  `D3Freq` decimal(19, 0) NULL
,  `D3Amp` int NULL
,  `CcoldFreq` decimal(19, 0) NULL
,  `CcoldAmp` int NULL
,  `StartTime` datetime NULL
,  `EndTime` datetime NULL
,  `Timestamp` datetime NULL
,PRIMARY KEY (`FFTDetailsNdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `WorkOrder` (`WorkOrderNdx`, `WorkOrderIdentifier`, `Timestamp`) VALUES
 (1, 'UT-WO-1','2018-01-01T11:22:33')
,(2, 'UT-WO-2','2018-01-01T11:22:33')
,(3, 'UT-WO-3','2018-01-01T23:44:55')
,(4, 'UT-WO-4','2018-01-01T22:43:00')
,(5, 'UT-WO-5','2018-01-01T11:22:00');




DELETE FROM `aifmda`.`fftdetails`;
DELETE FROM `aifmda`.`reel`;
DELETE FROM `aifmda`.`telemetryvariablevalues`;
DELETE FROM `aifmda`.`telemetrymetadata`;
DELETE FROM `aifmda`.`telemetryheader`;
DELETE FROM `aifmda`.`workorder`;
DELETE FROM `aifmda`.`telemetryvalue`;
DELETE FROM `aifmda`.`telemetrydata`;


DROP TABLE if exists fftdetails;
DROP TABLE if exists reel;
DROP TABLE if exists telemetrydata;
DROP TABLE if exists telemetrymetadata;
DROP TABLE if exists workorder  ;
DROP TABLE if exists telemetryvariablevalues;
DROP TABLE if exists telemetryvalue;
DROP TABLE if exists telemetryheader;


DROP TABLE if exists telemetryheader;
DROP TABLE if exists telemetryvalue;
DROP TABLE if exists telemetryvariablevalues;
DROP TABLE if exists workorder  ;
DROP TABLE if exists telemetrymetadata;
DROP TABLE if exists telemetrydata;
DROP TABLE if exists reel;
DROP TABLE if exists fftdetails;





INSERT INTO `WorkOrder` (`WorkOrderNdx`, `WorkOrderIdentifier`, `Timestamp`) VALUES
 (1, 'UT-WO-1','2018-01-01T11:22:33')
,(2, 'UT-WO-2','2018-01-01T11:22:33')
,(3, 'UT-WO-3','2018-01-01T23:44:55')
,(4, 'UT-WO-4','2018-01-01T22:43:00')
,(5, 'UT-WO-5','2018-01-01T11:22:00');


INSERT INTO `Reel` (`ReelNdx`,`ReelIdentifier`,`WorkOrderNdx`,`WorkOrderIdentifier`,`Type`) VALUES
 (1,'UT-REEL-1',2,null,'TAPER')
,(2,'UT-REEL-2',2,null,'TAPER')
,(3,'UT-EXT-REEL-3',3,null,'EXTRUDER')
,(4,'UT-TPR-REEL-4',4,null,'TAPER');

INSERT INTO `TelemetryMetadata` (`VariableNdx`,`VariableName`,`VariableShortName`,`VariableType`) VALUES 
 (1,'UT-Variable1','UTVAR1','DEFAULT')
,(2,'UT-Variable2','UTVAR2','DEFAULT')
,(3,'UT-Variable3','UTVAR3','DEFAULT')
,(4,'UT-Variable4','UTVAR4','DEFAULT')
,(1000,'in_oven_heater_tmp','VAR1000','DEFAULT')
,(1001,'in_capstan_tension','VAR1001','DEFAULT')
,(1002,'in_taper1_rpm','VAR1002','DEFAULT')
,(1003,'in_taper2_','VAR1003','DEFAULT')
,(1004,'in_line_speed','VAR1004','DEFAULT')
,(1005,'in_taper2_rpm','VAR1005','DEFAULT')
,(2000,'oven_heater_tmp','VAR2000','DEFAULT')
,(2001,'capstan_tension','VAR2001','DEFAULT')
,(2002,'taper1_rpm','VAR2002','DEFAULT')
,(2003,'taper2_','VAR2003','DEFAULT')
,(2004,'line_speed','VAR2004','DEFAULT')
,(2005,'taper2_rpm','VAR2005','DEFAULT')
,(2006,'pred_impedance','VAR2006','DEFAULT')
,(2007,'pred_insertion_loss','VAR2007','DEFAULT')
,(2008,'oven_voltage','VAR2008','DEFAULT')
,(2009,'oven_current','VAR2009','DEFAULT')
,(2010,'oven_watts','VAR2010','DEFAULT')
,(2011,'oven_heater','VAR2011','DEFAULT')
,(2012,'oven_load','VAR2012','DEFAULT')
,(2013,'oven_inlet_tmp','VAR2013','DEFAULT')
,(2014,'oven_exit_tmp','VAR2014','DEFAULT')
,(2015,'chiller_exit_tmp','VAR2015','DEFAULT')
,(2016,'chiller_mix','VAR2016','DEFAULT')
,(2017,'chiller_inlet_tmp','VAR2017','DEFAULT')
,(2018,'chiller_wash_tmp','VAR2018','DEFAULT')
,(2019,'oven_load2','VAR2019','DEFAULT')
,(2020,'room_humidity','VAR2020','DEFAULT')
,(2021,'room_tmp','VAR2021','DEFAULT')
,(2022,'payoff_run_length','VAR2022','DEFAULT')
,(2023,'taper1_inlet_tension','VAR2023','DEFAULT')
,(2024,'taper1_dispenser_diameter','VAR2024','DEFAULT')
,(2025,'taper1_reel_capacity','VAR2025','DEFAULT')
,(2026,'taper1_dispenser','VAR2026','DEFAULT')
,(2027,'taper2_reel_capacity','VAR2027','DEFAULT')
,(2028,'taper1_outlet_tension','VAR2028','DEFAULT')
,(2029,'taper2_inlet_tension','VAR2029','DEFAULT')
,(2030,'taper2_dispenser1','VAR2030','DEFAULT')
,(2031,'taper2_dispenser2','VAR2031','DEFAULT')
,(2032,'capstan_air','VAR2032','DEFAULT')
,(2033,'payoff1_tension','VAR2033','DEFAULT')
,(2034,'payoff2_tension','VAR2034','DEFAULT')
,(2035,'drain_tension','VAR2035','DEFAULT')
,(2036,'taper1_dispenser_tension','VAR2036','DEFAULT')
,(2037,'taper2_dispenser_tension','VAR2037','DEFAULT')
,(2038,'takeup_tension','VAR2038','DEFAULT');

INSERT INTO `TelemetryHeader` (`TelemetryNdx`,`VariableNdx`,`Value`,`Timestamp`,`ReelNdx`,`SendStatus`,`ReelIdentifier`) VALUES
 (2,2,999.1234559,'2018-07-04T00:00:00',2,'NOSEND','UT-REEL-2') 
,(3,2,533.1234559,'2018-07-04T00:00:05',2,'NOSEND','UT-REEL-2') 
,(4,2,780.1234559,'2018-07-04T00:00:10',2,'NOSEND','UT-REEL-2')
,(5,2,679.1234559,'2018-07-04T00:00:15',2,'NOSEND','UT-REEL-2')
,(6,2,788.1234559,'2018-07-04T00:00:20',2,'NOSEND','UT-REEL-2')
,(7,2,897.1234559,'2018-07-04T00:00:25',2,'NOSEND','UT-REEL-2')
,(8,2,999.1234559,'2018-07-04T00:00:00',1,'NOSEND','UT-REEL-1')
,(9,2,533.1234559,'2018-07-04T00:00:05',1,'NOSEND','UT-REEL-1');
