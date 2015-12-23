#include "GameMap.h"


GameMap::GameMap(void) : layer(NULL)
{
	allCollisionPolygon = new CCArray;
	allCollisionPolyline = new CCArray;
    world = new CCArray;
}

GameMap::~GameMap(void)
{
	allCollisionPolygon->release();
	allCollisionPolyline->release();
	world->release();
}

GameMap* GameMap::gameMapWithTMXFile(const char *tmxFile)
{
    GameMap *pRet = new GameMap();
    if(CCTMXData::sharedTMXData()){
        CCTMXData::sharedTMXData()->clean();
    }
    if (pRet->initWithTMXFile(tmxFile))
    {
        pRet->autorelease();
        pRet->getCollisions();
        return pRet;
    }
    CC_SAFE_DELETE(pRet);
    return NULL;
}

/**
 *	保存的是点坐标
 */
void GameMap::getCollisions() {
    
	// collision object group
    CCTMXObjectGroup *og = this->objectGroupNamed("collision");
    if(!og){
        return;
    }
	CCArray *allCollision = og->getObjects();
    
	// save points to a array, then save this array to polygon/polyline array
	for(int i = 0; i < allCollision->count(); i++) {
        
		CCDictionary *dic = (CCDictionary *)allCollision->objectAtIndex(i);
		CCString *polygonPoints = (CCString *)dic->objectForKey("polygonPoints");
		CCString *polylinePoints = (CCString *)dic->objectForKey("polylinePoints");
        
		int x = ((CCString *)dic->objectForKey("x"))->intValue();
		int y = ((CCString *)dic->objectForKey("y"))->intValue();
        
		CCArray *allPoints = NULL;
        
		if(polygonPoints) {
			allPoints = this->stringToPoints((char *)polygonPoints->getCString());
			if (!strcmp(((CCString *)dic->objectForKey("name"))->getCString(), "world")) {
				world = allPoints;
				world->retain();
				CCAssert(world, "world is null");
			} else {
				allCollisionPolygon->addObject(allPoints);
			}
			
		}
		if(polylinePoints) {
			allPoints = this->stringToPoints((char *)polylinePoints->getCString());
			allCollisionPolyline->addObject(allPoints);
		}
		
		for(int i = 0; i < allPoints->count(); i++) {
			CCPoint *p = (CCPoint *)allPoints->objectAtIndex(i);
			p->x += x;
			p->y *= -1;
			p->y += y;
			
			*p = CC_POINT_PIXELS_TO_POINTS(*p);
		}
	}
}


bool GameMap::isPointInWorld(cocos2d::CCPoint p) {
	
	bool bRet = false;
	CCPoint *p1 = (CCPoint *)world->objectAtIndex(world->count()-1);
	for (int i = 0; i < world->count(); i++) {
		CCPoint *p2 = (CCPoint *)world->objectAtIndex(i);
		if (((p2->y < p.y && p1->y >= p.y) || (p1->y < p.y && p2->y >= p.y))
			&& (p2->x <= p.x || p1->x <= p.x)) {
			
			if (p2->x + (p.y - p2->y) * (p1->x - p2->x) / (p1->y - p2->y) < p.x) {
				bRet = !bRet;
			}
		}
		p1 = p2;
	}
	return bRet;
}

bool GameMap::isPointInCollisionRegion(CCPoint p, CCSize roleSize) {
	
	// 地图尺寸外
	CCSize s = roleSize;
	if (p.x < s.width/2 || p.x > this->getContentSize().width-s.width/2 ||
		p.y < 0 || p.y > this->getContentSize().height-s.height) {
		return true;
	}
	
	// world外
	if (!isPointInWorld(p)) {
		return true;
	}

	bool bRet = false;
	for(int i = 0; i < allCollisionPolygon->count(); i++) {
		
		CCArray *pointArray = (CCArray *)allCollisionPolygon->objectAtIndex(i);
		CCPoint *p1 = (CCPoint *)pointArray->objectAtIndex(pointArray->count()-1);
		for (int i = 0; i < pointArray->count(); i++) {
			CCPoint *p2 = (CCPoint *)pointArray->objectAtIndex(i);
			if (((p2->y < p.y && p1->y >= p.y) || (p1->y < p.y && p2->y >= p.y))
				&& (p2->x <= p.x || p1->x <= p.x)) {
                if (p2->x + (p.y - p2->y) / (p1->y - p2->y) * (p1->x - p2->x) < p.x) {
                    bRet = !bRet;
                }
			}
			p1 = p2;
		}
        
		if(bRet == true) {
			break;
		}
	}
	return bRet;
}

