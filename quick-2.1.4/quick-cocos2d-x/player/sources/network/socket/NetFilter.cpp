//
//  NetFilter.cpp
//  cb_v1
//
//  Created by wuzongan on 13-9-10.
//
//

#include "NetFilter.h"
#include <zlib.h>
#include <algorithm>

#define __BIG_ENDIAN		4321
#define __LITTLE_ENDIAN		1234

#if (('4321'>>24)=='1')	/*big*/
#define __BYTE_ORDER __BIG_ENDIAN
#else
#define __BYTE_ORDER __LITTLE_ENDIAN
#endif

static int64_t hton(int64_t v)
{
#if (__BYTE_ORDER == __LITTLE_ENDIAN)
    uint64_t h3 = (0xFF00000000000000LL & v);
    uint64_t h2 = (0xFF000000000000LL & v);
    uint64_t h1 = (0xFF0000000000LL & v);
    uint64_t h0 = (0xFF00000000LL & v);
    
    uint64_t l3 = (0xFF000000 & v);
    uint64_t l2 = (0x00FF0000 & v);
    uint64_t l1 = (0x0000FF00 & v);
    uint64_t l0 = (0x000000FF & v);
    
    return int64_t((h3 >> 56 )|(h2>>40) | (h1>>24) | (h0>>8)|
                   (l3 << 8 )|(l2<<24) | (l1<<40) | (l0<<56));
#else
	return v;
#endif
}
static uint64_t hton(uint64_t v)
{
#if (__BYTE_ORDER == __LITTLE_ENDIAN)
    uint64_t h3 = (0xFF00000000000000LL & v);
    uint64_t h2 = (0xFF000000000000LL & v);
    uint64_t h1 = (0xFF0000000000LL & v);
    uint64_t h0 = (0xFF00000000LL & v);
    
    uint64_t l3 = (0xFF000000 & v);
    uint64_t l2 = (0x00FF0000 & v);
    uint64_t l1 = (0x0000FF00 & v);
    uint64_t l0 = (0x000000FF & v);
    
    return (h3 >> 56 )|(h2>>40) | (h1>>24) | (h0>>8)|
    (l3 << 8 )|(l2<<24) | (l1<<40) | (l0<<56);
#else
	return v;
#endif
}

static int32_t hton(int32_t v)
{
#if (__BYTE_ORDER == __LITTLE_ENDIAN)
    uint32_t hh = (0xFF000000 & v);
    uint32_t hl = (0xFF0000 & v);
    uint32_t lh = (0xFF00 & v);
    uint32_t ll = (0xFF & v);
    return int32_t((hh >> 24 )| (hl >> 8) | (lh << 8 ) | (ll<<24));
#else
	return v;
#endif
}

static uint32_t hton(uint32_t v)
{
#if (__BYTE_ORDER == __LITTLE_ENDIAN)
    uint32_t hh = (0xFF000000 & v);
    uint32_t hl = (0xFF0000 & v);
    uint32_t lh = (0xFF00 & v);
    uint32_t ll = (0xFF & v);
    return (hh >> 24 )| (hl >> 8) | (lh << 8 ) | (ll<<24);
#else
	return v;
#endif
}

static int16_t hton(int16_t v)
{
#if (__BYTE_ORDER == __LITTLE_ENDIAN)
    uint16_t h = (0xFF00 & v);
    uint16_t l = (0xFF & v);
    return int16_t((h>> 8) | (l << 8));
#else
	return v;
#endif
}

static uint16_t hton(uint16_t v)
{
#if (__BYTE_ORDER == __LITTLE_ENDIAN)
    return (v>> 8) | (v << 8);
#else
	return v;
#endif
}
bool NetFilter::toNet(BytesBufferU16 * ib,BytesBufferU16 ** ob)
{
    if(0==ib)
        return false;
    
    char * idb = ib->data();
    uint16_t s = ib->size();
    
    char zflg = 0;
    uint16_t ods = s; //output data size
    char * odb = NULL;
    char * tmpBuff = NULL;
    //1 先看是否需要压缩( >= 64字节压缩)
    if(s<64)
    {
        odb = idb;
    }
    else
    {
        //2 压缩
        unsigned long outSize = std::max(s * 3,8192);
        tmpBuff = new char[outSize];
        int ret = ::compress((unsigned char *)tmpBuff, &outSize ,(unsigned char *)idb , s);
        if(ret!=0)
        {
            delete []tmpBuff;
            if(ret==Z_BUF_ERROR)
            {
                tmpBuff = new char [outSize];
                ::compress((unsigned char *)tmpBuff, &outSize ,(unsigned char *)idb , s);
            }
            else
            {
                return false;
            }    
        }
        zflg = 1;
        ods = outSize;
        odb = tmpBuff;
    }
    
    // 3组织打包
    BytesBufferU16 * newBuf = new BytesBufferU16(ods + 3);
    uint16_t ps = hton(uint16_t(ods+1));
    newBuf->append((char * ) &ps, sizeof(uint16_t));
    newBuf->append(&zflg,1);
    newBuf->append(odb,ods);
    
    //释放临时变量
    if(tmpBuff)
        delete []tmpBuff;
    
    * ob = newBuf;
    return true;
}

bool NetFilter::fromNet(BytesBufferU16 * ib,BytesBufferU16 ** ob)
{
    // 1 判断是否需要解压缩
    char * idb = ib->data();
    uint16_t s = ib->size();
    
    uint16_t ods = s - 3; //output data size
    char * odb = idb + 3; //output pure data
    char * tmpBuff = NULL;
    char zflg = idb[2];
    if(zflg)
    {
        //2 解压缩

        unsigned long UZSize = std::max(s * 3,8192);
		do
		{
	        tmpBuff = new char[UZSize];
		    int ret = uncompress((unsigned char*)tmpBuff, &UZSize, (unsigned char*)odb, ods);
			if(ret!=0)
			{
				delete []tmpBuff;
				if(ret==Z_BUF_ERROR)
				{
					UZSize<<=1;
					continue;
				}
				else
					return false;
		    }
			else
				break;

		}while(1);
        odb = tmpBuff;
        ods = UZSize;
    }
    
    //设置到输出缓冲区
    BytesBufferU16 * newBuf = new BytesBufferU16(odb,ods);
    
    //释放临时变量
    if(tmpBuff)
        delete []tmpBuff;
    
    *ob = newBuf;
    return true;
}
