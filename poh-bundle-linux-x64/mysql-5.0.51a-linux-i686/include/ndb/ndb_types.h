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

/**
 * @file ndb_types.h
 */

#ifndef NDB_TYPES_H
#define NDB_TYPES_H

#if defined(_WIN32) || defined(__WIN32__) || defined(WIN32) || defined(_WIN64)
#define NDB_SIZEOF_CHARP SIZEOF_CHARP
#define NDB_SIZEOF_CHAR SIZEOF_CHAR
#define NDB_SIZEOF_SHORT 2
#define NDB_SIZEOF_INT SIZEOF_INT
#define NDB_SIZEOF_LONG SIZEOF_LONG
#define NDB_SIZEOF_LONG_LONG SIZEOF_LONG_LONG
typedef unsigned __int64 Uint64;
typedef   signed __int64 Int64;
#else
#define NDB_SIZEOF_CHARP 4
#define NDB_SIZEOF_CHAR 1
#define NDB_SIZEOF_INT 4
#define NDB_SIZEOF_SHORT 2
#define NDB_SIZEOF_LONG 4
#define NDB_SIZEOF_LONG_LONG 8
typedef unsigned long long Uint64;
typedef   signed long long Int64;
#endif

typedef   signed char  Int8;
typedef unsigned char  Uint8;
typedef   signed short Int16;
typedef unsigned short Uint16;
typedef   signed int   Int32;
typedef unsigned int   Uint32;

typedef unsigned int UintR;

#ifdef __SIZE_TYPE__
  typedef __SIZE_TYPE__ UintPtr;
#elif NDB_SIZEOF_CHARP == 4
  typedef Uint32 UintPtr;
#elif NDB_SIZEOF_CHARP == 8
  typedef Uint64 UintPtr;
#else
  #error "Unknown size of (char *)"
#endif

#if ! (NDB_SIZEOF_CHAR == 1)
#error "Invalid define for Uint8"
#endif

#if ! (NDB_SIZEOF_SHORT == 2)
#error "Invalid define for Uint16"
#endif

#if ! (NDB_SIZEOF_INT == 4)
#error "Invalid define for Uint32"
#endif

#if ! (NDB_SIZEOF_LONG_LONG == 8)
#error "Invalid define for Uint64"
#endif

#include "ndb_constants.h"

#endif
