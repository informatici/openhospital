CREATE TABLE PATIENTEXAMINATION (
	PEX_ID int NOT NULL AUTO_INCREMENT ,
	PEX_DATE datetime NOT NULL ,
	PEX_PAT_ID int NOT NULL ,
	PEX_HEIGHT double DEFAULT 0 ,
	PEX_WEIGHT int DEFAULT 0 ,
	PEX_PA_MIN int DEFAULT 0 ,
	PEX_PA_MAX int DEFAULT 0 ,
	PEX_FC int DEFAULT 0 ,
	PEX_TEMP double DEFAULT 0 ,
	PEX_SAT double DEFAULT 0 ,
	PEX_NOTE varchar(300) NULL ,
	
	INDEX ( PEX_PAT_ID  ) ,
	PRIMARY KEY ( PEX_ID )
);

--
-- CONSTRAINTS
--
ALTER TABLE PATIENTEXAMINATION
	ADD CONSTRAINT FK_PATIENTEXAMINATION_PATIENT 
	FOREIGN KEY (PEX_PAT_ID) 
	REFERENCES PATIENT (PAT_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE;

-- Examination Button
INSERT INTO MENUITEM VALUES ('btnopdnewexamination','angal.opd.examination','angal.opd.examination','x','A','btnopdnew','none','N', 1);
INSERT INTO MENUITEM VALUES ('btnopdeditexamination','angal.opd.examination','angal.opd.examination','x','A','btnopdedit','none','N', 1);
INSERT INTO MENUITEM VALUES ('btnadmadmexamination','angal.admission.examination','angal.admission.examination','x','A','btnadmadm','none','N', 1);
INSERT INTO MENUITEM VALUES ('btnadmexamination','angal.admission.examination','angal.admission.examination','x','A','admission','none','N', 1);


-- Admin activation
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnopdnewexamination','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnopdeditexamination','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnadmadmexamination','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnadmexamination','Y');

--
--  ROLLBACK ON MALNUTRITIONCONTROL CHANGES (step_23_wardpharmacy20.sql)
--

INSERT INTO PATIENTEXAMINATION (PEX_DATE, PEX_PAT_ID, PEX_HEIGHT, PEX_WEIGHT) 
SELECT MLN_DATE_SUPP, MLN_PAT_ID, IF(MLN_HEIGHT<3, MLN_HEIGHT * 100, MLN_HEIGHT), MLN_WEIGHT FROM MALNUTRITIONCONTROL WHERE MLN_PAT_ID <> 0;

DELETE FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = 0;

ALTER TABLE MALNUTRITIONCONTROL DROP COLUMN MLN_PAT_ID, CHANGE COLUMN MLN_ADM_ID MLN_ADM_ID INT(11) NOT NULL;
