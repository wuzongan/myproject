//
//  PacketStream.cpp
//  
//
//  Copyright (c) ChunBai. All rights reserved.
//
//

#include "PacketStream.h"
#include <iostream>
#include <algorithm>
#include <stdexcept>
#include <zlib.h>

#define __BIG_ENDIAN		4321
#define __LITTLE_ENDIAN		1234

#if (('4321'>>24)=='1')	/*big*/
#define __BYTE_ORDER __LITTLE_ENDIAN
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
PacketStream * PacketStream::FromOrgData(char * Data,uint16_t S)
{
    PacketStream * pack = new PacketStream(Data,S);
    return  pack;
}

PacketStream * PacketStream::FromPack(char * Data,uint16_t S)
{
    bool bZFlag = Data[0]? true:false;
    S = S-1;
    Data +=1;
    
    if(bZFlag)
    {
        unsigned long UZSize = std::max(S * 3,8192);
        char * UZData = new char[UZSize];
        char * ZData = Data;
        
        int ret = uncompress((unsigned char*)UZData, &UZSize, (unsigned char*)ZData, S);
        if(ret!=0)
        {
            if(ret==Z_BUF_ERROR)
            {
                delete []UZData;
                UZData = new char [UZSize];
                uncompress((unsigned char*)UZData, &UZSize, (unsigned char*)ZData, S);
            }
            else
            {
                delete []UZData;
                return NULL;
            }
        }
        PacketStream * pack = new PacketStream(UZData,UZSize);
        delete []UZData;
        return pack;
    }
    PacketStream * pack = new PacketStream(Data,S);
    return pack;
}
PacketStream * PacketStream::FromCaps(uint16_t Caps)
{
    PacketStream * pack = new PacketStream(Caps);
    return pack;
}


PacketStream::PacketStream(uint16_t capSize)
{
    _modified = false;
    init(capSize);
    _size_z = 0;
    _pack_z = NULL;
	_caps_z = 0;
}

void PacketStream::init(uint16_t capSize)
{
    if(capSize>DEFAULT_CAPSIZE)
    {
        uint16_t newCap = (capSize + HEADER_SIZE) & 0xFF00;
        if((capSize+HEADER_SIZE) & 0x00FF)
        {
            newCap += 0x100;
        }
        
        _capSize = newCap-HEADER_SIZE;
    }
    else
    {
        _capSize = DEFAULT_CAPSIZE;
    }
    
    _pack = new char[_capSize + HEADER_SIZE];
    _pack[ZFLAG_INDEX] = false;
    _curPos = 0;
    _size = 0;
}
uint16_t PacketStream::correctCaps(uint16_t wanSize)
{
    if(wanSize>DEFAULT_CAPSIZE)
    {
        uint16_t newCap = (wanSize + HEADER_SIZE) & 0xFF00;
        if((wanSize+HEADER_SIZE) & 0x00FF)
        {
            newCap += 0x100;
        }
        
        return newCap-HEADER_SIZE;
    }
    else
    {
        return DEFAULT_CAPSIZE;
    }
}
PacketStream::PacketStream(char * orgData,uint16_t size)
{
    
    if(size>MAX_CAPSIZE)
        throw std::length_error("out of capbility");

    init(size);
    
    memcpy(_pack + HEADER_SIZE, orgData, size);
    correctPacketSize(size);
    _pack[ZFLAG_INDEX] = false; // unzlib
    _modified = true;
    _size_z = 0;
    _pack_z = NULL;
	_caps_z = 0;
	_curPos = 0;
}

PacketStream::~PacketStream()
{
    if(_pack)
    {
        delete [] _pack;
    }
    
    if(_pack_z)
    {
        delete []_pack_z;
    }
    
}
void PacketStream::reset()
{
    _size = 0;
    _curPos = 0;
}

bool PacketStream::compress()
{
    //compress
    
    if(!_caps_z)
    {
        _caps_z = correctCaps(std::max(_size * 3,8192));
        _pack_z = new char [_caps_z + HEADER_SIZE];
        
    }
    unsigned long outSize = _caps_z;
    int ret = ::compress((unsigned char *)(_pack_z + HEADER_SIZE), &outSize ,(unsigned char *) data(), _size);
    if(ret!=0)
    {
        if(ret==Z_BUF_ERROR)
        {
            delete []_pack_z;
            uint16_t newCaps = correctCaps(outSize);
            _pack_z = new char [newCaps + HEADER_SIZE];
            ::compress((unsigned char*)(_pack_z + HEADER_SIZE), &outSize, (unsigned char*)data(), _size);
        }
        else
        {
            return false;
        }
        
    }
    _pack_z[ZFLAG_INDEX] = true;
    _size_z = outSize;
    *((uint16_t *)_pack_z) = hton((uint16_t)(_size_z+1));
    _modified = false;
    return true;
}

