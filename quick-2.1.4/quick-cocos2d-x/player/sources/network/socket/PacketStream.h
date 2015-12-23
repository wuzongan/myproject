//
//  PacketStream.h
//  
//
//  Copyright (c) ChunBai. All rights reserved.
//
//

#ifndef ____PacketStream__
#define ____PacketStream__

#include <stdint.h>
#include <string>
#include <vector>


template <typename T>
class Pair
{
public:
    T first;
    T second;
};

template <typename T>
class Ternary
{
public:
    T first;
    T second;
    T third;
};

const uint16_t HEADER_SIZE = 3;
const uint16_t ZFLAG_INDEX = 2;
const uint16_t DEFAULT_CAPSIZE = 0x100-HEADER_SIZE;
const uint16_t MAX_CAPSIZE = 0xFFFF - HEADER_SIZE;
const uint16_t NEED_COMPRESS_SIZE = 64;


//+-------------+--------------+---------------------+
//|   2 byte    |  1 byte      |    payload
//|  bigendian  |   zlib flag  |
//|  packlen    |  0:noz | 1:z |
//|-------------+--------------+---------------------+

class PacketStream
{
protected:
    uint16_t   _size;
    char  * _pack;
    uint16_t  _capSize;
    uint16_t  _curPos;
    
    uint16_t _size_z;
    uint16_t _caps_z;
    char * _pack_z;
    bool      _modified;
    
    void correctPacketSize(uint16_t newSize);
    char * curData();
    bool requireCap(uint16_t reqSize);
    void init(uint16_t );

    static uint16_t correctCaps(uint16_t wanSize);
    PacketStream(char * Data,uint16_t S);
    PacketStream(uint16_t Caps);
    bool compress();
public:
    static PacketStream * FromOrgData(char * Data,uint16_t S);
    static PacketStream * FromPack(char * ZData,uint16_t ZS);
    static bool NetDataToOrg(char * i,char * o,uint16_t is,uint16_t& os);
    static PacketStream * FromCaps(uint16_t Caps);
    virtual ~PacketStream();
    uint16_t readAvailable();
    uint16_t writeAvailable();
    
    uint16_t size();
    char * pack(bool b_compress);
	uint16_t packSize(bool b_compress);
    char * data();
    
    bool setPos(uint16_t pos);
    void reset();

    bool readBool(bool & o);
    bool   writeBool(bool i);
    
    bool readInt8(int8_t & o);
    bool   writeInt8(int8_t i);
    
    bool readUint8(uint8_t & o);
    bool    writeUint8(uint8_t i);

    bool readInt16(int16_t & o);
    bool    writeInt16(int16_t i);
    
    bool readUint16(uint16_t & o);
    bool    writeUint16(uint16_t i);
    
    bool readInt32(int32_t & o);
    bool    writeInt32(int32_t i);

    bool readUint32(uint32_t & o);
    bool    writeUint32(uint32_t i);

    bool readInt64(int64_t & o);
    bool    writeInt64(const int64_t & i);
    
    bool readUint64(uint64_t &o);
    bool    writeUint64(const uint64_t & i);
    
    bool readString(std::string & str);
    bool writeString(const std::string & str);

    bool readBoolList(std::vector<bool> & o);
    bool   writeBoolList(const std::vector<bool>& i);
    
    bool readInt8List(std::vector<int8_t> & o);
    bool   writeInt8List(const std::vector<int8_t>& i);
    
    bool readUint8List(std::vector<uint8_t> & o);
    bool    writeUint8List(const std::vector<uint8_t> & i);
    
    bool readInt16List(std::vector<int16_t> & o);
    bool    writeInt16List(const std::vector<int16_t> & i);
    
    bool readUint16List(std::vector<uint16_t> & o);
    bool    writeUint16List(const std::vector<uint16_t> & i);
    
    bool readInt32List(std::vector<int32_t> & o);
    bool    writeInt32List(const std::vector<int32_t> & i);
    
    bool readUint32List(std::vector<uint32_t> & o);
    bool    writeUint32List(const std::vector<uint32_t> & i);
    
    bool readInt64List(std::vector<int64_t> & o);
    bool    writeInt64List(const std::vector<int64_t> & i);
    
    bool readUint64List(std::vector<uint64_t> &o);
    bool    writeUint64List(const std::vector<uint64_t> & i);
    
    bool readStringList(std::vector<std::string> & str);
    bool writeStringList(const std::vector<std::string> & str);
    
    bool readPair(Pair<int8_t> & o);
    bool writePair(const Pair<int8_t> & i);
    
    bool readPair(Pair<int16_t> & o);
    bool writePair(const Pair<int16_t> & i);
    
    bool readPair(Pair<int32_t> & o);
    bool writePair(const Pair<int32_t> & i);

    bool readPair(Pair<int64_t> & o);
    bool writePair(const Pair<int64_t> & i);
    
    bool readTernary(Ternary<int8_t> & o);
    bool writeTernary(const Ternary<int8_t> & i);
    
    bool readTernary(Ternary<int16_t> & o);
    bool writeTernary(const Ternary<int16_t> & i);
    
    bool readTernary(Ternary<int32_t> & o);
    bool writeTernary(const Ternary<int32_t> & i);
    
    bool readTernary(Ternary<int64_t> & o);
    bool writeTernary(const Ternary<int64_t> & i);


    bool readPairList(std::vector<Pair<int8_t> > & o);
    bool writePairList(const std::vector<Pair<int8_t> > & i);
    
    bool readPairList(std::vector<Pair<int16_t> > & o);
    bool writePairList(const std::vector<Pair<int16_t> > & i);

    bool readPairList(std::vector<Pair<int32_t> > & o);
    bool writePairList(const std::vector<Pair<int32_t> > & i);

    bool readPairList(std::vector<Pair<int64_t> > & o);
    bool writePairList(const std::vector<Pair<int64_t> > & i);
    
    bool readTernaryList(std::vector<Ternary<int8_t> > & o);
    bool writeTernaryList(const std::vector<Ternary<int8_t> > & i);
    
    bool readTernaryList(std::vector<Ternary<int16_t> > & o);
    bool writeTernaryList(const std::vector<Ternary<int16_t> > & i);

    bool readTernaryList(std::vector<Ternary<int32_t> > & o);
    bool writeTernaryList(const std::vector<Ternary<int32_t> > & i);

    bool readTernaryList(std::vector<Ternary<int64_t> > & o);
    bool writeTernaryList(const std::vector<Ternary<int64_t> > & i);

};

#endif /* defined(____PacketStream__) */



