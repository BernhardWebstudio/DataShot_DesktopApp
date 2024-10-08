ALTER TABLE SPECIMEN 
ADD (VERBATIMCLUSTERIDENTIFIER VARCHAR2(255 CHAR) );

ALTER TABLE SPECIMEN 
ADD (EXTERNALWORKFLOWPROCESS VARCHAR2(900 CHAR) );

ALTER TABLE SPECIMEN 
ADD (EXTERNALWORKFLOWDATE DATE );

CREATE INDEX IDX_EXTCLUSTERIDENTIFIER ON SPECIMEN (VERBATIMCLUSTERIDENTIFIER);


ALTER TABLE TEMPLATE ADD CONSTRAINT TEMPLATE_IMAGE_FK1 FOREIGN KEY
( REFERENCEIMAGE ) REFERENCES IMAGE ( IMAGEID ) ON DELETE SET NULL ENABLE;

CREATE TABLE ALLOWED_VERSION 
(
  ALLOWED_VERSION_ID NUMBER(20, 0) NOT NULL 
, VERSION VARCHAR2(50 CHAR) NOT NULL 
, CONSTRAINT ALLOWED_VERSION_PK PRIMARY KEY 
  (
    ALLOWED_VERSION_ID 
  )
  ENABLE 
);

CREATE OR REPLACE TRIGGER "LEPIDOPTERA"."ALLOWED_VERSION_TRG" BEFORE
  INSERT ON ALLOWED_VERSION FOR EACH ROW BEGIN <<COLUMN_SEQUENCES>> BEGIN IF :NEW.ALLOWED_VERSION_ID IS NULL THEN
  SELECT ALLOWED_VERSION_SEQ.NEXTVAL INTO :NEW.ALLOWED_VERSION_ID FROM DUAL;
END IF;
END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "LEPIDOPTERA"."ALLOWED_VERSION_TRG" ENABLE;

insert into allowed_version (version) values ('1.3');

CREATE TABLE EXTERNAL_HISTORY 
(
  EXTERNAL_HISTORY_ID NUMBER(20, 0) NOT NULL 
, SPECIMENID NUMBER(20, 0) NOT NULL 
, EXTERNALWORKFLOWPROCESS VARCHAR2(900 CHAR) 
, EXTERNALWORKFLOWDATE DATE 
, CONSTRAINT EXTERNAL_HISTORY_PK PRIMARY KEY 
  (
    EXTERNAL_HISTORY_ID 
  )
  ENABLE 
);

CREATE UNIQUE INDEX "LEPIDOPTERA"."EXTERNAL_HISTORY_PK" ON "LEPIDOPTERA"."EXTERNAL_HISTORY"
  (
    "EXTERNAL_HISTORY_ID"
  )
  PCTFREE 10 INITRANS 2 MAXTRANS 255 TABLESPACE "USERS" ;
  CREATE INDEX "LEPIDOPTERA"."EXTERNAL_HISTORY_INDEX1" ON "LEPIDOPTERA"."EXTERNAL_HISTORY"
    (
      "SPECIMENID"
    )
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS TABLESPACE "USERS" ;
CREATE OR REPLACE TRIGGER "LEPIDOPTERA"."EXTERNAL_HISTORY_TRG" BEFORE
  INSERT ON EXTERNAL_HISTORY FOR EACH ROW BEGIN <<COLUMN_SEQUENCES>> BEGIN IF :NEW.EXTERNAL_HISTORY_ID IS NULL THEN
  SELECT EXTERNAL_HISTORY_SEQ.NEXTVAL INTO :NEW.EXTERNAL_HISTORY_ID FROM DUAL;
END IF;
END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "LEPIDOPTERA"."EXTERNAL_HISTORY_TRG" ENABLE;