uint16_t PacketStream::size()
{
    if(_size<NEED_COMPRESS_SIZE)
        return _size + 1;
    
    if (_modified || _pack_z ==NULL)
    {
        compress();
    }
    
    return _size_z + 1;
}


char * PacketStream::pack(bool b_compress)
{
    if(!b_compress){
        return _pack;
    }
    else{
        if(_size<NEED_COMPRESS_SIZE)
        {
            return _pack;
        }
        
        if (_modified || _pack_z ==NULL)
        {
            compress();
        }

    }
        
    return _pack_z;
}

uint16_t PacketStream::packSize(bool b_compress)
{
    if(!b_compress){
        return _size + HEADER_SIZE;
    }
    else{
        if(_size<NEED_COMPRESS_SIZE)
            return _size + HEADER_SIZE;
        
        if (_modified || _pack_z ==NULL)
        {
            compress();
        }
    }
        
    return _size_z + HEADER_SIZE;
}

char * PacketStream::data()
{
    return _pack + HEADER_SIZE;
}

char * PacketStream::curData()
{
    return _pack + HEADER_SIZE + _curPos;
}


bool PacketStream::requireCap(uint16_t reqSize)
{
    uint16_t reqCorrSize = reqSize & 0xFF00;
    if(reqSize & 0xFF00)
    {
        reqCorrSize += 0x100;
    }
    
    if((reqCorrSize + _capSize) >MAX_CAPSIZE)
        return false;
    
    uint16_t newSize = _capSize + HEADER_SIZE + reqCorrSize;
    char * newPack = new char [newSize];
    memcpy(newPack + HEADER_SIZE, _pack + HEADER_SIZE, _size);
    delete []_pack;
    _pack = newPack;
    _capSize = reqCorrSize + _capSize;
    _pack[ZFLAG_INDEX] = false;
    return true;
}
uint16_t PacketStream::readAvailable()
{
    return _size - _curPos;
}

bool PacketStream::setPos(uint16_t pos)
{
    if(pos >= _capSize)
    {
        if(!requireCap(pos - _capSize))
            return false;//out caps
    }

    
    if(pos >= _size)
    {
        correctPacketSize(pos);
    }
    _curPos = pos;
    
    return 0;
}

void PacketStream::correctPacketSize(uint16_t newSize)
{
    _size = newSize;
	newSize++;
    *((uint16_t *)_pack) = hton(newSize);
}

bool PacketStream::readBool(bool & o)
{
    if(readAvailable()>=sizeof(uint8_t))
    {
        o = *((bool*)curData());
        ++_curPos;
        return true;
    }
    else
    {
        return false;
    }
}


bool PacketStream::writeBool(bool i)
{
    uint16_t wantPos = _curPos+ sizeof(uint8_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
        
    }

    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((bool*)curData()) = i;
    
    _curPos = wantPos;
    _modified = true;
    return true;
}


bool PacketStream::readInt8(int8_t &o)
{
    if(readAvailable()>=sizeof(int8_t))
    {
        o = *((int8_t*)curData());
        ++_curPos;
        return true;
    }
    else
    {
        return false;
    }
}


bool PacketStream::writeInt8(int8_t i)
{
    uint16_t wantPos = _curPos+ sizeof(uint8_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
        
    }

    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((uint8_t*)curData()) = i;
    
    _curPos = wantPos;
    _modified = true;    
    return true;
}

bool PacketStream::readUint8(uint8_t & o)
{
    if(readAvailable()>=sizeof(uint8_t))
    {
        o = *((uint8_t*)curData());
        ++_curPos;
        return true;
    }
    else
    {
        return false;
    }
    
}


bool PacketStream::writeUint8(uint8_t i)
{
    uint16_t wantPos = _curPos+ sizeof(uint8_t);
    
    if(wantPos >_capSize)
        return false;//out caps
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((uint8_t*)curData()) = i;
    
    _curPos = wantPos;
    _modified = true;
    return true;
}



