DROP TABLE IF EXISTS VACCINETYPE;
CREATE TABLE VACCINETYPE (
	VACT_ID_A char (1)  NOT NULL ,
	VACT_DESC varchar (50)  NOT NULL ,
	PRIMARY KEY ( VACT_ID_A )
) ENGINE=MyISAM;

ALTER TABLE VACCINE 
   CHANGE VAC_PATI  VAC_VACT_ID_A char(1) not null;

INSERT INTO VACCINETYPE (VACT_ID_A, VACT_DESC)  
SELECT DISTINCT  VAC_VACT_ID_A, CASE VAC_VACT_ID_A WHEN "C" THEN "Child" WHEN "P" THEN "Pregnant" WHEN "N" THEN "No pregnant" END
FROM VACCINE;

INSERT INTO MENUITEM VALUES ('vaccinetype','angal.menu.btn.vaccinetype','angal.menu.vaccinetype','x','V','types','org.isf.vactype.gui.VaccineTypeBrowser','N', 12);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','vaccinetype','Y');

INSERT INTO MENUITEM VALUES ('patientvaccine','angal.menu.btn.patientvaccine','angal.menu.patientvaccine','x','V','main','org.isf.patvac.gui.PatVacBrowser','N', 5);
INSERT INTO MENUITEM VALUES ('btnpatientvaccinenew','angal.patvac.new','angal.patvac.new','x','N','patientvaccine','none','N',0);
INSERT INTO MENUITEM VALUES ('btnpatientvaccineedit','angal.patvac.edit','angal.patvac.edit','x','E','patientvaccine','none','N',1);
INSERT INTO MENUITEM VALUES ('btnpatientvaccinedel','angal.patvac.delete','angal.patvac.delete','x','D','patientvaccine','none','N',2);

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','patientvaccine','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnpatientvaccinenew','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnpatientvaccineedit','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE)  VALUES  ('admin','btnpatientvaccinedel','Y');
