local CSocketBuff = class("CSocketBuff")

function CSocketBuff:ctor()
	self.readBuf = ""
	self.writeBuf = ""
end

function CSocketBuff:addReadBuf(SocketData)
	if type(SocketData) == "string" then
		self.readBuf = self.readBuf..SocketData
	end
end

function CSocketBuff:getReadBuf() 
	return self.readBuf
end

function CSocketBuff:addWriteBuf(SocketData)
	if  type(SocketData) == "string" then
		self.writeBuf = self.writeBuf..SocketData
	end
end

function CSocketBuff:getWriteBuf()
	return self.writeBuf
end

function CSocketBuff:getPacket()
	if string.len(self.readBuf) <= PACKET_HEAD_LENGTH then
		return nil
	end
	local __buf = cc.utils.ByteArray.new(ENDIAN)
	__buf:writeBuf(self.readBuf)
	__buf:setPos(1)
	local packetLen = __buf:readShort()
	if PACKET_HEAD_LENGTH + packetLen - 1 > string.len(self.readBuf) then
		return nil
	else
		local packet = string.sub(self.readBuf,1,packetLen+PACKET_HEAD_LENGTH - 1)
		self.readBuf = string.sub(self.readBuf,packetLen+PACKET_HEAD_LENGTH)
		return packet
	end
end


return CSocketBuff