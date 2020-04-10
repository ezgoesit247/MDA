USE [aifmda]
GO

--  1) call WorkOrder.create.sql
--  2) call Reel.create.sql
--  3) call TelemetryMetadata.create.sql
--  4) call TelemetryDetail.create.sql
--  4) call TelemetryHeader.create.sql
--  5) call FFTDetails.create.sql


SET IDENTITY_INSERT [WorkOrder] ON
INSERT INTO [WorkOrder] ([WorkOrderNdx], [WorkOrderIdentifier], [Timestamp]) VALUES
 (1, 'UT-WO-1','2018-01-01T11:22:33')
,(2, 'UT-WO-2','2018-01-01T11:22:33')
,(3, 'UT-WO-3','2018-01-01T23:44:55')
,(4, 'UT-WO-4','2018-01-01T22:43:00')
,(5, 'UT-WO-5','2018-01-01T11:22:00')
SET IDENTITY_INSERT [WorkOrder] OFF
GO

SET IDENTITY_INSERT [Reel] ON
INSERT INTO [dbo].[Reel] ([ReelNdx],[ReelIdentifier],[WorkOrderNdx],[WorkOrderIdentifier],[Type]) VALUES
 (1,'UT-REEL-1',2,null,'TAPER')
,(2,'UT-REEL-2',2,null,'TAPER')
,(3,'UT-EXT-REEL-3',3,null,'EXTRUDER')
,(4,'UT-TPR-REEL-4',4,null,'TAPER')
SET IDENTITY_INSERT [Reel] OFF
GO

SET IDENTITY_INSERT [TelemetryMetadata] ON
INSERT INTO [dbo].[TelemetryMetadata] ([VariableNdx],[VariableName],[VariableShortName],[VariableType]) VALUES 
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
,(2038,'takeup_tension','VAR2038','DEFAULT')
SET IDENTITY_INSERT [TelemetryMetadata] OFF
GO

SET IDENTITY_INSERT [TelemetryHeader] ON
INSERT INTO [dbo].[TelemetryHeader] ([TelemetryNdx],[VariableNdx],[Value],[Timestamp],[ReelNdx],[SendStatus],[ReelIdentifier]) VALUES
 (2,2,999.1234559,'2018-07-04T00:00:00',2,'NOSEND','UT-REEL-2') 
,(3,2,533.1234559,'2018-07-04T00:00:05',2,'NOSEND','UT-REEL-2') 
,(4,2,780.1234559,'2018-07-04T00:00:10',2,'NOSEND','UT-REEL-2')
,(5,2,679.1234559,'2018-07-04T00:00:15',2,'NOSEND','UT-REEL-2')
,(6,2,788.1234559,'2018-07-04T00:00:20',2,'NOSEND','UT-REEL-2')
,(7,2,897.1234559,'2018-07-04T00:00:25',2,'NOSEND','UT-REEL-2')
,(8,2,999.1234559,'2018-07-04T00:00:00',1,'NOSEND','UT-REEL-1')
,(9,2,533.1234559,'2018-07-04T00:00:05',1,'NOSEND','UT-REEL-1')
SET IDENTITY_INSERT [TelemetryHeader] OFF
GO

--
--
--DECLARE @NDX INT
--DECLARE @WO NVARCHAR(50)
--DECLARE @REEL NVARCHAR(50)
--
---- any names will as long as their unique to the table
--SELECT @WO = 'WRKODR-20180715.0001'
--SELECT @REEL = 'EXT-12345'
--
---- make a wo
--
--INSERT INTO [dbo].[WorkOrder]
--           ([WorkOrderIdentifier]
--           ,[Timestamp])
--     VALUES
--           (@WO
--           ,CURRENT_TIMESTAMP)
--
--
--SELECT @NDX = SCOPE_IDENTITY()
--
---- make a ree;
--
--INSERT INTO [dbo].[Reel]
--           ([ReelIdentifier]
--           ,[WorkOrderNdx]
--           ,[WorkOrderIdentifier]
--           ,[Type])
--     VALUES
--           (@REEL
--           ,@NDX
--           ,@WO
--           ,'EXTRUDER')
--GO
