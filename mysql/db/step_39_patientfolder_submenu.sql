UPDATE MENUITEM SET MNI_IS_SUBMENU='Y' WHERE MNI_ID_A='btnadmpatientfolder';

INSERT INTO MENUITEM VALUES ('btnpatfoldopdrpt', 'angal.menu.btn.opdchart', 'angal.menu.opdchart', 'x', 'O', 'btnadmpatientfolder', 'none','N', 1);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnpatfoldopdrpt','Y');

INSERT INTO MENUITEM VALUES ('btnpatfoldadmrpt', 'angal.menu.btn.admchart', 'angal.menu.admchart', 'x', 'A', 'btnadmpatientfolder', 'none','N', 2);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnpatfoldadmrpt','Y');

INSERT INTO MENUITEM VALUES ('btnpatfoldpatrpt', 'angal.menu.btn.patreport', 'angal.menu.patreport', 'x', 'R', 'btnadmpatientfolder', 'none','N', 3);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnpatfoldpatrpt','Y');

INSERT INTO MENUITEM VALUES ('btnpatfolddicom', 'angal.menu.btn.dicom', 'angal.menu.dicom', 'x', 'D', 'btnadmpatientfolder', 'none','N', 4);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnpatfolddicom','Y');
