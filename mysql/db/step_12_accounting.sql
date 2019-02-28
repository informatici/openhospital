--
-- Definition of table BILLITEMS
--

DROP TABLE IF EXISTS BILLITEMS;
CREATE TABLE  BILLITEMS (
  BLI_ID int(11) NOT NULL auto_increment,
  BLI_ID_BILL int(11) default NULL,
  BLI_IS_PRICE tinyint(1) NOT NULL,
  BLI_ID_PRICE varchar(10) default NULL,
  BLI_ITEM_DESC varchar(100) default NULL,
  BLI_ITEM_AMOUNT double NOT NULL,
  BLI_QTY int(11) NOT NULL,
  PRIMARY KEY (BLI_ID)
) ENGINE=MyISAM;

--
-- Definition of table BILLPAYMENTS
--

DROP TABLE IF EXISTS BILLPAYMENTS;
CREATE TABLE  BILLPAYMENTS (
  BLP_ID int(11) NOT NULL auto_increment,
  BLP_ID_BILL int(11) default NULL,
  BLP_DATE datetime NOT NULL,
  BLP_AMOUNT double NOT NULL,
  PRIMARY KEY (BLP_ID)
) ENGINE=MyISAM;

--
-- Definition of table BILLS
--

DROP TABLE IF EXISTS BILLS;
CREATE TABLE  BILLS (
  BLL_ID int(11) NOT NULL auto_increment,
  BLL_DATE datetime NOT NULL,
  BLL_UPDATE datetime NOT NULL,
  BLL_IS_LST tinyint(1) NOT NULL,
  BLL_ID_LST int(11) default NULL,
  BLL_LST_NAME varchar(50) default NULL,
  BLL_IS_PAT tinyint(1) NOT NULL,
  BLL_ID_PAT int(11) default NULL,
  BLL_PAT_NAME varchar(100) default NULL,
  BLL_STATUS varchar(1) default NULL,
  BLL_AMOUNT double default NULL,
  BLL_BALANCE double default NULL,
  PRIMARY KEY (BLL_ID)
) ENGINE=MyISAM;

INSERT INTO MENUITEM VALUES ('accounting', 'angal.menu.btn.accounting', 'angal.menu.accounting', 'x', 'C', 'main', 'none','Y', 5);

INSERT INTO MENUITEM VALUES ('newbill', 'angal.menu.btn.newbill', 'angal.menu.newbill', 'x', 'N', 'accounting', 'org.isf.accounting.gui.PatientBillEdit','N', 0);

INSERT INTO MENUITEM VALUES ('billsmanager', 'angal.menu.btn.billsmanager', 'angal.menu.billsmanager', 'x', 'M', 'accounting', 'org.isf.accounting.gui.BillBrowser','N', 1);

-- Functionalities initially disabled. Put 'Y' to activate them

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','accounting','Y');

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','newbill','Y');

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','billsmanager','Y');

