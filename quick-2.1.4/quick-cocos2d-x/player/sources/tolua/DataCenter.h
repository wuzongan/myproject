//
//  DataCenter.h
//  EmptyMap
//
//  Created by liu geng on 12-12-31.
//
//

#ifndef __EmptyMap__DataCenter__
#define __EmptyMap__DataCenter__

#include <iostream>
#include "cocos2d.h"

USING_NS_CC;
using namespace std;

class DataCenter : public CCObject {
	
public:
	DataCenter(){strcopy=NULL;};
	~DataCenter();
	static DataCenter *shared();
	
	void setStringForKey(const char *str, const char *key);
	const char *getStringForKey(const char *key);
	
	void setStringForKey(const char *str, int key);
	const char *getStringForKey(int key);
	
	void setValueForKey(int val, int key);
	int getValueForKey(int key);
	
private:
	// 数据加密
	void encryptString(string &str);
	void decryptString(string &str);
	char hexToChar(char c);
	char hexToChar(char a, char b);
	char *strcopy;
};

#endif /* defined(__EmptyMap__DataCenter__) */
