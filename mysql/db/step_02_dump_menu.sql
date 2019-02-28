#v 17-11-2006  (ADDED 3 NEW MENU ITEMS, printing, examlist, diseaselist)

#
# deleting existing data
#
delete from GROUPMENU;
delete from MENUITEM;
delete from USER;
delete from USERGROUP;

#
# Dumping data for table 'USERGROUP'
#
INSERT INTO USERGROUP VALUES("admin","User with all the privileges");

#
# Dumping data for table 'USER'
#
INSERT INTO USER VALUES("admin","admin","admin","administrator");


#
# Dumping data for table 'MENUITEM'
#
INSERT INTO MENUITEM (MNI_ID_A,MNI_BTN_LABEL,MNI_LABEL,MNI_TOOLTIP,MNI_SHORTCUT,MNI_SUBMENU,MNI_CLASS,MNI_IS_SUBMENU,MNI_POSITION) VALUES 
 ('admission','Admission/Patient','Admission/Patient','x','A','main','org.isf.admission.gui.AdmittedPatientBrowser','N',5),
 ('admtype','Admission Type','AdmissionType','x','A','types','org.isf.admtype.gui.AdmissionTypeBrowser','N',0),
 ('deliverytype','Delivery Type','Delivery','x','T','types','org.isf.dlvrtype.gui.DeliveryTypeBrowser','N',2),
 ('delresulttype','Delivery Result Type','DeliveryResult','x','R','types','org.isf.dlvrrestype.gui.DeliveryResultTypeBrowser','N',3),
 ('disctype','Discharge Type','DischargeType','x','D','types','org.isf.disctype.gui.DischargeTypeBrowser','N',1),
 ('disease','Disease','Disease','x','D','generaldata','org.isf.disease.gui.DiseaseBrowser','N',3),
 ('diseaselist','Disease List','DiseaseList','x','D','printing','org.isf.stat.gui.DiseasesListLauncher','N',2),
 ('diseasetype','Disease Type','Disease','x','D','types','org.isf.distype.gui.DiseaseTypeBrowser','N',4),
 ('examlist1','Exams List','ExamsList','x','E','printing','org.isf.stat.gui.ExamsList1Launcher','N',1),
 ('exams','Exams','Exams','x','E','generaldata','org.isf.exa.gui.ExamBrowser','N',4),
 ('examtype','Exam Type','Exam Type','x','E','types','org.isf.exatype.gui.ExamTypeBrowser','N',5),
 ('exit','Exit','Exit','x','E','file','none','N',0),
 ('file','File','File','x','F','main','none','Y',0),
 ('generaldata','General Data','GeneralData','x','G','main','none','Y',1),
 ('groups','Groups','Groups','x','G','users','org.isf.menu.gui.UsrGroupBrowsing','N',1),
 ('help','HELP','HELP','x','H','main','none','N',8),
 ('hospital','Hospital','Hospital','x','H','generaldata','org.isf.hospital.gui.HospitalBrowser','N',1),
 ('labbrowsing','Laboratory Browsing','LaboratoryBrowsing','x','B','laboratory','org.isf.lab.gui.LabBrowser','N',0),
 ('laboratory','Laboratory','Laboratory','x','L','main','none','Y',4),
 ('laboratoryresulttype','Lab. Res. Type','Laboratory result type','x','L','types','org.isf.labrestype.gui.LaboratoryResultTypeBrowser','N',10),
 ('medicals','Pharmaceuticals','Pharmaceuticals','x','P','pharmacy','org.isf.medicals.gui.MedicalBrowser','N',0),
 ('medicalstock','Pharmaceutical Stock','PharmaceuticalStock','x','S','pharmacy','org.isf.medicalstock.gui.MovStockBrowser','N',1),
 ('medicalstype','Medicals Type','Medicals','x','M','types','org.isf.medtype.gui.MedicalTypeBrowser','N',7),
 ('medstockmovtype','Medicals Stock Mov Type','MedicalsStock Mov','x','S','types','org.isf.medstockmovtype.gui.MedicaldsrstockmovTypeBrowser','N',6),
 ('opd','OPD','OPD','x','O','main','org.isf.opd.gui.OpdBrowser','N',2),
 ('operation','Operation','Operation','x','O','generaldata','org.isf.operation.gui.OperationBrowser','N',5),
 ('operationtype','Operation Type','Operation','x','O','types','org.isf.opetype.gui.OperationTypeBrowser','N',8),
 ('pharmacy','Pharmacy','Pharmacy','x','P','main','none','Y',3),
 ('pretreatmenttype','Pregnant Treatment','PregnantTreatment','x','P','types','org.isf.pregtreattype.gui.PregnantTreatmentTypeBrowser','N',9),
 ('printing','Printing','Printing','x','R','main','none','Y',7),
 ('statistics','Statistics','Statistics','x','T','main','org.isf.stat.reportlauncher.gui.ReportLauncher','N',6),
 ('types','Types','Types','x','T','generaldata','none','Y',0),
 ('users','Users','Users','x','U','file','none','Y',1),
 ('usersusers','Users','Users','x','U','users','org.isf.menu.gui.UserBrowsing','N',0),
 ('vaccine','Vaccine','Vaccine','x','V','generaldata','org.isf.vaccine.gui.VaccineBrowser','N',6),
 ('ward','Ward','Ward','x','W','generaldata','org.isf.ward.gui.WardBrowser','N',2);

#
# Dumping data for table 'GROUPMENU'
#
INSERT INTO GROUPMENU (GM_ID,GM_UG_ID_A,GM_MNI_ID_A,GM_ACTIVE) VALUES 
 ( 1,'admin','admtype','Y'),
 ( 2,'admin','disctype','Y'),
 ( 3,'admin','laboratoryresulttype','Y'),
 ( 4,'admin','admission','Y'),
 ( 7,'admin','disease','Y'),
 ( 8,'admin','exams','Y'),
 ( 9,'admin','exit','Y'),
 (10,'admin','file','Y'),
 (11,'admin','generaldata','Y'),
 (12,'admin','groups','Y'),
 (13,'admin','help','Y'),
 (14,'admin','hospital','Y'),
 (15,'admin','labbrowsing','Y'),
 (16,'admin','laboratory','Y'),
 (17,'admin','medicals','Y'),
 (18,'admin','medicalstock','Y'),
 (19,'admin','operation','Y'),
 (21,'admin','pharmacy','Y'),
 (22,'admin','statistics','Y'),
 (26,'admin','opd','Y'),
 (27,'admin','users','Y'),
 (28,'admin','usersusers','Y'),
 (29,'admin','vaccine','Y'),
 (30,'admin','ward','Y'),
 (31,'admin','types','Y'),
 (32,'admin','pretreatmenttype','Y'),
 (33,'admin','diseasetype','Y'),
 (34,'admin','medstockmovtype','Y'),
 (35,'admin','examtype','Y'),
 (36,'admin','operationtype','Y'),
 (37,'admin','deliverytype','Y'),
 (38,'admin','medicalstype','Y'),
 (39,'admin','delresulttype','Y'),
 (40,'admin','printing','Y'),
 (41,'admin','examlist1','Y'),
 (42,'admin','diseaselist','Y');


