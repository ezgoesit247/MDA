USE [aifmda]
GO

/****** Object:  Table [dbo].[TelemetryMetadata]    Script Date: 8/3/2018 10:53:35 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TelemetryMetadata](
	[VariableNdx] [int] IDENTITY(1,5001) NOT NULL,
	[VariableName] [nvarchar](50) NOT NULL,
	[VariableType] [nvarchar](20) NOT NULL,
	[VariableShortName] [nvarchar](20) NOT NULL,
	[LineMachine] [nvarchar](50) NULL,
	[Equipment] [nvarchar](20) NULL,
 CONSTRAINT [PK_TelemetryMetadata] PRIMARY KEY CLUSTERED 
(
	[VariableNdx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_s3onhlge9t7v39yukxmcu8645] UNIQUE NONCLUSTERED 
(
	[VariableName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_sg5ndqlfq1h00dcd0f8eqrw34] UNIQUE NONCLUSTERED 
(
	[VariableShortName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[TelemetryMetadata] ADD  CONSTRAINT [DF_TelemetryMetadata_VariableType]  DEFAULT (N'DEFAULT') FOR [VariableType]
GO

