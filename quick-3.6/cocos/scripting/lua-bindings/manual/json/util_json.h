//
//  util_json.h
//  xggClient_ios
//
//  Created by yock on 12-12-24.
//  Copyright (c) 2012å¹?__MyCompanyName__. All rights reserved.
//
//  this is hash json util

#ifndef xggClient_ios_util_json_h
#define xggClient_ios_util_json_h

#include "libjson/libjson.h"

class util_json;

typedef util_json* util_json_iterator;


class util_json
{
public:
    enum {
        ENull,
        EString,
        ENumber,
        EBool,
        EArray,
        ENode,
        EError
    };
    
public:
    util_json();
    util_json(const util_json* node);
    util_json(const char* jsonbuffer);
    ~util_json();
     
    /*
    bool init();
    bool initWithNode(const util_json* node);
    bool initWithString(const char* jsonBuffer);
    void release();
     */
public:
//    static util_json* createJsonNode();
//    static util_json* createJsonNode(const util_json* node);
//    static util_json* createJsonNode(const char* jsonbuffer);
//    static util_json* createJsonArrayNode();
    bool init(const char* jsonbuffer);
    void addJson(const char *key,const char* value);
    void addJson(const char *key,float value);
    void addJson(const char *key,int value);
    void addJson(const char *key,bool value);
    void addJson(const char *key,const util_json *value);
    void createNode();
    void createArrayNode();
     void setJsonNodeName(const char* name);
//    char* getJsonName();
public:
    int getNodeCount();
    util_json* getNodeAt(int index);
    util_json* getNodeWithKey(char* key);
public:
    util_json_iterator begin();
    util_json_iterator end();
public:
    std::string toStr();
    int         toInt();
    float       toFloat();
    bool        toBool();
    
    int         nodeType();
    std::string getKey();
    bool        isEmpty();
public:
    //it used free buffer then come from getFormatBuffer()
    std::string getFormatBuffer();
    //this is not use
    static void freeWriteBuffer(void *buffer);
  
   
protected:
    JSONNODE        *m_hJsonRoot;
};


#endif
