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