CCPoint GameMap::getPointInCollisionRegion(CCPoint p, CCSize roleSize) {
	
	// 地图尺寸外
	CCSize s = roleSize;
	if (p.x < s.width/2 || p.x > this->getContentSize().width-s.width/2 ||
		p.y < 0 || p.y > this->getContentSize().height-s.height) {
        if(p.x < s.width/2){
            p = ccp(s.width/2 + 1, p.y);
        }
        else if(p.x > this->getContentSize().width-s.width/2){
            p = ccp(this->getContentSize().width-s.width/2 - 1, p.y);
        }
        else if(p.y < 0){
            p = ccp(p.x, 1);
        }
        else if(p.y > this->getContentSize().height-s.height){
            p = ccp(p.x, this->getContentSize().height-s.height - 1);
        }
	}
	
	// world外
	if (!isPointInWorld(p)) {
//		return true;
	}
    
	bool bRet = false;
	for(int i = 0; i < allCollisionPolygon->count(); i++) {
		
		CCArray *pointArray = (CCArray *)allCollisionPolygon->objectAtIndex(i);
		CCPoint *p1 = (CCPoint *)pointArray->objectAtIndex(pointArray->count()-1);
		for (int i = 0; i < pointArray->count(); i++) {
			CCPoint *p2 = (CCPoint *)pointArray->objectAtIndex(i);
			if (((p2->y < p.y && p1->y >= p.y) || (p1->y < p.y && p2->y >= p.y))
				&& (p2->x <= p.x || p1->x <= p.x)) {
                if (p2->x + (p.y - p2->y) / (p1->y - p2->y) * (p1->x - p2->x) < p.x) {
                    bRet = !bRet;
                    if (p.y + p1->y > this->getContentSize().height){
                        return ccp(p.x, p2->y);
                    }else{
                        return ccp(p.x, p1->y);
                    }
                }
			}
			p1 = p2;
		}
        
		if(bRet == true) {
			break;
		}
	}
	return p;
}

bool GameMap::isWaylineIntersectCollisionLine(CCPoint origionPos, CCPoint destPos) {
    
    //	CCSize s = Hero::Instance()->getContentSize();
    //	if (destPos.x < s.width/2 || destPos.x > this->getContentSize().width-s.width/2 ||
    //		destPos.y < 0 || destPos.y > this->getContentSize().height-s.height) {
    //		return true;
    //	}
    //
	
	/**
	 *	world collision
	 */
	CCPoint *p1 = (CCPoint *)world->objectAtIndex(world->count()-1);
	CCPoint *p2;
	for(int i = 0; i < world->count(); i++) {
		p2 = (CCPoint *)world->objectAtIndex(i);
		if(ccpSegmentIntersect(*p1, *p2, origionPos, destPos)) {
			/**
			 *	碰撞线方向向量
			 */
			intersectPoint = ccpIntersectPoint(*p1, *p2, origionPos, destPos);
			if (p1->x <= p2->x) {
				forwardVec = *p2;
				backwardVec = *p1;
			} else {
				forwardVec = *p1;
				backwardVec = *p2;
			}
			
			return true;
		}
		p1 = p2;
	}
    
    
	
	/**
	 *	collistion area in world
	 */
	for(int i = 0; i < allCollisionPolygon->count(); i++) {
		CCArray *points = (CCArray *)allCollisionPolygon->objectAtIndex(i);
		p1 = (CCPoint *)points->objectAtIndex(points->count()-1);
		for(int j = 0; j < points->count(); j++) {
			p2 = (CCPoint *)points->objectAtIndex(j);
			if(ccpSegmentIntersect(*p1, *p2, origionPos, destPos)) {
				/**
				 *	碰撞线方向向量
				 */
				intersectPoint = ccpIntersectPoint(*p1, *p2, origionPos, destPos);
				if (p1->x <= p2->x) {
					forwardVec = ccpSub(*p2, intersectPoint);
					backwardVec = ccpSub(*p1, intersectPoint);
				} else {
					forwardVec = ccpSub(*p1, intersectPoint);
					backwardVec = ccpSub(*p2, intersectPoint);
				}
				return true;
			}
			p1 = p2;
		}
	}
    
	/**
	 *	polyline collision
	 */
	for(int i = 0; i < allCollisionPolyline->count(); i++) {
        
		CCArray *points = (CCArray *)allCollisionPolyline->objectAtIndex(i);
		p1 = (CCPoint *)points->objectAtIndex(0);
		for(int j = 1; j < points->count(); j++) {
			p2 = (CCPoint *)points->objectAtIndex(j);
			if(ccpSegmentIntersect(*p1, *p2, origionPos, destPos)) {
				/**
				 *	碰撞线方向向量
				 */
				intersectPoint = ccpIntersectPoint(*p1, *p2, origionPos, destPos);
				if (p1->x <= p2->x) {
					forwardVec = ccpSub(*p2, intersectPoint);
					backwardVec = ccpSub(*p1, intersectPoint);
				} else {
					forwardVec = ccpSub(*p1, intersectPoint);
					backwardVec = ccpSub(*p2, intersectPoint);
				}
				return true;
			}
			p1 = p2;
		}
	}
	
	forwardVec = backwardVec = intersectPoint = CCPointZero;
	
	return false;
}


