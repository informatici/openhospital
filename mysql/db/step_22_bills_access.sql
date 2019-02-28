INSERT INTO MENUITEM VALUES ('btnbillnew','angal.billbrowser.newbill','angal.billbrowser.newbill','x','N','billsmanager','none','N',0);
INSERT INTO MENUITEM VALUES ('btnbilledit','angal.billbrowser.editbill','angal.billbrowser.editbill','x','N','billsmanager','none','N',1);
INSERT INTO MENUITEM VALUES ('btnbilldelete','angal.billbrowser.deletebill','angal.billbrowser.deletebill','x','N','billsmanager','none','N',2);
INSERT INTO MENUITEM VALUES ('btnbillreport','angal.billbrowser.report','angal.billbrowser.report','x','N','billsmanager','none','N',3);

INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnbillnew','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnbilledit','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnbilldelete','Y');
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnbillreport','Y');
