//
//  BytesBuffer.cpp
//  cb_v1
//
//  Created by wuzongan on 13-9-10.
//
//

#include "BytesBuffer.h"
#include <string.h>
bool BytesBufferU16::requireCaps(uint16_t newCaps,bool discard)
{
    uint16_t cap = newCaps;
    uint16_t newCap = cap & 0xFF00;
    if((cap) & 0x00FF)
    {
        newCap += 0x100;
    }
    //不超过原来的大小
    if(newCap<_cap)
    {
        return true;
    }
    
    char * newD = new char [newCap];
    if(!newD)
        return false;
    
    if(!discard)
    {
        memcpy(newD, _d, _s);
    }
    
    delete []_d;
    _d = newD;
    _cap = newCap;
    
    return true;
}
BytesBufferU16::BytesBufferU16(uint16_t cap)
{
    uint16_t newCap = cap & 0xFF00;
    if((cap) & 0x00FF)
    {
        newCap += 0x100;
    }
    _d = new char[newCap];
    _cap = newCap;
    _s = 0;
}
BytesBufferU16::BytesBufferU16(char * d,uint16_t s)
{
    uint16_t cap = s;
    uint16_t newCap = cap & 0xFF00;
    if((cap) & 0x00FF)
    {
        newCap += 0x100;
    }
    _d = new char[newCap];
    _cap = newCap;
    _s = s;
    memcpy(_d,d,s);
}
bool BytesBufferU16::copy(BytesBufferU16 * src)
{
    //尝试申请新大小
    if(requireCaps(src->size(),true))
    {
        memcpy(_d,src->data(),src->size());
        _s = src->size();
	}
    else{
        return false;
	}
    return true;
}
bool BytesBufferU16::copy(char * d,uint16_t s)
{
    //尝试申请新大小
    if(requireCaps(s,true))
    {
        memcpy(_d,d,s);
        _s = s;
    }
    else{
        return false;
	}
    return true;
    
}
bool BytesBufferU16::append(BytesBufferU16 * src)
{
    uint16_t s = src->size();
    char * d = src->data();
    if(!d || !s)
        return true;
    
    if(requireCaps(s + _s,false))
    {
        memcpy(_d+_s,d,s);
        _s = _s+s;
    }
    else
        return false;
    return true;
    
}
bool BytesBufferU16::append(char * d,uint16_t s)
{
    if(!d || !s)
        return true;
    
    if(requireCaps(s + _s,false))
    {
        memcpy(_d+_s,d,s);
        _s = _s+s;
    }
    else
        return false;
    return true;
    
}
BytesBufferU16::~BytesBufferU16()
{
    if(_d)
        delete []_d;
}
uint16_t BytesBufferU16::capcity()
{
    return _cap;
}
uint16_t BytesBufferU16::size()
{
    return _s;
}
char *   BytesBufferU16::data()
{
    return _d;
}
