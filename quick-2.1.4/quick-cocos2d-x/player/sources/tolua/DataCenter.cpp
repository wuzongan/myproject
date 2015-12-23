//
//  DataCenter.cpp
//  EmptyMap
//
//  Created by liu geng on 12-12-31.
//
//

#include "DataCenter.h"


static DataCenter *m_sInstance = NULL;
DataCenter *DataCenter::shared() {
	if (!m_sInstance) {
		m_sInstance = new DataCenter;
	}
	return m_sInstance;
}



DataCenter::~DataCenter() {
	CC_SAFE_RELEASE_NULL(m_sInstance);
}

void DataCenter::setStringForKey(const char *str, const char *key) {
	
	string tVal = str;
	string tKey = key;
	encryptString(tVal);
	encryptString(tKey);
	
	CCUserDefault::sharedUserDefault()->setStringForKey(tKey.c_str(), tVal);
	CCUserDefault::sharedUserDefault()->flush();
}

const char *DataCenter::getStringForKey(const char *key) {
	
	string str = key;
	encryptString(str);
	str = CCUserDefault::sharedUserDefault()->getStringForKey(str.c_str());
	decryptString(str);
	if (strcopy != NULL )
	{
		delete []strcopy;
	}
	strcopy = new char[str.length()+1];
	memcpy(strcopy,str.c_str(),str.length()+1);
	return strcopy;
}

void DataCenter::setStringForKey(const char *str, int key) {
	
	char *c = new char[4];
	sprintf(c, "%d", key);
	setStringForKey(str, c);
	delete c;
}

const char *DataCenter::getStringForKey(int key) {
	
	char *c = new char[4];
	sprintf(c, "%d", key);
	const char *ret = getStringForKey(c);
	delete c;
	return ret;
}

void DataCenter::setValueForKey(int val, int key) {
	
	char *v = new char[4];
	char *k = new char[4];
	sprintf(v, "%d", val);
	sprintf(k, "%d", key);
	setStringForKey(v, k);
	delete v;
	delete k;
}

int DataCenter::getValueForKey(int key) {
	
	return atoi(getStringForKey(key));
}

void DataCenter::encryptString(string &str) {
	
	unsigned char *a = (unsigned char *)str.c_str();
	string tStr = "t";	// 添加前缀解决读取错误
	char *c = new char[sizeof(str)];
	for (int i = 0; i < str.size(); i++) {
		sprintf(c, "%x", 0xff - a[i]);
		tStr += c;
	}
	delete[] c;
	str = tStr;
}

void DataCenter::decryptString(string &str) {
	
	string ret;
	const unsigned char *a = (const unsigned char *)str.c_str();
	for (int i = 1; i < str.size(); i += 2) {
		ret += 0xff - hexToChar(a[i], a[i+1]);
	}
	str = ret;
}

char DataCenter::hexToChar(char c) {
	
	if (isalpha(c)) {
		return c - 'a' + 10;
	}
	return c - '0';
}

char DataCenter::hexToChar(char a, char b) {
	
	return hexToChar(a) * 16 + hexToChar(b);
}

