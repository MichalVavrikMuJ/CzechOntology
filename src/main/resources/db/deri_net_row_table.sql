--------------------------------------------------------
--  File created - Sunday-April-05-2020   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table DERI_NET_ROW
--------------------------------------------------------

  CREATE TABLE "NEURONKY"."DERI_NET_ROW" 
   (	"ID" FLOAT(126), 
	"LANGUAGE_SPECIFIC_ID" VARCHAR2(3999 BYTE), 
	"MAIN_PARENT_ID" FLOAT(126), 
	"LEMMA" VARCHAR2(3999 BYTE), 
	"POS" VARCHAR2(3999 BYTE), 
	"MORPHOLOGICAL_FEATURES" VARCHAR2(3999 BYTE), 
	"MORPHOLOGICAL_SEGMENTATION" VARCHAR2(3999 BYTE), 
	"PARENT_RELATION" VARCHAR2(3999 BYTE), 
	"OTHER_RELATIONS" VARCHAR2(3999 BYTE), 
	"JSON_GENERAL_DATA" VARCHAR2(3999 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C004561
--------------------------------------------------------

  CREATE UNIQUE INDEX "NEURONKY"."SYS_C004561" ON "NEURONKY"."DERI_NET_ROW" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table DERI_NET_ROW
--------------------------------------------------------

  ALTER TABLE "NEURONKY"."DERI_NET_ROW" ADD PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DERI_NET_ROW
--------------------------------------------------------

  ALTER TABLE "NEURONKY"."DERI_NET_ROW" ADD CONSTRAINT "PARENT_ID" FOREIGN KEY ("MAIN_PARENT_ID")
	  REFERENCES "NEURONKY"."DERI_NET_ROW" ("ID") ENABLE;
