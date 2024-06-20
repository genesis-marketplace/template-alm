/*
  System              : GENESIS Business Library
  Sub-System          : DbToDta SQL generator
  Version             : 1.0
  Copyright           : (c) GENESIS

  Function : Oracle Script example to generate the UpdateQueue and its basic procedures.
    Note: Change "DB_NAME" for actual database name.
*/

CREATE TABLE "DB_NAME"."UPDATEQUEUE"
   (	"TABLE_NAME" VARCHAR2(20 BYTE) not null,
	    "UPDATE_TYPE" VARCHAR2(20 BYTE) not null,
	    "RECORD_REFERENCE" VARCHAR2(20 BYTE) not null,
	    "RECORD_TIMESTAMP" TIMESTAMP (6) not null,
	    "ID" RAW(16)
   );
   /

CREATE UNIQUE INDEX "DB_NAME"."UPDATEQUEUE_PK" ON "DB_NAME"."UPDATEQUEUE" ("ID");

ALTER TABLE "DB_NAME"."UPDATEQUEUE" ADD CONSTRAINT "UPDATEQUEUE_PK" PRIMARY KEY ("ID");

create or replace PROCEDURE queryUpdateQueue
(p_TABLE_NAME IN UPDATEQUEUE.TABLE_NAME%TYPE,
p_TIMESTAMP IN UPDATEQUEUE.RECORD_TIMESTAMP%TYPE,
prc OUT sys_refcursor)

is
begin
   open prc for select * from UPDATEQUEUE where TABLE_NAME = p_TABLE_NAME AND RECORD_TIMESTAMP > p_TIMESTAMP order by RECORD_TIMESTAMP ASC;
end queryUpdateQueue;
/

create or replace PROCEDURE clearUpdateQueue
(p_TABLE_NAME IN UPDATEQUEUE.TABLE_NAME%TYPE,
p_TIMESTAMP IN UPDATEQUEUE.RECORD_TIMESTAMP%TYPE)

is
begin
   delete from UPDATEQUEUE where TABLE_NAME = p_TABLE_NAME AND RECORD_TIMESTAMP <= p_TIMESTAMP;
   COMMIT;
end clearUpdateQueue;
/