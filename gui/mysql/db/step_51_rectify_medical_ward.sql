ALTER TABLE MEDICALDSRWARD CHANGE COLUMN MDSRWRD_IN_QTI MDSRWRD_IN_QTI FLOAT NULL DEFAULT '0', CHANGE COLUMN MDSRWRD_OUT_QTI MDSRWRD_OUT_QTI FLOAT NULL DEFAULT '0';

INSERT INTO MENUITEM VALUES ('btnmedicalswardrectify','angal.menu.btn.btnmedicalswardrectify','angal.menu.btnmedicalswardrectify','x','R','medicalsward','none','N',1);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnmedicalswardrectify','Y');
