//
//
//  Created by wuzongan on 13-1-30.
//
//

#ifndef __gc_2dx_v001__FontNumber__
#define __gc_2dx_v001__FontNumber__

#include <iostream>
#include "cocos2d.h"

USING_NS_CC;

enum kFloaterType {
	kFloaterBlood,				// - 掉血
	kFloaterCriticalHit,		// - 暴击掉血
	kFloaterExperience,			// + 经验值
	kFloaterGold,				// 金钱
	kFloaterMessage,			// 浮动文字，0-失误，1-格挡，2-吸收
	kFloaterSkillWarrior,
	kFloaterSkillWarrioress,
	kFloaterSkillStrategist
};

// new floaterType
enum kFloater_Type {
	kFloater_attack_normal,				// 对敌，平砍			白色
	kFloater_attack_normal_critical,	// 对敌，平砍暴击		白色，放大
	kFloater_attack_skill,				// 对敌，技能攻击		黄色
	kFloater_attack_skill_critical,		// 对敌，技能攻击暴击	黄色，放大
	kFloater_miss,						// 未命中				miss
	kFloater_dot,						// dot				黄色
	kFloater_attack_self_normal,		// 自己受到的伤害		红色
	kFloater_attack_self_critical,		// 自己受到的暴击		红色，放大
	kFloater_addBlood,					// 加血				绿色
    kFloater_addBlood_critical,			// 加血暴击			绿色
	kFloater_addMagic					// 加蓝				蓝色
};

class Floater : public CCSprite {
public:
	CCRect m_oRect;
	int m_nType;
    int dir;
public:
	static Floater *create(int num, int type, const CCRect& rect, int dir);
	void init(int num, int type, const CCRect& rect);
	
	void show();
	
	void attackNormal();
	void attackSkill();
	void attackCritical();
	void add();
	void miss();
    void moveWithParabola(CCPoint startPoint, CCPoint endPoint,float originPoint, float time);

};


#endif /* defined(__gc_2dx_v001__FontNumber__) */
