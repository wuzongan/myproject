//
//
//  Created by admin on 13-1-30.
//
//

#include "Floater.h"

Floater *Floater::create(int num, int type, const CCRect& rect, int dir) {
	
	Floater *ret = new Floater;
    ret->dir = dir;
	ret->init(num, type, rect);
	ret->autorelease();
	return ret;
}

void Floater::init(int num, int type, const CCRect& rect) {
	
	m_oRect = rect;
	m_nType = type;
	
	int w = 0, h = 0;
	const char *preFix = NULL;
	
		
	switch (type) {
		case kFloater_attack_normal_critical:
			preFix = "w";
			num = -num;
			CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile("font/num_white.plist");
			break;
        case kFloater_attack_normal:
		case kFloater_attack_skill:
		case kFloater_attack_skill_critical:
			preFix = "y";
			num = -num;
			CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile("font/num_yellow.plist");
			break;
			
		case kFloater_attack_self_normal:
		case kFloater_attack_self_critical:
        case kFloater_dot:
			preFix = "r";
			num = -num;
			CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile("font/num_red.plist");
			break;
			
		case kFloater_addBlood:
        case kFloater_addBlood_critical:
			preFix = "g";
			CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile("font/num_green.plist");
			break;
		case kFloater_addMagic:
			preFix = "b";
			CCSpriteFrameCache::sharedSpriteFrameCache()->addSpriteFramesWithFile("font/num_blue.plist");
			break;
			
		default:
			break;
	}
	
	// 拼数字
	std::stringstream ss;
	ss << num;
	std::string s = ss.str();
	
	if (type == kFloater_addBlood || type == kFloater_addMagic) {
		s = "+" + s;
	}
	
	
	
	if (type == kFloater_miss) {
		initWithFile("font/miss.png");
		
	} else if (preFix) {
		
		CCNode *node = new CCNode;
		for (int i = 0; i < s.size(); i++) {
			CCString *str = CCString::createWithFormat("%s_%c.png", preFix, s[i]);
//			CCLog("font num: %s", str->getCString());
			CCSprite *sp = CCSprite::createWithSpriteFrameName(str->getCString());
			sp->setAnchorPoint(ccp(0, 0));
			sp->setPosition(ccp(w, 0));
			node->addChild(sp);
			
			h = MAX(h, sp->getContentSize().height);
			w += sp->getContentSize().width;
		}
		
		CCRenderTexture *rt = new CCRenderTexture;
		rt->initWithWidthAndHeight(w, h, kCCTexture2DPixelFormat_RGB5A1);
		rt->begin();
		node->visit();
		rt->end();
		initWithTexture(rt->getSprite()->getTexture());
		setFlipY(true);
		rt->release();
		node->release();
	}
	
	setAnchorPoint(ccp(0.5, 0.5));
}


