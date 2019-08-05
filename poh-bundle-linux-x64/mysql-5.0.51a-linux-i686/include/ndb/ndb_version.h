/* Copyright (C) 2003 MySQL AB

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */

#ifndef NDB_VERSION_H
#define NDB_VERSION_H

#include <ndb_global.h>
#include <version.h>

/* NDB build version */
#define NDB_VERSION_BUILD 51

/* NDB major version */
#define NDB_VERSION_MAJOR 5

/* NDB minor version */
#define NDB_VERSION_MINOR 0

/* NDB status version */
#define NDB_VERSION_STATUS ""


#define MAKE_VERSION(A,B,C) (((A) << 16) | ((B) << 8)  | ((C) << 0))

#define NDB_VERSION_D MAKE_VERSION(NDB_VERSION_MAJOR, NDB_VERSION_MINOR, NDB_VERSION_BUILD)
#define NDB_VERSION_STRING_BUF_SZ 100
#ifdef __cplusplus
extern "C"
#else
extern
#endif
char ndb_version_string_buf[NDB_VERSION_STRING_BUF_SZ];
#define NDB_VERSION_STRING (getVersionString(NDB_VERSION, NDB_VERSION_STATUS, \
                                             ndb_version_string_buf, \
                                             sizeof(ndb_version_string_buf)))

#define NDB_VERSION ndbGetOwnVersion()

/**
 * Version id 
 *
 *  Used by transporter and when communicating with
 *     managment server
 */
/*#define NDB_VERSION_ID 0*/

#define NDBD_INCL_NODECONF_VERSION_4 MAKE_VERSION(4,1,17)
#define NDBD_INCL_NODECONF_VERSION_5 MAKE_VERSION(5,0,18)

#define NDBD_DICT_LOCK_VERSION_5 MAKE_VERSION(5,0,23)

#define NDBD_UPDATE_FRAG_DIST_KEY_50 MAKE_VERSION(5,0,26)
#define NDBD_UPDATE_FRAG_DIST_KEY_51 MAKE_VERSION(5,1,12)

#define NDBD_QMGR_SINGLEUSER_VERSION_5 MAKE_VERSION(5,0,25)

#endif
 
