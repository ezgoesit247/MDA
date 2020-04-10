USE [aifmda]
GO

/****** Object:  Table [dbo].[FFTDetails]    Script Date: 8/3/2018 10:52:39 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[FFTDetails](
	[FFTDetailsNdx] [numeric](19, 0) IDENTITY(1000000,1) NOT NULL,
	[WorkOrderIdentifier] [nvarchar](50) NULL,
	[ReelNdx] [int] NOT NULL,
	[WorkOrderNdx] [int] NOT NULL,
	[D1Freq] [numeric](19, 0) NULL,
	[D1Amp] [int] NULL,
	[D3Freq] [numeric](19, 0) NULL,
	[D3Amp] [int] NULL,
	[CcoldFreq] [numeric](19, 0) NULL,
	[CcoldAmp] [int] NULL,
	[StartTime] [datetime] NULL,
	[EndTime] [datetime] NULL,
	[Timestamp] [datetime] NULL,
 CONSTRAINT [PK_FFTDetails] PRIMARY KEY CLUSTERED 
(
	[FFTDetailsNdx] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[FFTDetails]  WITH CHECK ADD  CONSTRAINT [FKkc2epg0d4d5b92x8jbuhhagc] FOREIGN KEY([WorkOrderNdx])
REFERENCES [dbo].[WorkOrder] ([WorkOrderNdx])
GO

ALTER TABLE [dbo].[FFTDetails] CHECK CONSTRAINT [FKkc2epg0d4d5b92x8jbuhhagc]
GO

ALTER TABLE [dbo].[FFTDetails]  WITH CHECK ADD  CONSTRAINT [FKop2ajlqvgtxv3f29ecamdykf0] FOREIGN KEY([ReelNdx])
REFERENCES [dbo].[Reel] ([ReelNdx])
GO

ALTER TABLE [dbo].[FFTDetails] CHECK CONSTRAINT [FKop2ajlqvgtxv3f29ecamdykf0]
GO

