

truncate table aifmda.telemetrymetadata;
truncate table aifmda.telemetryvariablevalues;
truncate table aifmda.fftdetails;
truncate table aifmda.reel;
truncate table aifmda.workorder;

delete from aifmda.telemetryvalue;
delete from aifmda.telemetryheader;


INSERT INTO `workorder` (`WorkOrderNdx`, `WorkOrderIdentifier`, `Timestamp`) VALUES
 (1, 'UT-WO-1','2018-01-01T11:22:33')
,(2, 'UT-WO-2','2018-01-01T11:22:33')
,(3, 'UT-WO-3','2018-01-01T23:44:55')
,(4, 'UT-WO-4','2018-01-01T22:43:00')
,(5, 'UT-WO-5','2018-01-01T11:22:00');