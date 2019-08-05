/* Copyright (C) 2002-2006 MySQL AB

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; version 2
   of the License.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */

#ifndef _my_handler_h
#define _my_handler_h

#include "my_base.h"
#include "m_ctype.h"
#include "myisampack.h"

typedef struct st_HA_KEYSEG		/* Key-portion */
{
  CHARSET_INFO *charset;
  uint32 start;				/* Start of key in record */
  uint32 null_pos;			/* position to NULL indicator */
  uint16 bit_pos;                       /* Position to bit part */
  uint16 flag;
  uint16 length;			/* Keylength */
  uint8  type;				/* Type of key (for sort) */
  uint8  language;
  uint8  null_bit;			/* bitmask to test for NULL */
  uint8  bit_start,bit_end;		/* if bit field */
  uint8  bit_length;                    /* Length of bit part */
} HA_KEYSEG;

#define get_key_length(length,key) \
{ if ((uchar) *(key) != 255) \
    length= (uint) (uchar) *((key)++); \
  else \
  { length=mi_uint2korr((key)+1); (key)+=3; } \
}

#define get_key_length_rdonly(length,key) \
{ if ((uchar) *(key) != 255) \
    length= ((uint) (uchar) *((key))); \
  else \
  { length=mi_uint2korr((key)+1); } \
}

#define get_key_pack_length(length,length_pack,key) \
{ if ((uchar) *(key) != 255) \
  { length= (uint) (uchar) *((key)++); length_pack=1; }\
  else \
  { length=mi_uint2korr((key)+1); (key)+=3; length_pack=3; } \
}

#define store_key_length_inc(key,length) \
{ if ((length) < 255) \
  { *(key)++=(length); } \
  else \
  { *(key)=255; mi_int2store((key)+1,(length)); (key)+=3; } \
}

#define get_rec_bits(bit_ptr, bit_ofs, bit_len) \
  (((((uint16) (bit_ptr)[1] << 8) | (uint16) (bit_ptr)[0]) >> (bit_ofs)) & \
   ((1 << (bit_len)) - 1))

#define set_rec_bits(bits, bit_ptr, bit_ofs, bit_len) \
{ \
  (bit_ptr)[0]= ((bit_ptr)[0] & ~(((1 << (bit_len)) - 1) << (bit_ofs))) | \
                ((bits) << (bit_ofs)); \
  if ((bit_ofs) + (bit_len) > 8) \
    (bit_ptr)[1]= ((bit_ptr)[1] & ~((1 << ((bit_len) - 8 + (bit_ofs))) - 1)) | \
                  ((bits) >> (8 - (bit_ofs))); \
}

#define clr_rec_bits(bit_ptr, bit_ofs, bit_len) \
  set_rec_bits(0, bit_ptr, bit_ofs, bit_len)

extern int mi_compare_text(CHARSET_INFO *, uchar *, uint, uchar *, uint ,
			   my_bool, my_bool);
extern int ha_key_cmp(register HA_KEYSEG *keyseg, register uchar *a,
		      register uchar *b, uint key_length, uint nextflag,
		      uint *diff_pos);

extern HA_KEYSEG *ha_find_null(HA_KEYSEG *keyseg, uchar *a);

#endif /* _my_handler_h */
