//
//  BytesBuffer.h
//  cb_v1
//
//  Created by wuzongan on 13-9-10.
//
//

#ifndef __cb_v1__BytesBuffer__
#define __cb_v1__BytesBuffer__
#include <stdint.h>
class BytesBufferU16
{
private:
    char * _d;
    uint16_t _cap;
    uint16_t _s;
    bool requireCaps(uint16_t newCaps,bool discard);
public:
    BytesBufferU16(uint16_t cap);
    BytesBufferU16(char * d,uint16_t s);
    bool copy(BytesBufferU16 * src);
    bool copy(char * d,uint16_t s);
    bool append(BytesBufferU16 * src);
    bool append(char * d,uint16_t s);
    virtual ~BytesBufferU16();
    uint16_t capcity();
    uint16_t size();
    char *   data();
};

#endif /* defined(__cb_v1__BytesBuffer__) */
