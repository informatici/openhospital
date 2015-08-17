ALTER TABLE MEDICALDSRSTOCKMOV ADD COLUMN MMV_REFNO VARCHAR(50) NOT NULL DEFAULT ''  AFTER MMV_LOCK;

INSERT INTO MENUITEM VALUES ('btnpharmstockcharge','angal.menu.btn.btnpharmstockcharge','angal.menu.btnpharmstockcharge','x','C','medicalstock','none','N', 1);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnpharmstockcharge','Y');

INSERT INTO MENUITEM VALUES ('btnpharmstockdischarge','angal.menu.btn.btnpharmstockdischarge','angal.menu.btnpharmstockdischarge','x','D','medicalstock','none','N', 2);
INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) VALUES ('admin','btnpharmstockdischarge','Y');