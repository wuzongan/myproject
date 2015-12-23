#pragma once
#include "cocos2d.h"
USING_NS_CC;


class GameMap : public CCTMXTiledMap {
public:
	CCArray *allCollisionPolygon;
	CCArray *allCollisionPolyline;
	CCArray *world;
    
    // collisitn vector
    CCPoint forwardVec;
    CCPoint backwardVec;
    CCPoint intersectPoint;
    CCLayerColor *layer;
	
public:
	GameMap(void);
    virtual ~GameMap(void);
    static GameMap* gameMapWithTMXFile(const char *tmxFile);
	
    
	/*************************************************************************/
	
    // 判断点是否在碰撞区
	bool isPointInCollisionRegion(CCPoint p, CCSize roleSize);
    // 判断点是否在碰撞区(返回可行走坐标)
    CCPoint getPointInCollisionRegion(CCPoint p, CCSize roleSize);
	// 判断路线是否与碰撞区相交
	bool isWaylineIntersectCollisionLine(CCPoint origionPos, CCPoint destPos);

	CCArray* stringToPoints(char *s);
	CCPoint stringToPoint(char *s);
	CCPoint adjustPosition(CCPoint position);
	CCPoint tileCoordForPosition(CCPoint position);
	CCPoint positionForTileCoord(CCPoint tileCoord);

    //震屏效果
    void vibrationScreen();
    void screedEnd();

    void getCollisions();
    // 判断点是否在边界内
	bool isPointInWorld(CCPoint p);
};

