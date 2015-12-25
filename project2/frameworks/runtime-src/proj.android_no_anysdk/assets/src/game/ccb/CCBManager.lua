--ccbi基类，用于创建ccbi
local CCBManager = class("CCBManager", function()
    return display.newLayer()
end)
-- 处理粒子跟随
local PARTICLE_NUM = 10

--创建ccb对象
--jsParent为ccb的“JS Controller”名称
--是否缓存
function CCBManager:ctor(jsParent, b_cache)
    self.M = self.M or {} 
    if jsParent then self.M[jsParent] = self.M end
    self.animationMgr = nil
    --回调特效列表
    self.specialList = {} 
    self.backup_data = {}
    self.b_cache = b_cache
    self:setTouchSwallowEnabled(false) 
end

--创建并播放ccb

--ccbName：ccbi的路径
--animationName：要播放的动作名称，没有写“”字符串
--b_CompletedCallback：是否动作完成回调
--fun：动作播放完回调函数
function CCBManager:createCCB(ccbName, animationName, b_CompletedCallback, fun)
    self.ccbname = ccbName
    animationName = animationName or ""
    self.b_CompletedCallback = b_CompletedCallback or false
    -- print("-------------------------",ccbName)
    -- for i = 1, 10 do
    --     self.M["ccb"..i] = function() return self:callCCB("ccb"..i) end
    -- end

    --震屏
    -- for i = 1, 3 do
    --     self.M["quake"..i] = function() return Tools.vibrationScreen(i*8) end
    -- end
    self.proxy = cc.CCBProxy:create()
    local node = self:readCCB(ccbName,self.proxy,self.M)
    node:addChild(self.proxy)
    self:addChild(node)
    --获取动作对象
    if self.M then
        self.animationMgr = tolua.cast(self.M["mAnimationManager"],"CCBAnimationManager")
        if self.animationMgr then
            self:runAnimationsByName(animationName, fun)
            if b_CompletedCallback then
                self.animationMgr:setAnimationCompletedCallback(nil, cc.CallFunc:create(function()
                    self:remove(fun)
                end))
            end
        end
        
        -- for i = 1, PARTICLE_NUM do
        --     local particleName = "particle" .. tostring(i)
        --     if self.M[particleName] then
        --         local particle = tolua.cast(self.M[particleName],"CCParticleSystemQuad")
        --         if particle then
        --             particle:setPositionType(kCCPositionTypeRelative)
        --         end
        --     end
        -- end

        if self.M["ccb_special"] then
            local ccb_special = tolua.cast(self.M["ccb_special"],"CCLabelTTF")
            self.specialList = self:callBackSpecial(ccb_special:getString())
        end
    end
    return self
end

function CCBManager:setCallback( b_CompletedCallback )
    self.b_CompletedCallback = b_CompletedCallback
    if b_CompletedCallback then
        self.animationMgr:setAnimationCompletedCallback(nil, cc.CallFunc:create(function ()
            self:remove()
        end))
    end
end

function CCBManager:remove(fun)
    if fun and fun ~= "" then
        fun()
    end
    self.b_cache = false
    self:removeFromParent()
end

--播放相应动作
--actionName：播放的动作；
--fun：动作播放完回调函数
function CCBManager:runAnimationsByName(actionName, fun)
    -- print("+===========",self.ccbname,actionName)
    if actionName and actionName ~= "" then
        if self.animationMgr then
            self.animationMgr:runAnimationsForSequenceNamedTweenDuration(actionName, 0)
        end
        if not self.b_CompletedCallback and fun and fun ~= "" then
            self.animationMgr:setAnimationCompletedCallback(nil, cc.CallFunc:create(function ()
                fun()
                self.b_cache = false
                self.animationMgr:setAnimationCompletedCallback(nil,cc.CallFunc:create(function()end))
            end))
        end
    else
        if self.animationMgr then
            local autoPlaySeqId = self.animationMgr:getAutoPlaySequenceId()
            if -1 ~= autoPlaySeqId and not self.b_cache then
                self.animationMgr:runAnimationsForSequenceIdTweenDuration(autoPlaySeqId, 0)
            end
        end
    end
end