bool PacketStream::readInt16(int16_t & o)
{
    if(readAvailable()>=sizeof(int16_t))
    {
        int16_t v = *((int16_t*)curData());
        o = hton(v);
        _curPos +=sizeof(int16_t);
        return true;
    }
    else
    {
        return false;
    }
}
bool PacketStream::writeInt16(int16_t i)
{
    uint16_t wantPos = _curPos+ sizeof(int16_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
        
    }

    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int16_t*)curData()) = hton(i);
    
    _curPos = wantPos;
    _modified = true;    
    return true;
}

bool PacketStream::readUint16(uint16_t & o)
{
    if(readAvailable()>=sizeof(uint16_t))
    {
        uint16_t v = *((uint16_t*)curData());
        o = hton(v);
        _curPos +=sizeof(uint16_t);
        return true;
    }
    else
    {
        return false;
    }
    
}
bool PacketStream::writeUint16(uint16_t i)
{
    uint16_t wantPos = _curPos+ sizeof(uint16_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
        
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((uint16_t*)curData()) = hton(i);
    
    _curPos = wantPos;
    _modified = true;    
    return true;
    
}

bool PacketStream::readInt32(int32_t & o)
{
    if(readAvailable()>=sizeof(int32_t))
    {
        int32_t v = *((int32_t*)curData());
        _curPos +=sizeof(int32_t);
        o = hton(v);
        return true;
    }
    else
    {
        return false;
    }
    
}
bool PacketStream::writeInt32(int32_t i)
{
    uint16_t wantPos = _curPos+ sizeof(int32_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
        {
            return false;//out caps
        }
    }
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int32_t*)curData()) = hton(i);
    
    _curPos = wantPos;
    _modified = true;    
    return true;
  
}

bool PacketStream::readUint32(uint32_t & o)
{
    if(readAvailable()>=sizeof(uint32_t))
    {
        uint32_t v = *((uint32_t*)curData());
        _curPos +=sizeof(uint32_t);
        o = hton(v);
        return true;
    }
    else
    {
        return false;
    }
}

bool PacketStream::writeUint32(uint32_t i)
{
    uint16_t wantPos = _curPos+ sizeof(uint32_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
    }
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((uint32_t*)curData()) = hton(i);
    
    _curPos = wantPos;
    _modified = true;    
    return true;
    
}

bool PacketStream::readInt64(int64_t & o)
{
    if(readAvailable()>=sizeof(int64_t))
    {
		int64_t v = 0;
		memcpy(&v,curData(),sizeof(int64_t));
        _curPos +=sizeof(int64_t);
        o = hton(v);
        return true;
    }
    else
    {
        return false;
    }
}
bool PacketStream::writeInt64(const int64_t& i)
{
    uint16_t wantPos = _curPos+ sizeof(int64_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
        
    }

    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    int64_t v = hton(i);
	memcpy(curData(),&v,sizeof(int64_t));
    
    _curPos = wantPos;
    _modified = true;    
    return true;
}

bool PacketStream::readUint64(uint64_t & o)
{
    if(readAvailable()>=sizeof(uint64_t))
    {
		uint64_t v = 0;
		memcpy(&v,curData(),sizeof(uint64_t));

        _curPos +=sizeof(uint64_t);
        o = hton(v);
        return true;
    }
    else
    {
        return false;
    }
}
bool PacketStream::writeUint64(const uint64_t &i)
{
    uint16_t wantPos = _curPos+ sizeof(uint64_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(0x100))
            return false;//out caps
        
    }

    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    uint64_t v = hton(i);
	memcpy(curData(),&v,sizeof(int64_t));
    
    _curPos = wantPos;
    _modified = true;    
    return true;
}
bool PacketStream::readString(std::string & str)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t len = hton(*((uint16_t*)curData()));
    if(len==0)
    {
        str = "";
		_curPos += sizeof(uint16_t);
        return true;
    }
    uint16_t strSpace = sizeof(uint16_t) + len;
    if(readAvailable()< strSpace)
        return false;
    str=std::string(curData()+sizeof(uint16_t),len);
    
    _curPos += strSpace;
    
    return true;
}
bool PacketStream::writeString(const std::string & str)
{
    uint16_t len = str.size();
    uint16_t wantPos = _curPos+ sizeof(uint16_t) + len;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
        
    }

    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }

    *((uint16_t *)curData()) = hton(len);
    
    char * p = curData()+ sizeof(uint16_t);
    memcpy(p,str.c_str(),len);
    _curPos = wantPos;
    _modified = true;    
    return true;
}

