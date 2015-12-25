--- 网络请求封装
local networkMgr = {
    callbackFunc = {},
    loadingRef = 0
}

local errorTab = {
    error_500 = "服务器出错了，请稍后再试！",
    error_502 = "服务器出问题了，请稍后再试！",
}


--[[--
    网络请求回调
]]
function networkMgr.httpRequestCallback(event)
    local request = event.request
    local tag = request:getTag()
    local ok = (event.name == "completed")
    if tag ~= "user_guide" and tag ~= "guild_noLoading" then
        networkMgr.loadingRef = networkMgr.loadingRef - 1
    end
    local retrunFlag = false
    local callbackTab = networkMgr.callbackFunc[tag]
    local status = "-100000"
    local all_config_version = ""
    -- print("httpRequestCallback======",tag,ok,tostring(callbackTab))
    if not ok then
        -- 请求失败，显示错误代码和错误消息
        -- print("======1======",request:getErrorCode(), request:getErrorMessage())
        local errCode = request:getErrorCode()
        if errCode ~= 0 then
            game_pop:show(request:getErrorMessage())
        end
        return
        -- if callbackTab.requestCount < 3 then--请求失败后重新连接
        --     local m_shared = 0
        --     function tick(shared)
        --         callbackTab.requestCount = callbackTab.requestCount + 1
        --         networkMgr.sendHttpRequestByParams(callbackTab)
        --     end
        --     scheduler.performWithDelayGlobal(tick, 1.0)
        --     return
        -- else
        --     retrunFlag = true
        -- end
    end
    local code = request:getResponseStatusCode()
    if code ~= 200 then
        local errorMsg = errorTab["error_" .. tostring(code)]
        if errorMsg then
            game_pop:show(errorMsg)
        else
            game_pop:show(string_config.m_net_req_failed)
        end
        retrunFlag = true
    end
    
    if not retrunFlag then    
        local buffer = request:getResponseString()
        local gameData = util_json:new(buffer)
        if gameData and (not gameData:isEmpty()) then 
            status = gameData:getNodeWithKey("status"):toStr()
            all_config_version = gameData:getNodeWithKey("all_config_version")
            if all_config_version then
                all_config_version = all_config_version:toStr()
            else
                all_config_version = ""
            end
            local client_upgrade = gameData:getNodeWithKey("client_upgrade")
            if(client_upgrade ~= nil)then
                local updataUrl = client_upgrade:getNodeWithKey("url"):toStr()
                networkMgr.openUrl(updataUrl)
            else
                -- game_data:responseGameData(gameData)
                if status == "0" then--正常 
                    -- game_scene:fillPropertyBarData()
                    if callbackTab ~= nil and type(callbackTab.callback) == "function" and gameData:getNodeWithKey("data") then
                        callbackTab.callback(tag,gameData,contentLength,status,all_config_version)
                    else
                        retrunFlag = true
                    end
                elseif status == "9527" or status == "error_21" then--多设备登录 提示  或者重新登录
                    if callbackTab ~= nil and (callbackTab.retrunFlag or callbackTab.noresFlag) then
                        callbackTab.callback(tag,nil,contentLength,status,all_config_version)
                    end
                    game_pop:close()

                    networkMgr.reLogin(gameData:getNodeWithKey("msg"):toStr())
                    retrunFlag = false
                else
                    local msg = gameData:getNodeWithKey("msg")
                    -- game_scene:addPop("game_error_pop",{errorStatus = status,errorMsg = msg:toStr(),tag = tag})
                    retrunFlag = true
                end
            end
        else
            --require("game_ui.game_pop_up_box").showAlertView(string_config.m_net_no_data)
            retrunFlag = true
        end
        gameData:delete()
    end
    
    if networkMgr.loadingRef <= 0 then
        --require("game_ui.game_loading").close()
    end
    if retrunFlag and callbackTab ~= nil and (callbackTab.retrunFlag or callbackTab.noresFlag) then
        callbackTab.callback(tag,nil,contentLength,status,all_config_version)
    end
    networkMgr.callbackFunc[tag] = nil