function CCBManager:callBackSpecial(str)
    local t1 = {}
    local specialList = {}
    local _, index = string.find(str, ";")
    if str then
        if nil == index then
            table.insert(t1, str)
        else 
            Tools.split(t1,str,";")
        end
    end
        
    for i1,v1 in ipairs(t1) do
        local special = {}
        local t2 = {}
        Tools.split(t2,v1,",")
        for i2,v2 in ipairs(t2) do
            local t3 = {}
            Tools.split(t3,v2,"=")
            for i3,v3 in ipairs(t3) do
                if t3[1] == "name" then
                    special.name = t3[2]
                elseif t3[1] == "path" then
                    special.path = t3[2]
                elseif t3[1] == "x" then
                    special.x = t3[2]
                elseif t3[1] == "y" then
                    special.y = t3[2]
                end
            end
        end
        table.insert(specialList,special)
    end
    return specialList
end

function CCBManager:callCCB(str, role) 
    local ccbName = ""
    for i,v in ipairs(self.specialList) do
        if str == v.name then
            ccbName = "ccb/"..v.path
            if Global.isWorld then
                local node = CCBManager.new(""):createCCB(ccbName, "", true)
                LayerManager.spriteLayer:addChild(node)
                
                if role then
                    node:setPosition(ccpAdd(ccp(v.x,v.y),ccp(role:getPosition())))
                    local scale = (role.dir == kDirection.kRight and node:getScale()) or -node:getScale()
                    node:setAnchorPoint(ccp(0,0))
                    node:setScaleX(scale)
                else
                    node:setPosition(ccpAdd(ccp(v.x,v.y),ccp(self:getPosition())))
                end
            else
                if not self.b_cache then
                    if Global.battle then
                        node = ccbCache:createCCB({ccbName=ccbName, animationName="", b_CompletedCallback=true, b_cache=true})
                        ccbCache:addCacheChild(LayerManager.spriteLayer,node)
                        if role then
                            node:setPosition(ccpAdd(ccp(v.x,v.y),ccp(role:getPosition())))
                            local scale = (role.dir == kDirection.kRight and node:getScale()) or -node:getScale()
                            node:setAnchorPoint(ccp(0,0))
                            node:setScaleX(scale)
                        else
                            node:setPosition(ccpAdd(ccp(v.x,v.y),ccp(self:getPosition())))
                        end
                    end
                else
                    self.b_cache = false
                    ccbCache.cacheRoleSkill:addCache(ccbName, 1)
                end
            end
            break
        end
    end
end

function CCBManager:resetParticle()
    local particles = self.animationMgr:getParticles()
    local particle = nil
    for i = 0, particles:count() - 1 do
        particle = particles:objectAtIndex(i)
        particle = tolua.cast(particle,"CCParticleSystemQuad")
        particle:resetSystem()
    end
end

--还原ccb数据
function CCBManager:resetCCB()
    self:setRotation(self.backup_data["rotation"])
    self:setScale(self.backup_data["scale"])
    self:setPosition(self.backup_data["position"])
    self:setAnchorPoint(self.backup_data["anchor"])
    self:setZOrder(self.backup_data["zorder"])
    self:setTag(self.backup_data["tag"])
end

--备份ccb数据
function CCBManager:backupCCB()
    self.backup_data["rotation"] = self:getRotation()
    self.backup_data["scale"] = self:getScale()
    self.backup_data["position"] = ccp(self:getPosition())
    self.backup_data["anchor"] = ccp(self:getAnchorPoint().x,self:getAnchorPoint().y)
    self.backup_data["zorder"] = self:getZOrder()
    self.backup_data["tag"] = self:getTag()

end