bool PacketStream::readBoolList(std::vector<bool> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace =  size;
    if(readAvailable()< listSpace)
        return false;

    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        bool vl;
        if(!readBool(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
}
bool PacketStream::writeBoolList(const std::vector<bool>& i)
{
   uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeBool(i[j]))
            return false;
    }
    _modified = true;    
    return true;
}

bool PacketStream::readInt8List(std::vector<int8_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t strSpace = size;
    if(readAvailable()< strSpace)
        return false;

    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        int8_t vl;
        if(!readInt8(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
}


bool PacketStream::writeInt8List(const std::vector<int8_t>& i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeInt8(i[j]))
            return false;
    }
    _modified = true;    
    return true;
}
bool PacketStream::readUint8List(std::vector<uint8_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace = size;
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        uint8_t vl;
        if(!readUint8(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
    
}
bool PacketStream::writeUint8List(const std::vector<uint8_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeUint8(i[j]))
            return false;
    }
    _modified = true;    
    return true;

}

bool PacketStream::readInt16List(std::vector<int16_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace =  size*sizeof(int16_t);
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        int16_t vl;
        if(!readInt16(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;

}
bool PacketStream::writeInt16List(const std::vector<int16_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int16_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeInt16(i[j]))
            return false;
    }
    _modified = true;    
    return true;
}

bool PacketStream::readUint16List(std::vector<uint16_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace =  size*sizeof(uint16_t);
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        uint16_t vl;
        if(!readUint16(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
    
}
bool PacketStream::writeUint16List(const std::vector<uint16_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(uint16_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeUint16(i[j]))
            return false;
    }
    _modified = true;    
    return true;
    
}

bool PacketStream::readInt32List(std::vector<int32_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace = size*sizeof(int32_t);
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        int32_t vl;
        if(!readInt32(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
}

bool PacketStream::writeInt32List(const std::vector<int32_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int32_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeInt32(i[j]))
            return false;
    }
    _modified = true;    
    return true;
    
}

bool PacketStream::readUint32List(std::vector<uint32_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace = size*sizeof(uint32_t);
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        uint32_t vl;
        if(!readUint32(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
    
}
bool PacketStream::writeUint32List(const std::vector<uint32_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(uint32_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeUint32(i[j]))
            return false;
    }
    _modified = true;    
    return true;
    
}

bool PacketStream::readInt64List(std::vector<int64_t> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace =  size*sizeof(int64_t);
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        int64_t vl;
        if(!readInt64(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
    
}
bool PacketStream::writeInt64List(const std::vector<int64_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int64_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeInt64(i[j]))
            return false;
    }
    _modified = true;    
    return true;
    
}

bool PacketStream::readUint64List(std::vector<uint64_t> &o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    uint16_t listSpace =  size*sizeof(uint64_t);
    if(readAvailable()< listSpace)
        return false;
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        uint64_t vl;
        if(!readUint64(vl))
            return false;
        o.push_back(vl);
    }
    
    return true;
    
}
bool PacketStream::writeUint64List(const std::vector<uint64_t> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(uint64_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeUint64(i[j]))
            return false;
    }
    _modified = true;    
    return true;
}

bool PacketStream::readStringList(std::vector<std::string> & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
        
    uint16_t size;
    if(!readUint16(size))
        return false;
        
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        std::string str;
        if(!readString(str))
            return false;
        o.push_back(str);
    }
    
    return true;
        
}
bool PacketStream::writeStringList(const std::vector<std::string> & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t);
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeString(i[j]))
            return false;
    }
    
    _modified = true;    
    return true;    
}

bool PacketStream::readPair(Pair<int8_t> & o)
{
    if(readAvailable()>=sizeof(int8_t)*2)
    {
        int8_t v1 = *((int8_t*)curData());
        int8_t v2 = *(((int8_t*)curData())+1);
        _curPos +=sizeof(int8_t)*2;
        o.first = v1;
        o.second = v2;
        return true;
    }
    else
    {
        return false;
    }
}