end
--[[--
    发送请求
]]
function networkMgr.sendHttpRequestNoLoading(callback, url, method ,params_ ,tag)
    if not method then method = "GET" end
    local params = ""
    if params_ ~= nil then
        if type(params_) == "table" then
            for k,v in pairs(params_) do
                params = params .. k .. "=" .. v .. "&"
            end
        elseif type(params_) == "string" then
            params = params_
        end
    end
    if string.upper(tostring(method)) == "GET" then
        url = url .. "&" .. params
    end
    print("sendHttpRequestNoLoading========",url,method,tag)
    local request = network.createHTTPRequest(callback, url, method)     
    request:setTag(tag)
    if string.upper(tostring(method)) == "POST" then
        request:setPOSTData(params,string.len(params))
    end
    request:start()
end
--[[--
    发送请求
]]
function networkMgr.sendHttpRequest(callback, url, method ,params_ ,tag , loadingFlag, retrunFlag,noresFlag)
    if tag ~= "user_guide" and tag ~= "guild_noLoading" then
        if loadingFlag == nil or loadingFlag == true then 
            --require("game_ui.game_loading").show() 
        else
            --require("game_ui.game_loading").show({opacity = 0})
        end
        networkMgr.loadingRef = networkMgr.loadingRef + 1
    end
    if retrunFlag == nil then retrunFlag = false end
    if networkMgr.callbackFunc[tag] == nil then
        local callbackParam = {}
        callbackParam.callback = callback
        callbackParam.url = url
        callbackParam.method = method
        callbackParam.params = params_
        callbackParam.tag = tag
        callbackParam.loadingFlag = loadingFlag
        callbackParam.retrunFlag = retrunFlag
        callbackParam.noresFlag = noresFlag
        callbackParam.requestCount = 1
        networkMgr.callbackFunc[tag] = callbackParam
    end
    networkMgr.sendHttpRequestNoLoading(networkMgr.httpRequestCallback,url,method,params_,tag)
end


--[[--
    发送请求
]]
function networkMgr.sendHttpRequestByParams(params)
    params = params or {}
    local tag = params.tag
    local loadingFlag = params.loadingFlag
    if tag ~= "user_guide" and tag ~= "guild_noLoading" then
        if loadingFlag == nil or loadingFlag == true then 
            --require("game_ui.game_loading").show() 
        else
            --require("game_ui.game_loading").show({opacity = 0})
        end
        networkMgr.loadingRef = networkMgr.loadingRef + 1
    end
    networkMgr.sendHttpRequestNoLoading(networkMgr.httpRequestCallback,params.url,params.method,params.params,params.tag)
end

function networkMgr.openUrl(url)
    -- local t_params = 
    -- {
    --     title = "提示",
    --     okBtnCallBack = function(target,event)
    --         --require("game_ui.game_pop_up_box").close() 
    --         util_system:openUrlOutside(url)
    --     end,   --可缺省
    --     cancelBtnCallBack = function(target,event) cclog("rightBtnCallBack") --require("game_ui.game_pop_up_box").close() end,   --可缺省
    --     okBtnText = "更新",       --可缺省
    --     cancelBtnText = "取消",
    --     text = client_upgrade:getNodeWithKey("msg"):toStr(),      --可缺省
    --     onlyOneBtn = true,          --可缺省
    -- }
    --require("game_ui.game_pop_up_box").show(t_params)
end

function networkMgr.reLogin(str)
    -- local t_params = 
    -- {
    --     title = "提示",
    --     okBtnCallBack = function(target,event)
    --         --require("game_ui.game_pop_up_box").close()
    --         game_scene:setVisibleBroadcastNode(false)
    --         game:resourcesDownload()
    --     end,   --可缺省
    --     okBtnText = "重新登录",       --可缺省
    --     text = str,      --可缺省
    --     onlyOneBtn = true,          --可缺省
    -- }
    --require("game_ui.game_pop_up_box").show(t_params)
end

--[[--
    打印所有的请求回调
]]
function networkMgr.printAllCallbackFunc()
    for k,v in pairs(networkMgr.callbackFunc) do
        cclog("printAllCallbackFunc tag = " .. k, "function = " .. type(v[1]))
    end
end
--[[--
    清除所有的请求回调
]]
function networkMgr.clearAllCallbackFunc()
    networkMgr.callbackFunc = {}
    networkMgr.loadingRef = 0
    --require("game_ui.game_loading").close()
end

return networkMgr
