2.0.0 (12/2022)
--------------
- interactive menu
- new features
- see help for documentation and updates

1.0.0 (09/2021)
--------------
- reworked entirely oh.sh startup script
- unified for x32, x64, Portable and Client version of Open Hospital
- centralized variable configuration in the script
- added multiple options - see oh.sh -h for more information

0.0.6 (02/2020)
--------------
- added lower_case_table_names = 1 in [mysqld] to avoid cross-OS data transfer problems 

0.0.5 (08/2019)
--------------
- added support for x64 architecture for Linux
- fixed startup script to wait for the MySQL socket file to be created

0.0.3 (08/05/2017)
------------------
- Added support for high dpi monitor in launchers (works only with Java6)
- Enhanced Ant build in order to build portable versions too (only OpenHospital part)
- Updated scripts for POH (windows version use always port 3307 for MySQL).

0.0.2 (13/04/2008)
------------------
- the shell script oh.sh looks for a free port to run MySQL server starting from port 3306
- created README, TODO and this Changelog file in the root directory of the package
- included the LICENSE file containing the GNU General Public License in the oh/ directory related to Open Hospital itself.
- suppressed MySQL log messages on stdout when launching oh.sh

0.0.1 (30/03/2008)
------------------
- the very first release
