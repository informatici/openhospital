--
-- Definition of table MEDICALSRSTOCKMOV_N
--

DROP TABLE IF EXISTS MEDICALSRSTOCKMOV_N;
CREATE TABLE  MEDICALSRSTOCKMOV_N (
  MMVN_ID int(10) NOT NULL auto_increment,
  MMVN_WRD_ID_A char(1) NOT NULL,
  MMVN_DATE datetime NOT NULL,
  MMVN_PAT_ID varchar(100) character set latin1 NOT NULL,
  MMVN_MDSR_ID varchar(100) character set latin1 NOT NULL,
  MMVN_MDSR_QTY int(10) NOT NULL,
  MMVN_MDSR_UNITS varchar(10) NOT NULL,
  PRIMARY KEY  USING BTREE (MMVN_ID)
) ENGINE=MyISAM;

INSERT INTO MENUITEM VALUES ('medicalsward', 'angal.menu.btn.medicalsward', 'angal.menu.medicalsward', 'x', 'W', 'pharmacy', 'org.isf.medicalstockward.gui.WardPharmacy','N', 2);

-- Functionality initially disabled. Put 'Y' to activate it

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','medicalsward','Y');
