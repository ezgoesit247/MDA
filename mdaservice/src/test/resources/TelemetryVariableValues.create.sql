USE [aifmda]
GO

/****** Object:  Table [dbo].[TelemetryVariableValues]    Script Date: 8/3/2018 10:54:42 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TelemetryVariableValues](
	[telemetry_telemetryndx] [numeric](19, 0) NOT NULL,
	[variables_telemetryvaluendx] [numeric](19, 0) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[telemetry_telemetryndx] ASC,
	[variables_telemetryvaluendx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_jl7yuul5r44s0ywcdx6ol93hw] UNIQUE NONCLUSTERED 
(
	[variables_telemetryvaluendx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_ne3udim9oihoy7hnmof3huv8q] UNIQUE NONCLUSTERED 
(
	[variables_telemetryvaluendx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[TelemetryVariableValues]  WITH CHECK ADD  CONSTRAINT [FKn4rqcv97mf5n391bpam60ci8h] FOREIGN KEY([variables_telemetryvaluendx])
REFERENCES [dbo].[TelemetryValue] ([TelemetryValueNdx])
GO

ALTER TABLE [dbo].[TelemetryVariableValues] CHECK CONSTRAINT [FKn4rqcv97mf5n391bpam60ci8h]
GO

ALTER TABLE [dbo].[TelemetryVariableValues]  WITH CHECK ADD  CONSTRAINT [FKpx3q623caq068gttdbkx5am3t] FOREIGN KEY([telemetry_telemetryndx])
REFERENCES [dbo].[TelemetryHeader] ([TelemetryNdx])
GO

ALTER TABLE [dbo].[TelemetryVariableValues] CHECK CONSTRAINT [FKpx3q623caq068gttdbkx5am3t]
GO

