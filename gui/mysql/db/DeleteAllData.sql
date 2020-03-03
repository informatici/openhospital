#version 28-5-2006
#version 12-6-2011

#tabelle senza relazioni
delete from PREGNANTTREATMENTTYPE;
delete from VERSION;
delete from LOG;
delete from HELP;
delete from DELIVERYRESULTTYPE;
delete from DELIVERYTYPE;
delete from HOSPITAL;
delete from AGETYPE;
delete from MEDICALSRSTOCKMOV_N;
delete from PRICELISTS;
delete from PRICES;
delete from PRICESOTHERS;
delete from BILLITEMS;
delete from BILLPAYMENTS;
delete from BILLS;
delete from THERAPIES;
delete from VISITS;

#tabelle con relazioni, ordine inverso di cancellazione
delete from GROUPMENU;
delete from USER;
delete from MENUITEM;
delete from USERGROUP;
delete from OPD;
delete from LABORATORYROW;
delete from LABORATORY;
delete from EXAMROW;
delete from EXAM;
delete from EXAMTYPE;
delete from MEDICALDSRLOT;
delete from MEDICALDSRSTOCKMOV;
delete from MEDICALDSRSTOCKMOVTYPE;
delete from MEDICALDSR;
delete from MEDICALDSRTYPE;
delete from VACCINE;
delete from PATIENTVACCINE;
delete from MALNUTRITIONCONTROL;
delete from ADMISSION;
delete from ADMISSIONTYPE;
delete from DISCHARGETYPE;
delete from OPERATION;
delete from OPERATIONTYPE;
delete from DISEASE;
delete from DISEASETYPE;
delete from PATIENT;
delete from WARD;

