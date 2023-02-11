Open Hospital v1.12.0
---------------------
Open Hospital (https://www.open-hospital.org/) is a free and open-source Health Information Management System (HIMS) software application.
_Released on 07 February 2023_

**Release Highlights**
- Arabic language support
- Upgrade to Java 11 and Mariadb 10.6 database support
- Linux Ubuntu 22.10 and Windows 11 support
- Full Dicom / Imaging support on x64 architectures
- Interactive startup scripts for Windows and Linux
- Feature to load custom reports from external folder
- Improved overall security settings and configuration
- Improved stability and performance
- Improved [website](https://www.open-hospital.org/) and [documentation](https://www.open-hospital.org/documentation) 
- EXPERIMENTAL: First full distribution (core + gui + api + ui)
- a lot of bug fixes 🙂

**Credits**: [Alessandro Domanico](@mwthi), [Andrei Dodu](@goto-eof), [David Malkovksy](@dbmalkovsky), [Intesys](https://github.com/intesys), [Nicole Lazzeri](https://github.com/nlazzer1), [mizzio](@mizzioisf), [Paolo Viotti](@pviotti), [Uni2grow](https://github.com/orgs/informatici/teams/uni2grow-cameroun) (see the complete list below)
**Special thanks to**: Antonio Jurado, Stefania Bergo
_Brought to you by [Informatici Senza Frontiere](https://github.com/informatici)_

**Languages**: Languages: English, Arabic, Chinese (simplified), French, German, Italian, Portuguese, Spanish, Swahili
*Brought to you by [Transifex community](https://www.transifex.com/informatici-senza-frontiere-onlus/openhospital/dashboard/)*

<details>
<summary> Release notes v1.12.0 details (click to expand) </summary>

# Release notes - Open Hospital - 1.12.0

### New Features

OP-97 New Anamnesis module
OP-138 Patient Photo to File System Repository
OP-159 New Open Hospital EXPERIMENTAL package
OP-701 User Logout and Timeout (Automatic Logout)
OP-705 Add option to login by typing the username instead of selecting from a list
OP-707 Add feature to create more OPD wards
OP-845 Add Laboratory button in Admission / Patient window
OP-875 Feature to link Bills with Admissions or OPD
OP-909 Add visit duration to Ward table 
OP-910 Add print button for Operations in Main Menu \ Reports
OP-962 Session database table for logged users
OP-981 Dicom series studies for JPEGs images in the same folder
OP-999 Add feature to load custom reports from external folder
OP-1014 Add support for Arabic language

### Improvements

OP-213 Add search fields for OPD records
OP-314 DICOM on Windows 64bit
OP-347 Update DICOM libraries
OP-348 User interface improvement for all search fields and calendars
OP-576 Filter Bill Manager table upon cashier (user) selection
OP-684 Add option to view online doc 
OP-696 Patient's Statement report on the selected patient
OP-704 Search box for Operations tabs in OPD and Admission window
OP-706 Change "DICOM" to more general "Imaging"
OP-725 Better display of the logged in user
OP-831 Database (mariadb) upgrade
OP-854 Upgrade to Java 11
OP-885 Security - User input data type validation (D02)
OP-894 Security - Password Length [configurable] (S01)
OP-895 Security - Password strength [deactivable][configurable] (S02)
OP-901 Security - Automatic logout after timeout (S13)
OP-905 Wrap the button bar on windows when the window is made smaller (instead of truncating the list of buttons)
OP-969 Replace/Remove JPEGImageReader because no longer provided in jdk11
OP-977 Allow "Types" and "User Group" changes
OP-980 Image file loader improvements
OP-989 Improve LabEdit and LabEditExtended layout
OP-1002 Add "createdBy" column in MovStockBrowser
OP-1013 Improved the OPD Next Visit workflow

### Bug Fixes

OP-328 Hospital data window requires double click to activate buttons on Linux x64
OP-687 Fix waiting cursor for patient search
OP-723 Fix failing sorting in Admission/Patient \ Data, 
OP-727 Changed user table name as it is a SQL keyword
OP-745 BillManager not showing bills other than today date
OP-751 Wrong date format for VoDateTextField
OP-775 Patient Data dialog not showing dates for the Exams tab
OP-776 The month date conversion in OPD Browser is off by 1
OP-821 Getting date with additional month was using the current month to calculate the last day of the month
OP-826 Wrong property parameter in MOH717_Monthly_Workload_Report_for_Hospitals_page2b.jxrml
OP-870 Title is wrong on Admission/Patient  \  Edit dialog when PATIENTEXTENDED=no
OP-873 Fix time rendering for OPD  \  New OPD Registration
OP-879 Patient profile picture size limited to 4096 bytes
OP-906 GUI tests won't run with Spring 5 and JUnit 5 (jupiter)
OP-908 Fix bad merge after 1.11.3 in PatientFolderBrowser
OP-915 Year picker on Patient Bills Management doesn't change the "to" year
OP-919 Worksheet too small when Admission/Patient \ Therapy \ Worksheet
OP-922 Formatting exceptions generating reports with Pharmacy \ Pharmaceuticals \ Expiring
OP-923 Formatting exceptions generating reports with Accounting \ Bill Manager \ Report
OP-926 Fix DateTimeParseException exception when generating a report from the Drugs Tab of Patient Data
OP-928 Error evaluating expression for source text: $F{date} in PrintManager.java
OP-929 Fix "IllegalArgumentException: Cannot format given Object as a Date" when generating a report
OP-931 If SMSENABLED=no then remove SMS Manager from Settings menu
OP-939 Wards' list problem in Worksheet
OP-940 OPD date should NOT be updated on edit
OP-941 OPD No. already exists upon OPD edit
OP-942 Pharmaceuticals double edit causes "the data has been updated by someone else"
OP-945 Excel exporter needs connection open (no autocommit)
OP-946 IllegalArgumentException in Pharmaceutical Stock when filtering by lot date
OP-947 Fix Excel Report in Pharmaceutical Stock Ward
OP-948 Wrong Drugs calculation in Pharmaceutical Stock Ward
OP-949 Error when creating new lots in Pharmaceutical Stock Ward
OP-950 Wrong range selection in Pharmaceutical Stock Ward
OP-953 Wrong table column headers; the last two are switched
OP-956 OPD edit from Admission/Patient \ Data is not updating in memory model
OP-961 NPE when trying to generate a tooltip when trying to load a jpg into the DICOM viewer
OP-963 Height and Weight values inverted in Patient Data
OP-968 Fix some tests failing locally with JDK11 on Windows
OP-970 Move literal string to resource bundle in Ward Browser
OP-971 Use TimeTools.truncateToSeconds() when returning dateTimes that could be null
OP-974 @Transactional methods in manager not catching Hibernate's translated execptions
OP-979 Change "New Admittance" for OPD into "New Attendance"
OP-983 Index out of bounds error when changing the filter Select type in the Exam Browser
OP-984 Switching to a specific disease type and then switching back to ALL in Disease Browser doesn't work
OP-985 Not possible to save an Exam of type "Procedure 2" with all negative results
OP-986 Getting the contents of the Ward column when it is OPD in Patient Data and other wards exist causes an error
OP-987 getPatientById to update at each read (caused by OP-138)
OP-988 Confusing check when Exam "Type 1" has not default value set
OP-996 Object is always null when calling medicalBrowsingManager.updateMedical()
OP-1000 Fix wrong "Payment in the future" upon PAID
OP-1003 DB ProfilePicture persists upon Patient deletion
OP-1004 MedicalStockMov Charge movement has no time field
OP-1006 Statistics tab throws a "setSelectedIndex: 12 out of bounds" exception
OP-1007 Fix "remembered ward" in OPD
OP-1010 When SINGLEUSER=yes the LOGOUT button should Exit the application
OP-1012 Correct the saving of edited OPD record as it is driven by a next visit being specified
OP-1023 NPE when creating the Clinical Sheet window
OP-1026 Missing anamnesis related property strings in language_XX.properties
OP-1027 Missing property string in resource bundle for Ward Browser
OP-1028 Incorrect test for missing time when creating a visit in OPD extended
OP-1031 OH-GUI - Error when canceling login window

</details>

<details>
<summary> Code changelogs (click to expand) </summary>

 - [Core component changelog](https://github.com/informatici/openhospital-core/compare/v1.11.5...v1.12.0)
 - [Gui component changelog](https://github.com/informatici/openhospital-gui/compare/v1.11.5...v1.12.0)
 - [Doc component changelog](https://github.com/informatici/openhospital-doc/compare/v1.11.5...v1.12.0)

</details>

<details>
<summary> SHA256SUM checksum (click to expand) </summary>

```
8d1da2f0a2d52db6856e6b2fe3ed479564d0e204311feb28d557b28fd307ad8c  OpenHospital-v1.12.0-multiarch-client.zip
9080a32004a0a88077fe421c0af9ad085f63d7fb7d0fac2e8127630b1cd7738f  OpenHospital-v1.12.0-windows_i686-portable.zip
252776fde18400f2327710102d5ba20592b4bf9470ace6005e4dcb24caa39dc3  OpenHospital-v1.12.0-windows_x86_64-portable.zip
0cb41a836e9823a7788bda3973e3c9c1458bd7748d7800ee6a05d1801cd0de55  OpenHospital-v1.12.0-linux_i686-portable.tar.gz
a6eacac74322faef65282883588e115dcfd7e8247ff4b1f13ff787d4cf945b2b  OpenHospital-v1.12.0-linux_x86_64-portable.tar.gz
8f91050989ba02b0288a879f23f29d2f213506f31ad60bc123fdee9a82882394  OpenHospital-v1.12.0-x86_64-EXPERIMENTAL.zip
```

</details>

<details>
<summary> Contributors (click to expand) </summary>
@AceGentile, @AndreiDodu, @AndreiDoduISF, @GraninNikita, @MartinezX21, @MrQwenty, @SilverD3, @SteveGT96, @alefalezza, @alekseykashytsa, @alexwibowo, @almeida1492, @andreafalco1991, @averni, @axelpezzo, @brian-heumann, @dbmalkovsky, @dependabot-preview[bot], @dependabot[bot], @dnlebgwa, @dumisani-ln, @elvisciuffetelli, @emecas, @estromsnes, @fstorz, @geco17, @gildasdongmo, @giuseppesorge, @goto-eof, @larsgrefer, @marcellintabou, @mizzioisf, @mwithi, @nicolaburetta, @nicopunzi, @nlazzer1, @npasquetto, @pranik, @pviotti, @simobasso, @tomek39856, @tommasomoroni, @torrespro, @transifex-integration[bot], @tsognong, @vir8wh47, @xrmx, Aleksey, Antonio Fin, Antonio Fin (afin), Damiano Stanzial, Fernando Espinosa, SilGraz, SteveGT96, gildas, mpelosi, riccardo_costa, uni2grow
</details>
