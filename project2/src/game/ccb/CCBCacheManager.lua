--ccb缓存类
--处理战斗技能缓存
local CCBCacheManager = class("CCBCacheManager")

function CCBCacheManager:ctor()
	self.ccbCacheList = {}
	self.cacheRoleSkill = nil
end

function CCBCacheManager:createCCB(param)
	local ccbName = param.ccbName
	local animationName = param.animationName
	local b_CompletedCallback = param.b_CompletedCallback
	local fun = param.fun
	local b_cache = param.b_cache
	local ccb = nil
	if b_cache then
		ccb = game.ccbManager.new():createCCB(ccbName, animationName, b_CompletedCallback)
		if self.cacheRoleSkill then
			self.cacheRoleSkill:handleCaCheByName(ccbName)
		end
	end
	return ccb
end

function CCBCacheManager:addCacheChild(parent, node, zOrder, tag)
	local isClear = false
	local node_bak = nil
	if node and parent then
		if not self.ccbCacheList[node.ccbname] then
			isClear = true
		else
			if tolua.type(parent) == "CCSprite" then
				if self.ccbCacheList[node.ccbname].id then
					-- node.backup_data = self.ccbCacheList[node.ccbname].node.backup_data
					if not tolua.isnull(self.ccbCacheList[node.ccbname].id[parent.roleId]) then
						self.ccbCacheList[node.ccbname].id[parent.roleId]:removeFromParent()
						self.ccbCacheList[node.ccbname].id[parent.roleId] = nil
						isClear = true
					elseif node ~= self.ccbCacheList[node.ccbname].node then
		       			isClear = true
					end
				end
			else
				if self.ccbCacheList[node.ccbname].node ~= node then
					if not self.ccbCacheList[node.ccbname].node:isVisible() then
		       			self.ccbCacheList[node.ccbname].node:removeFromParent()
		       		end
					isClear = true
				end
			end
		end
		if isClear then
			if tag then
				parent:addChild(node, zOrder, tag)
			elseif zOrder then
				parent:addChild(node, zOrder)
			else
				parent:addChild(node)
			end
		else
			if tag then
				node:setTag(tag)
				node:setZOrder(zOrder)
			elseif zOrder then
				node:setZOrder(zOrder)
			end
		end
		if isClear then
			self.ccbCacheList[node.ccbname] = self.ccbCacheList[node.ccbname] or {}
			self.ccbCacheList[node.ccbname].id = self.ccbCacheList[node.ccbname].id or {}
			self.ccbCacheList[node.ccbname].node = node
	        if tolua.type(parent) == "CCSprite" then
	        	self.ccbCacheList[node.ccbname].id[parent.roleId] = node
	        end
	    end
	end
end

function CCBCacheManager:removeCache()
    for k,v in pairs(self.ccbCacheList) do
    	if v and self.ccbCacheList[v.ccbname] then
	    	-- ccbNode:removeFromParent() --切换地图的时候已经移除
	    	self.ccbCacheList[v.ccbname].id = nil
	        self.ccbCacheList[v.ccbname].node = nil
	        self.ccbCacheList[v.ccbname] = nil
	    end
    end
    self.ccbCacheList = {}
end

function CCBCacheManager:removeCCBByRoleId(roleId)
    for k,v in pairs(self.ccbCacheList) do
    	if v.id and v.id[roleId] then
    		if not tolua.isnull(self.ccbCacheList[k].id[roleId]) then
    			self.ccbCacheList[k].id[roleId]:removeFromParent()
    		end
    		if not tolua.isnull(self.ccbCacheList[k].node) then
    			self.ccbCacheList[k].node:removeFromParent()
    		end 
    		self.ccbCacheList[k].id[roleId] = nil
    		self.ccbCacheList[k].node = nil
    		self.ccbCacheList[k] = nil
    	end
    end
end

function CCBCacheManager:removeCCB(ccb)
	if not tolua.isnull(ccb) then
	    if self.ccbCacheList[ccb.ccbname] then
	        if self.ccbCacheList[ccb.ccbname].node == ccb then
	            ccb:setVisible(false)
	        else
	            ccb:removeFromParent()
	        end
	    else
	        ccb:removeFromParent()
	    end
	end
end

return CCBCacheManager