/*
  System              : GENESIS Business Library
  Sub-System          : DbToDta SQL generator
  Version             : 1.0
  Copyright           : (c) GENESIS

  Function : MSSQL Script example to generate the UpdateQueue and its basic procedures.
  Note: Change "dbo" for actual database name.
*/

CREATE TABLE UPDATEQUEUE
     (
        ID int IDENTITY(1,1) PRIMARY KEY NOT NULL  ,
        TABLE_NAME        VARCHAR(20)           NOT NULL   ,
        UPDATE_TYPE         VARCHAR(40)           NOT NULL   ,
        RECORD_REFERENCE      VARCHAR(40)            NOT NULL   ,
        RECORD_TIMESTAMP       DATETIME              NOT NULL
     )
GO

CREATE PROCEDURE queryUpdateQueue
	@TableId varchar(50),
    @Timestamp datetime
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	SELECT * FROM dbo."UPDATEQUEUE" WHERE TABLE_NAME = @TableId AND RECORD_TIMESTAMP > @Timestamp ORDER BY RECORD_TIMESTAMP ASC;

END
GO


CREATE PROCEDURE clearUpdateQueue
	@TableId varchar(50),
    @Timestamp datetime
AS
BEGIN
	SET NOCOUNT ON;

	DELETE FROM dbo."UPDATEQUEUE" WHERE TABLE_NAME = @TableId AND RECORD_TIMESTAMP <= @Timestamp;

END
GO