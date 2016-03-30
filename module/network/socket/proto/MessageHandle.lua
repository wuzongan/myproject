module(...,package.seeall)
local msg_id_index = {}
local print_filter = {}
regist = function(msgid, fun, no_print, key)
	if msg_id_index[msgid] == nil then
		msg_id_index[msgid] = {}
	end
	if msg_id_index[msgid].dealers == nil then
		msg_id_index[msgid].dealers = {}
	end
	if key == nil then
		key = "none"
	end
	if no_print then
		msg_id_index[msgid].print = no_print
	end
	msg_id_index[msgid].dealers[key] = fun
end

getdealers = function (msgid)
	if msg_id_index[msgid] then
		return msg_id_index[msgid].dealers
	else
		return {}
	end
end

getmsgname = function (msgid)
	if msg_id_index[msgid] then
		return msg_id_index[msgid].name
	else
		return "unknown"
	end
end

deal = function (bin)
	local __buf = cc.utils.ByteArray.new(ENDIAN)
	__buf:writeBuf(bin)
	local packMsg = require("network.PackMsg").new()
	packMsg:unpack(__buf)
	local __body = packMsg.msg.body
	if not msg_id_index[packMsg.msg.msgCode] then
		print("==============No registed:"..packMsg.msg.msgCode.."====================")
		return
	else
		local str = __body
		if type(__body) == "table" then
			str = table.tostring(__body)
		end
		if not msg_id_index[packMsg.msg.msgCode].print and MSG_PRINT then
			print("==============receive:"..packMsg.msg.msgCode,packMsg.msg.roomId.."====================",str)
		end
	end
	wait:onReceiveMsg(packMsg.msg.msgCode)
	for i,fun in pairs(msg_id_index[packMsg.msg.msgCode].dealers) do
		local backup_room = gameMgr.curRoom
		-- print("=====receive==============",packMsg.msg.roomId)
		gameMgr.curRoom = gameMgr:getRoomById(packMsg.msg.roomId)
	    fun(__body, gameMgr.curRoom)
	    gameMgr.curRoom = backup_room or gameMgr.curRoom
	end
end

table.tostring = function(t)  
    local mark={}  
    local assign={}  
    local function ser_table(tbl,parent)  
	    mark[tbl]=parent  
	    local tmp={}  
	    for k,v in pairs(tbl) do  
	    	local key= type(k)=="number" and "["..k.."]" or "[".. string.format("%q", k) .."]"  
	    	if type(v)=="table" then  
		     	local dotkey= parent.. key  
		     	if mark[v] then  
		      		table.insert(assign,dotkey.."='"..mark[v] .."'")  
		     	else  
		      		table.insert(tmp, key.."="..ser_table(v,dotkey))  
	     		end  
	    	elseif type(v) == "string" then  
	     		table.insert(tmp, key.."=".. string.format('%q', v))  
	   	    elseif type(v) == "number" or type(v) == "boolean" then  
	     		table.insert(tmp, key.."=".. tostring(v))  
	    	end  
	    end  
    	return "{"..table.concat(tmp,",").."}"  
	end  
	return ser_table(t,"ret")..table.concat(assign," ")
end  
  
table.loadstring = function(strData)  
	local f = loadstring(strData)  
	if f then  
	   return f()  
	end  
end



