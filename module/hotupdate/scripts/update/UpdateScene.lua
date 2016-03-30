local updater = require("update.updater")
require "CCBReaderLoad"

local us = CCScene:create()
us.name = "updateScene"

UpdateControl = UpdateControl or {}
ccb["updateControl"] = UpdateControl

local localResInfo = nil

function us._addUI()
    local proxy = CCBProxy:create()
    local ccbNode  = CCBReaderLoad("ccb/ui/ui_hot_update.ccbi",proxy,true,"")
    us:addChild(ccbNode)
    us.progressContainer = tolua.cast(UpdateControl["complete"],"CCSprite")
    us.m_res_num = tolua.cast(UpdateControl["m_res_num"], "CCLabelTTF")
    us.m_progress = tolua.cast(UpdateControl["m_pro"], "CCLabelTTF")
    us.m_state_label = tolua.cast(UpdateControl["m_state"], "CCLabelTTF")
    us.root_layer = tolua.cast(UpdateControl["root_layer"], "CCLayer")
    us.root_layer:setVisible(false)

    us.progress = CCProgressTimer:create(CCSprite:create("img/ui/other/other_progress_bar_cyan_S.png"))
    us.progress:setPosition(us.progressContainer:getContentSize().width / 2, us.progressContainer:getContentSize().height / 2)
    us.progress:setType(1)
    us.progress:setMidpoint(CCPointMake(0, 0)) -- 设计进度条方向
    us.progress:setAnchorPoint(ccp(0.5, 0.5))
    us.progress:setBarChangeRate(CCPointMake(1, 0))
    us.progress:setPercentage(0)
    us.progressContainer:addChild(us.progress)

    
end

function us.init()
    us.progress:setVisible(true)
    us.root_layer:setVisible(true)
end

function us._getres(path)
    if not localResInfo then
        localResInfo = updater.getResCopy()
    end
    for key, value in pairs(localResInfo.oth) do
        print("us._getres:", key, value)
        local pathInIndex = string.find(key, path)
        if pathInIndex and pathInIndex >= 1 then
            print("us._getres getvalue:", path)
            --res[path] = value
            return value
        end
    end
    return path
end

function us._sceneHandler(event)
    if event == "enter" then
        print(string.format("updateScene \"%s:onEnter()\"", us.name))
    elseif event == "cleanup" then
        print(string.format("updateScene \"%s:onCleanup()\"", us.name))
        us.onCleanup()
    elseif event == "exit" then
        print(string.format("updateScene \"%s:onExit()\"", us.name))
        us.onExit()

        if DEBUG_MEM then
            print("----------------------------------------")
            print(string.format("LUA VM MEMORY USED: %0.2f KB", collectgarbage("count")))
            CCTextureCache:sharedTextureCache():dumpCachedTextureInfo()
            print("----------------------------------------")
        end
    end
end

function us._updateHandler(event, value)
    updater.state = event
    if event == "success" then
        updater.stateValue = value:getCString()
        updater.updateFinalResInfo()
        if us._succHandler then
            us._succHandler(event)
        end
    elseif event == "error" then
        updater.stateValue = value:getCString()
        if us._succHandler then
            us._succHandler(event)
        end
    elseif event == "progress" then
        updater.stateValue = tostring(value:getValue())
        us.progress:setPercentage(updater.stateValue)
        updater.stateValue = updater.stateValue .. "%"
    elseif event == "state" then
        updater.stateValue = value:getCString()
    end
    -- us._label:setString(updater.stateValue)
    us.m_state_label:setString(updater.stateValue)
    assert(event ~= "error", 
        string.format("Update error: %s !", updater.stateValue))
end


function us.addListener(handler)
    updater.update(us._updateHandler)
    us._succHandler = handler
    return us
end

function us.startUpdate(handler)
    updater.update(us._updateHandler)
    us._succHandler = handler
end


function us.onExit()
    updater.clean()
    us:unregisterScriptHandler()
end

function us.onCleanup()
end

--us:registerScriptHandler(us._sceneHandler)
us:registerScriptHandler(function(event)
    print("-registerScriptHandler----------",event)
    us._sceneHandler(event)
end)

us._addUI()
return us