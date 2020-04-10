USE [aifmda]
GO

/****** Object:  Table [dbo].[TelemetryValue]    Script Date: 8/3/2018 10:54:18 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TelemetryValue](
	[TelemetryValueNdx] [numeric](19, 0) IDENTITY(1049334,1) NOT NULL,
	[VariableNdx] [int] NOT NULL,
	[Value] [decimal](18, 6) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[TelemetryValueNdx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[TelemetryValue] ADD  DEFAULT ((0)) FOR [Value]
GO

ALTER TABLE [dbo].[TelemetryValue]  WITH CHECK ADD  CONSTRAINT [FKn3use9h0raqt943ef9a2aeh98] FOREIGN KEY([VariableNdx])
REFERENCES [dbo].[TelemetryMetadata] ([VariableNdx])
GO

ALTER TABLE [dbo].[TelemetryValue] CHECK CONSTRAINT [FKn3use9h0raqt943ef9a2aeh98]
GO

