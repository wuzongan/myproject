/****************************************************************************
 Copyright (c) 2010 cocos2d-x.org

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#ifndef __CCPOINTARRAY_H__
#define __CCPOINTARRAY_H__

#include "platform/CCPlatformMacros.h"
#include "CCObject.h"
#include "CCGeometry.h"
#include <vector>

using namespace std;

NS_CC_BEGIN

/** An Array that contain points.
 */
class CC_DLL CCPointArray : public CCObject
{
public:

    /** creates and initializes a Points array with capacity */
    static CCPointArray* create(unsigned int capacity);

    ~CCPointArray(void);
    CCPointArray(void);

    /** initializes a Catmull Rom config with a capacity hint */
    bool initWithCapacity(unsigned int capacity);

    /** appends a point */
    void add(CCPoint point);

    /** inserts a point at index */
    void insert(CCPoint &point, unsigned int index);

    /** replaces an existing point at index */
    void replace(CCPoint &point, unsigned int index);

    /** get the value of a point at a given index */
    const CCPoint get(unsigned int index);

    /** deletes a point at a given index */
    void remove(unsigned int index);

    /** deletes all points */
    void removeAll(void);

    /** returns the number of objects of the point array */
    unsigned int count(void);

    /** returns a new copy of the array reversed. User is responsible for releasing this copy */
    CCPointArray* reverse(void);

    /** reverse the current point array inline, without generating a new one */
    void reverseInline(void);

    virtual CCObject* copyWithZone(CCZone *zone);

    const std::vector<CCPoint*>* getPoints(void);
    void setPoints(std::vector<CCPoint*> *points);

    /** returns a array of points, User must delete [] this array */
    CCPoint *fetchPoints(void);

private:
    /** Array that contains the points */
    std::vector<CCPoint*> *m_pPoints;
};

NS_CC_END

#endif // __CCPOINTARRAY_H__
