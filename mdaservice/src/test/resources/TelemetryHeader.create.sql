USE [aifmda]
GO

/****** Object:  Table [dbo].[TelemetryHeader]    Script Date: 8/3/2018 10:53:21 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TelemetryHeader](
	[TelemetryNdx] [numeric](19, 0) IDENTITY(549334,1) NOT NULL,
	[VariableNdx] [int] NULL,
	[Value] [decimal](18, 6) NULL,
	[Timestamp] [datetime] NULL,
	[ReelNdx] [int] NOT NULL,
	[ReelIdentifier] [nvarchar](50) NULL,
	[SendStatus] [nvarchar](20) NULL,
 CONSTRAINT [PK_TelemetryHeader] PRIMARY KEY CLUSTERED 
(
	[TelemetryNdx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[TelemetryHeader] ADD  CONSTRAINT [DF_TelemetryHeader_SendStatus]  DEFAULT (N'NEW') FOR [SendStatus]
GO

ALTER TABLE [dbo].[TelemetryHeader]  WITH CHECK ADD  CONSTRAINT [FKmv5cb7xppcl0venu54qp836y0] FOREIGN KEY([ReelNdx])
REFERENCES [dbo].[Reel] ([ReelNdx])
GO

ALTER TABLE [dbo].[TelemetryHeader] CHECK CONSTRAINT [FKmv5cb7xppcl0venu54qp836y0]
GO

