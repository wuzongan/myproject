//
//  NetFilter.h
//  cb_v1
//
//  Created by wuzongan on 13-9-10.
//
//

#ifndef __cb_v1__NetFilter__
#define __cb_v1__NetFilter__
#include "BytesBuffer.h"
class NetFilter
{
public:
    static bool toNet(BytesBufferU16 * ib,BytesBufferU16 ** ob);
    static bool fromNet(BytesBufferU16 * ib,BytesBufferU16 ** ob);
};

#endif /* defined(__cb_v1__NetFilter__) */