bool PacketStream::writePair(const Pair<int8_t> & i)
{
    
    uint16_t wantPos = _curPos + sizeof(int8_t) * 2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int8_t*)curData()) = i.first;
    *(((int8_t*)curData())+1) = i.second;
    
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}


bool PacketStream::readPair(Pair<int16_t> & o)
{
    if(readAvailable()>=sizeof(int16_t)*2)
    {
        int16_t v1 = *((int16_t*)curData());
        int16_t v2 = *(((int16_t*)curData())+1);
        _curPos +=sizeof(int16_t)*2;
        o.first = hton(v1);
        o.second = hton(v2);
        return true;
    }
    else
    {
        return false;
    }
}


bool PacketStream::writePair(const Pair<int16_t> & i)
{
    
    uint16_t wantPos = _curPos + sizeof(int16_t) * 2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int16_t*)curData()) = hton(i.first);
    *(((int16_t*)curData())+1) = hton(i.second);
    
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}

bool PacketStream::readPair(Pair<int32_t> & o)
{
    if(readAvailable()>=sizeof(int32_t)*2)
    {
        int32_t v1 = *((int32_t*)curData());
        int32_t v2 = *(((int32_t*)curData())+1);
        _curPos +=sizeof(int32_t)*2;
        o.first = hton(v1);
        o.second = hton(v2);
        return true;
    }
    else
    {
        return false;
    }
}


bool PacketStream::writePair(const Pair<int32_t> & i)
{
    
    uint16_t wantPos = _curPos + sizeof(int32_t) * 2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int32_t*)curData()) = hton(i.first);
    *(((int32_t*)curData())+1) = hton(i.second);
    
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}


bool PacketStream::readPair(Pair<int64_t> & o)
{
    if(readAvailable()>=sizeof(int64_t)*2)
    {
		int64_t v1,v2;
		readInt64(v1);
		readInt64(v2);
        o.first = hton(v1);
        o.second = hton(v2);
        return true;
    }
    else
    {
        return false;
    }
}


bool PacketStream::writePair(const Pair<int64_t> & i)
{
    
    uint16_t wantPos = _curPos + sizeof(int64_t) * 2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }

	int64_t v1 = hton(i.first);
	int64_t v2 = hton(i.second);
	writeInt64(v1);
	writeInt64(v2);
    

    _modified = true;
    
    return  true;
}


bool PacketStream::readTernary(Ternary<int8_t> & o)
{
    if(readAvailable()>=sizeof(int8_t)*3)
    {
        int8_t v1 = *((int8_t*)curData());
        int8_t v2 = *(((int8_t*)curData())+1);
        int8_t v3 = *(((int8_t*)curData())+2);
        _curPos +=sizeof(int8_t)*3;
        o.first = v1;
        o.second = v2;
        o.third = v3;
        return true;
    }
    else
    {
        return false;
    }
}

bool PacketStream::writeTernary(const Ternary<int8_t> & i)
{
    uint16_t wantPos = _curPos + sizeof(int8_t) * 3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int8_t*)curData()) = i.first;
    *(((int8_t*)curData())+1) = i.second;
    *(((int8_t*)curData())+2) = i.third;
    
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}


bool PacketStream::readTernary(Ternary<int16_t> & o)
{
    if(readAvailable()>=sizeof(int16_t)*3)
    {
        int16_t v1 = *((int16_t*)curData());
        int16_t v2 = *(((int16_t*)curData())+1);
        int16_t v3 = *(((int16_t*)curData())+2);
        _curPos +=sizeof(int16_t)*3;
        o.first = hton(v1);
        o.second = hton(v2);
        o.third = hton(v3);
        return true;
    }
    else
    {
        return false;
    }
}

bool PacketStream::writeTernary(const Ternary<int16_t> & i)
{
    uint16_t wantPos = _curPos + sizeof(int16_t) * 3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int16_t*)curData()) = hton(i.first);
    *(((int16_t*)curData())+1) = hton(i.second);
    *(((int16_t*)curData())+2) = hton(i.third);
    
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}


bool PacketStream::readTernary(Ternary<int32_t> & o)
{
    if(readAvailable()>=sizeof(int32_t)*3)
    {
        int32_t v1 = *((int32_t*)curData());
        int32_t v2 = *(((int32_t*)curData())+1);
        int32_t v3 = *(((int32_t*)curData())+2);
        _curPos +=sizeof(int32_t)*3;
        o.first = hton(v1);
        o.second = hton(v2);
        o.third = hton(v3);
        return true;
    }
    else
    {
        return false;
    }
}