CCArray* GameMap::stringToPoints(char *s) {
    
	CCArray *allPoints = new CCArray;
	char *p = strtok(s, " ");
	while(p) {
		CCPoint *point = new CCPoint(this->stringToPoint(p));
		allPoints->addObject(point);
		point->release();
		p = strtok(NULL, " ");
	}
	allPoints->autorelease();
	return allPoints;
}

CCPoint GameMap::stringToPoint(char *s) {
    
	char *a = s;
	CCPoint p;
	while(1) {
		if(*a == ',') {
			*a = '\0';
			p.x = atoi(s);
			p.y = atoi(a+1);
			break;
		}
		a++;
	}
	return p;
}

CCPoint GameMap::adjustPosition(CCPoint position) {
    
	position = this->tileCoordForPosition(position);
	position = this->positionForTileCoord(position);
    
	return position;
}

//点坐标转图块坐标
CCPoint GameMap::tileCoordForPosition(CCPoint position) {
    
	int x = position.x / this->getTileSize().width;
	int y = (this->getContentSize().height-position.y) / this->getTileSize().height;
	return ccp(x, y);
}

//图块坐标转点坐标
CCPoint GameMap::positionForTileCoord(CCPoint tileCoord) {
    
	CCPoint pos = ccp(tileCoord.x * this->getTileSize().width,
                      (this->getMapSize().height - tileCoord.y - 1) * this->getTileSize().height);
	return pos;
}

void GameMap::vibrationScreen(){
    this->stopAllActions();
    this->setPosition(CCPointZero);
    CCPoint size = this->getPosition();
    int offset = 8;
    float t = 0.1;
    CCMoveTo* left1 = CCMoveTo::create(t,ccp(size.x-offset,size.y));
    CCMoveTo* right1 = CCMoveTo::create(t,ccp(size.x+offset,size.y));
    CCMoveTo* top1 = CCMoveTo::create(t,ccp(size.x,size.y-offset));
    CCMoveTo* rom1 = CCMoveTo::create(t,ccp(size.x,size.y+offset));
    CCCallFunc *call = CCCallFunc::create(this, callfunc_selector(GameMap::screedEnd));
    CCFiniteTimeAction* action3 = CCSequence::create(left1,right1,top1,rom1,call,NULL);
    if(!layer){
        layer = CCLayerColor::create( ccc4(0, 0, 0, 180) );
        layer->setContentSize(CCDirector::sharedDirector()->getVisibleSize());
        int z = -this->getContentSize().height + 1;
        this->addChild(layer,z);
        
    }
    layer->setVisible(true);
    this->runAction(action3);
}

void GameMap::screedEnd(){
    layer->setVisible(false);
}
