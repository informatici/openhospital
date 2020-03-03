-- Query per la gestione del AGETYPE

ALTER TABLE PATIENT ADD COLUMN PAT_AGETYPE VARCHAR(50) NOT NULL DEFAULT '' AFTER PAT_AGE;

--
-- Definition of table AGETYPE
--

DROP TABLE IF EXISTS AGETYPE;
CREATE TABLE AGETYPE (
  AT_CODE VARCHAR(4) NOT NULL DEFAULT '',
  AT_FROM INT(11) NOT NULL DEFAULT 0,
  AT_TO INT(11) NOT NULL DEFAULT 0,
  AT_DESC VARCHAR(100) character set utf8 collate utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (AT_CODE)
) ENGINE=MyISAM;

--
-- Dumping default data for table AGETYPE
--

/*!40000 ALTER TABLE AGETYPE DISABLE KEYS */;
LOCK TABLES AGETYPE WRITE;
INSERT INTO AGETYPE VALUES  ('d0',0,0,'angal.agetype.newborn'),
 ('d1',1,5,'angal.agetype.earlychildhood'),
 ('d2',6,12,'angal.agetype.latechildhood'),
 ('d3',13,24,'angal.agetype.adolescents'),
 ('d4',25,59,'angal.agetype.adult'),
 ('d5',60,99,'angal.agetype.elderly');
UNLOCK TABLES;
/*!40000 ALTER TABLE AGETYPE ENABLE KEYS */;


INSERT INTO MENUITEM VALUES ('agetype', 'angal.menu.btn.agetype', 'angal.menu.agetype', 'x', 'G', 'types', 'org.isf.agetype.gui.AgeTypeBrowser','N', 11);

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','agetype','Y');