function CCBManager:readCCB(strFilePath, proxy, owner)
  
    if nil == proxy then
        return nil
    end

    local ccbReader = proxy:createCCBReader()
    local node      = ccbReader:load(strFilePath)
    local rootName  = "" 
    --owner set in readCCBFromFile is proxy
    if nil ~= owner then
        --Callbacks
        local ownerCallbackNames = ccbReader:getOwnerCallbackNames() 
        local ownerCallbackNodes = ccbReader:getOwnerCallbackNodes()
        local ownerCallbackControlEvents = ccbReader:getOwnerCallbackControlEvents()
        local i = 1
        for i = 1,table.getn(ownerCallbackNames) do
            local callbackName =  ownerCallbackNames[i]
            local callbackNode =  tolua.cast(ownerCallbackNodes[i],"cc.Node")

            if "function" == type(owner[callbackName]) then
                proxy:setCallback(callbackNode, owner[callbackName], ownerCallbackControlEvents[i])
            else
                print("Warning: Cannot find owner's lua function:" .. ":" .. callbackName .. " for ownerVar selector")
            end

        end

        --Variables
        local ownerOutletNames = ccbReader:getOwnerOutletNames() 
        local ownerOutletNodes = ccbReader:getOwnerOutletNodes()

        for i = 1, table.getn(ownerOutletNames) do
            local outletName = ownerOutletNames[i]
            local outletNode = tolua.cast(ownerOutletNodes[i],"cc.Node")
            owner[outletName] = outletNode
        end
    end

    local nodesWithAnimationManagers = ccbReader:getNodesWithAnimationManagers()
    local animationManagersForNodes  = ccbReader:getAnimationManagersForNodes()

    for i = 1 , table.getn(nodesWithAnimationManagers) do
        local innerNode = tolua.cast(nodesWithAnimationManagers[i], "cc.Node")
        local animationManager = tolua.cast(animationManagersForNodes[i], "cc.CCBAnimationManager")
        local documentControllerName = animationManager:getDocumentControllerName()
        if "" == documentControllerName then
            
        end
        if nil ~=  self.M[documentControllerName] then
            self.M[documentControllerName]["mAnimationManager"] = animationManager
        end
        
        --Callbacks
        local documentCallbackNames = animationManager:getDocumentCallbackNames()
        local documentCallbackNodes = animationManager:getDocumentCallbackNodes()
        local documentCallbackControlEvents = animationManager:getDocumentCallbackControlEvents()

        for i = 1,table.getn(documentCallbackNames) do
            local callbackName = documentCallbackNames[i]
            local callbackNode = tolua.cast(documentCallbackNodes[i],"cc.Node")
            if "" ~= documentControllerName and nil ~= self.M[documentControllerName] then
                if "function" == type(self.M[documentControllerName][callbackName]) then
                    proxy:setCallback(callbackNode, self.M[documentControllerName][callbackName], documentCallbackControlEvents[i])
                else
                    print("Warning: Cannot found lua function [" .. documentControllerName .. ":" .. callbackName .. "] for docRoot selector")
                end
            end
        end

        --Variables
        local documentOutletNames = animationManager:getDocumentOutletNames()
        local documentOutletNodes = animationManager:getDocumentOutletNodes()

        for i = 1, table.getn(documentOutletNames) do
            local outletName = documentOutletNames[i]
            local outletNode = tolua.cast(documentOutletNodes[i],"cc.Node")
            
            if nil ~= self.M[documentControllerName] then
                self.M[documentControllerName][outletName] = tolua.cast(outletNode, proxy:getNodeTypeName(outletNode))
            end 
        end
        --[[
        if (typeof(controller.onDidLoadFromCCB) == "function")
            controller.onDidLoadFromCCB();
        ]]--
        --Setup timeline callbacks
        local keyframeCallbacks = animationManager:getKeyframeCallbacks()

        for i = 1 , table.getn(keyframeCallbacks) do
            local callbackCombine = keyframeCallbacks[i]
            local beignIndex,endIndex = string.find(callbackCombine,":")
            local callbackType    = tonumber(string.sub(callbackCombine,1,beignIndex - 1))
            local callbackName    = string.sub(callbackCombine,endIndex + 1, -1)
            --Document callback

            if 1 == callbackType and nil ~= self.M[documentControllerName] then
                local callfunc = cc.CallFunc:create(self.M[documentControllerName][callbackName])
                animationManager:setCallFuncForLuaCallbackNamed(callfunc, callbackCombine);
            elseif 2 == callbackType and nil ~= owner then --Owner callback
                local callfunc = cc.CallFunc:create(owner[callbackName])--need check
                animationManager:setCallFuncForLuaCallbackNamed(callfunc, callbackCombine)
            end
        end
        --start animation
        local autoPlaySeqId = animationManager:getAutoPlaySequenceId()
        if -1 ~= autoPlaySeqId then
            animationManager:runAnimationsForSequenceIdTweenDuration(autoPlaySeqId, 0)
        end
    end

    return node
end

return CCBManager