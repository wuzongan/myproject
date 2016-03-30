--[[
协议打包类
--]]

local PackMsg = class("PackMsg")

--[[
	构造函数
	roomId：房间ID
	msgCode：协议码
--]]
function PackMsg:ctor(roomId, msgCode)
	local Message = {
		roomId,
		msgCode,
		body
	}
	self.__buf = cc.utils.ByteArray.new(ENDIAN)
	self.__head = cc.utils.ByteArray.new(ENDIAN)
	self.__body = cc.utils.ByteArray.new(ENDIAN)

	self.msg = Message
	if roomId and msgCode then
		self:setHead(roomId, msgCode)
	end
end

function PackMsg:setRoomId(roomId)
	if not self.msg.roomId then
		self.__head:writeByte(roomId)
	end
end

function PackMsg:setMsgCode(msgCode)
	if not self.msg.msgCode then
		self.__head:writeShort(msgCode)
	end
end

function PackMsg:setHead(roomId, msgCode)
	self:setRoomId(roomId)
	self:setMsgCode(msgCode)
	self.msg.roomId = roomId
	self.msg.msgCode = msgCode
end

function PackMsg:setBody(body)
	if not self.msg.body then
		local mp = require("proto.MessagePack")
		self.__body:writeBuf(mp.pack(body))
	end
	self.msg.body = body
end

--[[
	打包，生成要发送的数据
	之前已经设置好roomId,msgCode,body，才能调用该函数
]]
function PackMsg:pack()
	local len = PACKET_HEAD_LENGTH + self.__body:getLen()
	self.__buf:writeShort(len)
	self.__buf:writeBytes(self.__head)
	self.__buf:writeBytes(self.__body)
	if MSG_PRINT then
		printf("============send %u len %u packet: %s", self.msg.msgCode, len, self.__buf:toString(16))
	end
	local data = self.__buf:getPack() --mp.pack(self.__buf:getPack())
	return data
end

--[[
	解包，生成接收到的数据
]]
function PackMsg:unpack(__buf)
	-- print("socket receive raw data:", __buf:toString(16))
	local __body = cc.utils.ByteArray.new(ENDIAN)
	__buf:setPos(PACKET_HEAD_LENGTH)
	self.msg.roomId = __buf:readByte()
	self.msg.msgCode = __buf:readShort()
	
	__buf:setPos(PACKET_HEAD_LENGTH + 1 + 2)
	__buf:readBytes(__body, 1, __buf:getAvailable())
	if __body:getLen() > 0 then
		-- print("unpack body==================", __body:toString(16))
		__body:setPos(1)
		local mp = require("proto.MessagePack")
		self.msg.body = mp.unpack(__body:getPack())
	end
end

return PackMsg