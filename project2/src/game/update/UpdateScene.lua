local updater = require("update.updater")


local us = cc.Scene:create()
us.name = "updateScene"

local localResInfo = nil

function us._addUI()
    -- cc.SpriteFrameCache:getInstance():addSpriteFrames("ccbResources/ui_create_role_res.plist");
    -- local ccbNode = luaCCBNode:create()
    -- ccbNode:openCCBFile("ccb/ui_hot_update.ccbi")
    -- us.m_res_num = tolua.cast(ccbNode:objectForName("m_res_num"), "CCLabelTTF")
    -- us.m_progress = tolua.cast(ccbNode:objectForName("m_pro"), "CCLabelTTF")
    -- us.m_state_label = tolua.cast(ccbNode:objectForName("m_state"), "CCLabelTTF")
    -- us.m_progress_node = tolua.cast(ccbNode:objectForName("m_progress_node"), "CCNode")
    -- us.m_progress_bar = ExtProgressTime:createWithFrameName("cjjs_jindutiao_1.png","cjjs_jindutiao.png")
    -- us.m_progress_bar:setCurValue(0,false)
    -- us.m_progress_node:setVisible(false)
    -- us.m_progress_node:addChild(us.m_progress_bar)
    -- us:addChild(ccbNode)
end

function us.init()
    us.m_progress_node:setVisible(true)
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
        us.m_progress_bar:setCurValue(updater.stateValue,false)
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