void Floater::show() {
	
	CCPoint p = m_oRect.origin;
	float& w = m_oRect.size.width;
	float& h = m_oRect.size.height;
	float endPointX;
    float endPointY;
    float originPoint;
    float width;
    int kLeft = 7;
	switch (m_nType) {
			
		case kFloater_attack_normal_critical:
			setPosition(ccpAdd(p, ccp(w, h)));
			attackCritical();
			break;
			
		case kFloater_attack_skill:
			setPosition(ccpAdd(p, ccp(w / 2, h / 2)));
			attackSkill();
			break;
		case kFloater_attack_self_normal:
			setPosition(ccpAdd(p, ccp(w / 2, h)));
			attackNormal();
			break;
		case kFloater_attack_skill_critical:	
		case kFloater_attack_self_critical:
			setPosition(ccpAdd(p, ccp(w, h)));
			attackCritical();
			break;
        case kFloater_attack_normal:
		case kFloater_dot:
			
            
            endPointX = CCRANDOM_0_1()*100+100;
            endPointY = CCRANDOM_0_1()*100;
            originPoint = CCRANDOM_0_1()*100+100;
            endPointX = dir == kLeft ? -endPointX : endPointX;
            endPointX = dir == kLeft ? -endPointY : endPointY;
            endPointX = dir == kLeft ? -originPoint : originPoint;
            width = dir == kLeft ? - w / 2 : w / 2;
            setPosition(ccpAdd(p, ccp(width, h / 2)));
            moveWithParabola(p, ccpAdd(p, ccp(endPointX,endPointY)),originPoint, 0.6f);

//			attackSkill();
			break;
			
		case kFloater_addBlood:
			setAnchorPoint(ccp(0.5, 0));
			setPosition(ccpAdd(p, ccp(w / 2, h / 2)));
			attackNormal();
			break;
        case kFloater_addBlood_critical:
			setAnchorPoint(ccp(0.5, 0));
			setPosition(ccpAdd(p, ccp(w / 2, h / 2)));
			attackCritical();
			break;
		case kFloater_addMagic:
			setAnchorPoint(ccp(0.5, 1));
			setPosition(ccpAdd(p, ccp(w / 2, h / 2)));
			add();
			break;
			
		case kFloater_miss:
			setPosition(ccpAdd(p, ccp(w / 2, h / 2)));
			miss();
			break;
			
		default:
			break;
	}
}

void Floater::attackNormal() {
	
//	runAction(CCFadeTo::create(2.5, 0));
	runAction(CCSequence::create(
			CCMoveBy::create(0.5, ccp(0, 50)),
			CCDelayTime::create(0.2),
			CCFadeTo::create(0.2, 0),
            CCCallFunc::create(this, callfunc_selector(Floater::removeFromParent)), NULL));
}

void Floater::attackSkill() {
	runAction(CCSequence::create(
			CCMoveBy::create(0.5, ccp(-100, 100)),
			CCMoveBy::create(0.2, ccp(-50, 0)),
			CCDelayTime::create(0.2),
			CCFadeTo::create(0.2, 0),
			CCCallFunc::create(this, callfunc_selector(Floater::removeFromParent)), NULL));
}

void Floater::attackCritical() {
	
//	runAction(CCFadeTo::create(2.1, 0));
	runAction(CCSequence::create(
//			CCScaleTo::create(0.1, 2.5),
			CCScaleTo::create(0.1, 1.5),
			CCDelayTime::create(0.7),
			CCFadeTo::create(0.2, 0),
			CCCallFunc::create(this, callfunc_selector(Floater::removeFromParent)), NULL));
}

void Floater::add() {
	
//	runAction(CCFadeTo::create(2.0, 0));
	runAction(CCSequence::create(
			CCDelayTime::create(0.5),
			CCFadeTo::create(0.2, 0),
			CCCallFunc::create(this, callfunc_selector(Floater::removeFromParent)), NULL));
}

void Floater::miss() {
	
	attackNormal();
}

//抛物线
//startPoint:起始位置
//endPoint:中止位置
//originPoint:原点位置
//dirTime:起始位置到中止位置的所需时间
void Floater::moveWithParabola(CCPoint startPoint, CCPoint endPoint, float originPoint, float time){
    float sx = startPoint.x;
    float sy = startPoint.y;
    float ex =endPoint.x+50;
    float ey =endPoint.y+150;
    int h = getContentSize().height*0.5;
    ccBezierConfig bezier; // 创建贝塞尔曲线
    bezier.controlPoint_1 = ccp(sx, sy); // 起始点
    bezier.controlPoint_2 = ccp(sx+(ex-sx)*0.5, sy+(ey-sy)*0.5+originPoint); //控制点
    bezier.endPosition = ccp(endPoint.x-30, endPoint.y+h); // 结束位置
    //    CCSequence *seq = CCSequence::create(CCBezierTo::create(time, bezier));
    CCBezierTo *actionMove = CCBezierTo::create(time, bezier);
    
    runAction(CCSequence::create(actionMove,CCFadeTo::create(0.2, 0),CCCallFunc::create(this, callfunc_selector(Floater::removeFromParent)), NULL));
}
