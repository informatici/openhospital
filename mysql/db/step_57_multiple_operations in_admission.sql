CREATE TABLE OPERATIONROW(
	OPER_ID_A  int (11) NOT NULL auto_increment,
	OPER_ID varchar (11)  NOT NULL ,
	OPER_PRESCRIBER varchar (150)  NOT NULL ,
	OPER_RESULT varchar (250)  NOT NULL ,
	OPER_OPDATE datetime NOT NULL default '2018-01-01 00:00:00',
	OPER_REMARKS varchar (250) NOT NULL,
	OPER_ADMISSION_ID int(11) default 0, 
	OPER_OPD_ID int(11)  default 0,
	OPER_BILL_ID int(11)  default 0,
	OPER_TRANS_UNIT float NULL default 0,	
	PRIMARY KEY (OPER_ID_A)
);