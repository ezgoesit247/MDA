USE [aifmda]
GO


/****** Object:  Table [dbo].[TelemetryData]    Script Date: 7/15/2018 9:14:18 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TelemetryData](
	[TelemetryNdx] [numeric](19, 0) IDENTITY(549334,1) NOT NULL,
	[VariableNdx] [int] NULL,
	[Value] [decimal](18, 6) NULL,
	[Timestamp] [datetime] NULL,
	[ReelNdx] [int] NOT NULL,
	[ReelIdentifier] [nvarchar](50) NULL,
	[SendStatus] [nvarchar](20) NULL,
 CONSTRAINT [PK_TelemetryData] PRIMARY KEY CLUSTERED 
(
	[TelemetryNdx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[TelemetryData] ADD  CONSTRAINT [DF_TelemetryData_SendStatus]  DEFAULT (N'NEW') FOR [SendStatus]
GO

ALTER TABLE [dbo].[TelemetryData]  WITH CHECK ADD  CONSTRAINT [FKtncamfms2jkccysncbd37najw] FOREIGN KEY([ReelNdx])
REFERENCES [dbo].[Reel] ([ReelNdx])
GO

ALTER TABLE [dbo].[TelemetryData] CHECK CONSTRAINT [FKtncamfms2jkccysncbd37najw]
GO