bool PacketStream::writeTernary(const Ternary<int32_t> & i)
{
    uint16_t wantPos = _curPos + sizeof(int32_t) * 3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    *((int32_t*)curData()) = hton(i.first);
    *(((int32_t*)curData())+1) = hton(i.second);
    *(((int32_t*)curData())+2) = hton(i.third);
    
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}



bool PacketStream::readTernary(Ternary<int64_t> & o)
{
    if(readAvailable()>=sizeof(int64_t)*3)
    {
		int64_t v1,v2,v3;
		readInt64(v1);
		readInt64(v2);
		readInt64(v3);

        o.first = hton(v1);
        o.second = hton(v2);
        o.third = hton(v3);
        return true;
    }
    else
    {
        return false;
    }
}



bool PacketStream::writeTernary(const Ternary<int64_t> & i)
{
    uint16_t wantPos = _curPos + sizeof(int64_t) * 3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
	int64_t v1 = hton(i.first);
	int64_t v2 = hton(i.second);
	int64_t v3 = hton(i.third);
	writeInt64(v1);
	writeInt64(v2);
	writeInt64(v3);
    _curPos = wantPos;
    _modified = true;
    
    return  true;
}


bool PacketStream::readPairList(std::vector<Pair<int8_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Pair<int8_t> v;
        if(!readPair(v))
            return false;
        o.push_back(v);
    }
    
    return true;
        
}
bool PacketStream::writePairList(const std::vector<Pair<int8_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int8_t) *2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writePair(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
}


bool PacketStream::readPairList(std::vector<Pair<int16_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Pair<int16_t> v;
        if(!readPair(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}


bool PacketStream::writePairList(const std::vector<Pair<int16_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int16_t) *2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writePair(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
    
}


bool PacketStream::readPairList(std::vector<Pair<int32_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Pair<int32_t> v;
        if(!readPair(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}
bool PacketStream::writePairList(const std::vector<Pair<int32_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int32_t) *2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writePair(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
    
}


bool PacketStream::readPairList(std::vector<Pair<int64_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Pair<int64_t> v;
        if(!readPair(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}

bool PacketStream::writePairList(const std::vector<Pair<int64_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int64_t) *2;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writePair(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
    
}


bool PacketStream::readTernaryList(std::vector<Ternary<int8_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Ternary<int8_t> v;
        if(!readTernary(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}

bool PacketStream::writeTernaryList(const std::vector<Ternary<int8_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int8_t) *3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeTernary(i[j]))
            return false;
    }
    _modified = true;
    return true;
}




bool PacketStream::readTernaryList(std::vector<Ternary<int16_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Ternary<int16_t> v;
        if(!readTernary(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}

bool PacketStream::writeTernaryList(const std::vector<Ternary<int16_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int16_t) *3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeTernary(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
}


bool PacketStream::readTernaryList(std::vector<Ternary<int32_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Ternary<int32_t> v;
        if(!readTernary(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}

bool PacketStream::writeTernaryList(const std::vector<Ternary<int32_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int32_t) *3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeTernary(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
}


bool PacketStream::readTernaryList(std::vector<Ternary<int64_t> > & o)
{
    if(readAvailable()<sizeof(uint16_t))
    {
        return false;
    }
    
    uint16_t size;
    if(!readUint16(size))
        return false;
    
    if(size==0)
    {
        o.clear();
        return true;
    }
    
    o.clear();
    for (uint16_t i=0; i< size; ++i)
    {
        Ternary<int64_t> v;
        if(!readTernary(v))
            return false;
        o.push_back(v);
    }
    
    return true;
    
}

bool PacketStream::writeTernaryList(const std::vector<Ternary<int64_t> > & i)
{
    uint16_t count = i.size();
    
    uint16_t wantPos = _curPos + sizeof(uint16_t) + count*sizeof(int64_t) *3;
    
    if(wantPos >_capSize)
    {
        if(!requireCap(wantPos - _capSize))
            return false;//out caps
    }
    
    if(wantPos>=_size)
    {
        correctPacketSize(wantPos);
    }
    
    if(!writeUint16(count))
        return false;
    for(uint16_t j=0;j<count;j++)
    {
        if(!writeTernary(i[j]))
            return false;
    }
    _modified = true;
    return true;
    
}

