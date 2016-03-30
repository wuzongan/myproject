local NetworkMgr = class("NetworkMgr")
local socketBuff = require("network.CSocketBuff")
local SocketTCP = import(".SocketTCP")

function NetworkMgr:ctor()
	self.socketObj = nil
	self.socketBuff = socketBuff.new()
	self.func = nil
	self.isWait = nil
end

function NetworkMgr:connect_server(ip, port, func, isWait)
	self.func = func
	self.isWait = isWait
	-- 检查wifi和3G网络是否连通
	if game.isConnection() then
		if not self.socketObj then
			self.socketObj = SocketTCP.new(ip, port, true)
			self.socketObj:addEventListener(SocketTCP.EVENT_CONNECTED, handler(self, self.connected))
			self.socketObj:addEventListener(SocketTCP.EVENT_CLOSE, handler(self,self.onClose))
			self.socketObj:addEventListener(SocketTCP.EVENT_CLOSED, handler(self,self.onStatus))
			self.socketObj:addEventListener(SocketTCP.EVENT_CONNECT_FAILURE, handler(self,self.onStatus))
			self.socketObj:addEventListener(SocketTCP.EVENT_DATA, handler(self,self.receive))
			self.socketObj:addEventListener(SocketTCP.EVENT_RECONNECT_FAILURE, handler(self,self.socketOff))
		elseif self.socketObj.isConnected then
			if self.func then
				self.func()
				self.func = nil
			end
			return
		end
		self.socketObj:setReconnCount(0)
		self.socketObj:connect()
		if not self.isWait then
			self.delay = scheduler.performWithDelayGlobal(function()
				local loading = require("common.tools.KNLoading"):new({text = getLanStr("TIPS_103")})
				layerMgr.waitLayer:addChild(loading:getLayer())
			end, 0.5)
		end
	else
		device.showAlert("",getLanStr("TIPS_100"),getLanStr("btn_1"))
	end
end

function NetworkMgr:send(msg)
	if self.socketObj and self.socketObj.isConnected then
		self.socketObj:send(msg)
	end
end

function NetworkMgr:receive(event)
	-- protoMsg.deal(event.data)
	self:handlerData(event.data)
end

function NetworkMgr:socketOff(event)
	printf("socket status: %s", event.name)
	if not self.isWait then 
		layerMgr.waitLayer:removeAllChildren() 
	end
	scheduler.performWithDelayGlobal(function()
		local function onButtonClicked(event)
            -- if event.buttonIndex == 1 then
                game.enterMainScene()
            -- end
        end
		device.showAlert("",getLanStr("TIPS_101"),getLanStr("btn_1"),onButtonClicked)
	end,0.1)
	
end

-- 解析数据包
function NetworkMgr:handlerData(data)
	self.socketBuff:addReadBuf(data)
	while true do
		local packet = self.socketBuff:getPacket()
		if packet then
			protoMsg.deal(packet)
		else
			break
		end
	end
end

function NetworkMgr:closeSocket()
	if self.socketObj then
		self.socketObj:close()
		-- self.socketObj = nil
	end
end

function NetworkMgr:onStatus(event)
	printf("socket status: %s", event.name)
end

function NetworkMgr:onClose()
	printf("socket status: onClose")
	if gameMgr.user then
		-- local function onButtonClicked(event)
		-- 	gameMgr.user = nil
  --           game.enterMainScene()
  --       end
		-- device.showAlert("",getLanStr("TIPS_101"),getLanStr("btn_1"),onButtonClicked)
		self.func = function()
			local function yes()
	            reconnectMgr:startReconnect()
	        end 
	        local function no()
	            gameMgr.user = nil
	            game.enterMainScene()
	        end 
	        showTips(getLanStr("TIPS_107"),{{text=getLanStr("btn_1"),handler=yes},{text=getLanStr("btn_2"),handler=no}})
		end
		
	end
end

function NetworkMgr:connected(event)
	printf("connected: %s", event.name)
	self.socketObj:setReconnCount(2)
	if not self.isWait then
		if self.delay then scheduler.unscheduleGlobal(self.delay) end
		layerMgr.waitLayer:removeAllChildren()
	end
	-- loginMgr:connected()
	if self.func then
		self.func()
		self.func = nil
	end
end

function NetworkMgr:disConnect()
	if self.socketObj then
		self.socketObj:disConnect()
	end
end


return NetworkMgr