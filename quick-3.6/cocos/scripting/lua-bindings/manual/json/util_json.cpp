//
//  util_json.cpp
//  xggClient_ios
//
//  Created by yock on 12-12-24.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#include <iostream>
#include "util_json.h"

//static int m = 0, n = 0;
util_json::util_json()
{
//    printf("%d", m++);
    m_hJsonRoot = NULL;
}
util_json::util_json(const util_json* node)
{
//     printf("%d", m++);
    m_hJsonRoot = json_copy(node);
}

util_json::util_json(const char* jsonbuffer)
{
//    printf("%d", m++);
    m_hJsonRoot = json_parse(jsonbuffer);
}




util_json::~util_json()
{
//     printf("%d", n++);
    if(m_hJsonRoot)
        json_delete(m_hJsonRoot);
}


bool util_json::init(const char* jsonbuffer)
{
    if(m_hJsonRoot)
        json_delete(m_hJsonRoot);
    m_hJsonRoot = json_parse(jsonbuffer);
    return true;
}


/*
bool util_json::init()
{
    m_hJsonRoot = NULL;
    return true;
}
bool util_json::initWithNode(const util_json* node)
{
    m_hJsonRoot = json_copy(node);
}
bool util_json::initWithString(const char* jsonBuffer)
{
    m_hJsonRoot = json_parse(jsonBuffer);
}
void util_json::release()
{
    if(m_hJsonRoot)
        json_delete(m_hJsonRoot);
}
*/
void util_json::addJson(const char *key,const char* value)
{
    createNode();
    if(json_type(m_hJsonRoot) == JSON_ARRAY)
        json_push_back(m_hJsonRoot, json_new_a("", value));
    else 
        json_push_back(m_hJsonRoot, json_new_a(key, value));
}
void util_json::addJson(const char *key,float value)
{
    createNode();
    if(json_type(m_hJsonRoot) == JSON_ARRAY)
        json_push_back(m_hJsonRoot, json_new_f("", value));
    else
    
        json_push_back(m_hJsonRoot, json_new_f(key, value));
}
void util_json::addJson(const char *key,int value)
{
    createNode();
    if(json_type(m_hJsonRoot) == JSON_ARRAY)
        json_push_back(m_hJsonRoot, json_new_i("", value));
    else
        json_push_back(m_hJsonRoot, json_new_i(key, value));
}

 void util_json::addJson(const char *key,bool value)
{
     createNode();
    json_push_back(m_hJsonRoot, json_new_b(key, value));

}

void util_json::addJson(const char *key,const util_json *value)
{
    createNode();
//    util_json* node = (util_json*)value;
    JSONNODE *node = value->m_hJsonRoot;
    if(json_type(m_hJsonRoot) != JSON_ARRAY)
        json_set_name(node, key);
    
    json_push_back(m_hJsonRoot, node);
}


//void util_json::createNewArrayNode()
//{
//    if (m_hJsonRoot == NULL) {
//         m_hJsonRoot = json_new(JSON_ARRAY);
//    }
//   
//}

//char* util_json::getJsonName()
//{
//    char* value = json_name(m_hJsonRoot);
//    if(!value) return std::string("");
//    std::string v = value;
//    json_free(value);
//    return v.c_str();
//}
std::string util_json::getFormatBuffer()
{
    if(m_hJsonRoot == NULL)
        return NULL;
    char* buffer = json_write(m_hJsonRoot);
    std::string buff = buffer;
    json_free(buffer);
    return buff;
}
int util_json::getNodeCount()
{
    if(!m_hJsonRoot) return 0;
    int count = 0;
    JSONNODE_ITERATOR it = json_begin(m_hJsonRoot);
    while (it++ != json_end(m_hJsonRoot)) {
        count++;
    }
    return count;
    
}
util_json* util_json::getNodeAt(int index)
{
    if(index<0) return NULL;
    if(!m_hJsonRoot) return NULL;
    JSONNODE_ITERATOR it = json_begin(m_hJsonRoot);

    int i = 0;
    while (it != json_end(m_hJsonRoot) && i <= index) {
        if (i == index) {
            return (util_json*)it;
        }
        i++;
        it++;
    }
//    for(int i = 0 ; i<= index ; i++,it++)
//    {
//        if(it == json_end(m_hJsonRoot))
//        return NULL;
//    }
//    return (util_json*)it;
    return NULL;
}
util_json* util_json::getNodeWithKey(char* key)
{
    int s =0;
    if (strcmp("frames",key) == 0)
    {
        s = json_type(m_hJsonRoot);
       
    }
    JSONNODE_ITERATOR it = json_find(m_hJsonRoot, key);
    if (it == json_end(m_hJsonRoot)) {
        return NULL;
    }
    return (util_json*)it;
}

util_json_iterator util_json::begin()
{
    if(m_hJsonRoot == NULL) return NULL;
    JSONNODE_ITERATOR it = json_begin(m_hJsonRoot);
    return (util_json_iterator)it;
}


util_json_iterator util_json::end()
{
    if(m_hJsonRoot==NULL) return NULL;
    JSONNODE_ITERATOR it = json_end(m_hJsonRoot);
    return (util_json_iterator)it;
}

std::string util_json::toStr()
{
    char* value = json_as_string(m_hJsonRoot);
    if(!value) return std::string("");
    std::string v = value;
    json_free(value);
    return v;
}
int         util_json::toInt()
{
    return json_as_int(m_hJsonRoot);
}

float       util_json::toFloat()
{
    return json_as_float(m_hJsonRoot);
}

 bool        util_json::toBool()
{
    return json_as_bool(m_hJsonRoot);
}
int         util_json::nodeType()
{
    if(m_hJsonRoot == NULL) return EError;
    switch(json_type(m_hJsonRoot))
    {   
        case JSON_NULL:
            return ENull;
        case JSON_STRING:
            return EString;
        case JSON_NUMBER:
            return ENumber;
        case JSON_BOOL:
            return EBool;
        case JSON_ARRAY:
            return EArray;
        case JSON_NODE:
            return ENode;
        default:
            return EError;
    }
}

std::string util_json::getKey()
{
    std::string key;
    json_char* name = json_name(m_hJsonRoot);
    if(name == NULL)
        return NULL;
    key = name;
    json_free(name);
    return key;
}
bool        util_json::isEmpty()
{
    return m_hJsonRoot==NULL;
}
void util_json::createNode()
{
    if(m_hJsonRoot == NULL)
    {
        m_hJsonRoot = json_new(JSON_NODE);
    }
}
//it used free buffer then come from getFormatBuffer()
void util_json::freeWriteBuffer(void *buffer)
{

    json_free(buffer);
}



void util_json::createArrayNode()
{
    if(m_hJsonRoot == NULL)
    {
        m_hJsonRoot = json_new(JSON_ARRAY);
    }

}
void util_json::setJsonNodeName(const char* name)
{
    json_set_name(m_hJsonRoot, name);
}

