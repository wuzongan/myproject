local HotUpdate = class("HotUpdate")
local updater = require("update.updater")
function HotUpdate:ctor()
    -- self.url = "http://10.5.20.243/update/"
    -- self.url = "http://75.126.171.102/static/update/"
    -- 版本差异数
    self.version_diff = 0  
    --当前重连请求次数
    self.requestCount = 1
    --最大重连请求次数
    self.maxRequestCount = 5
    --当前下载第几个包
    self.pack_count = 1

    self.http_tag = "update"

    self.curVersion = 0
    self.remoteInfo = nil
    self.localInfo = nil

    self.updateScene = require("update.UpdateScene")
    -- self:registerExit(self.updateScene)
    updater.init()
    self:enterScene(self.updateScene)
end

function HotUpdate:run()
    if device.platform ~= "windows" then
        self:clearUpdateDir()
        self:sendRequest()
    else 
        self:enterGame()
    end
end

function HotUpdate:clearUpdateDir()
    self.localInfo = updater.getLocalResInfo()
    local l_arr = self:split(self.localInfo.version,".")
    if l_arr then
        local bigCode = l_arr[1].."."..l_arr[2]
        local localCode = CCUserDefault:sharedUserDefault():getStringForKey("versionCode")
        if bigCode ~= localCode then
            if localCode and localCode ~= "" then
                local deviceRes = {"config", "iphonehd", "lua", "sound", "video"}
                for k,v in ipairs(deviceRes) do
                    updater.rmdir(device.writablePath..v.."/")
                end
            end
            CCUserDefault:sharedUserDefault():setStringForKey("versionCode", bigCode)
            CCUserDefault:sharedUserDefault():flush()
        end
    end
end

function HotUpdate:startUpdate()
    self.updateScene.init()
    self:updateRemoteInfo()
    self:setUpdateCount(self.pack_count, self.version_diff)
    updater.setRemoteInfo(self.remoteInfo, self.localInfo)
    function func(event)
        if event == "success" then
            self.pack_count = self.pack_count + 1
            if self.pack_count < self.version_diff + 1 then
                self:setUpdateCount(self.pack_count, self.version_diff)
                self:updateRemoteInfo()
                updater.setRemoteInfo(self.remoteInfo, self.localInfo)
                self.updateScene.startUpdate(func)
            else
                self:enterGame()
            end
        else
            local t_params = 
            {
                title = string_config.m_title_prompt,
                okBtnCallBack = function(target,event)
                    game_util:closeAlertView()
                    self.updateScene.startUpdate(func)
                end,   --可缺省
                cancelBtnCallBack = function(target,event)
                    game_util:closeAlertView()
                    self:enterGame()
                end,
                okBtnText = "重试",       --可缺省
                cancelBtnText = "跳过",
                text = "更新失败，请检查SD状态或者存储空间是否已满。重试重新下载，跳过直接进入游戏。",      --可缺省
                onlyOneBtn = false,  
            }
            game_util:openAlertView(t_params)
        end
    end
    self.updateScene.startUpdate(func)
end


function HotUpdate:sendRequest()
    local function getRequest(tag,response,contentLength)
        print("getRequest================"..tag,response,contentLength)
        local ok = response:isSucceed()
        local code = response:getResponseCode()
        local data = response:getResponseDataBuffer()
        if not ok then
            local t_params = 
            {
                title = string_config.m_title_prompt,
                okBtnCallBack = function(target,event)
                    game_util:closeAlertView()
                    self:sendRequest()
                end,   --可缺省
                cancelBtnCallBack = function(target,event)
                    game_util:closeAlertView()
                    exitGame()
                end,
                okBtnText = "确定",       --可缺省
                cancelBtnText = "取消",
                text = "网络连接异常，确实需要重连？",      --可缺省
                onlyOneBtn = false,  
            }
            game_util:openAlertView(t_params)
            return
        end
        -- closeWait()
        self.remoteInfo = loadstring(data)()
        
        self:checkVersion()
    end
    local httpReq = CCHttpRequest:new()
    cclog("http update start -----" .. "url = ",self.localInfo.update_url)
    httpReq:setUrl(self.localInfo.update_url)
    httpReq:registerScriptTapHandler(getRequest)
    httpReq:setRequestType(0)
    -- httpReq:setTag("hotupdate")
    CCHttpClient:getInstance():send(httpReq)
    httpReq:release()
end

function HotUpdate:checkVersion()
    print("==========checkVersion==========")
    print("remoteInfo version=======",self.remoteInfo.version)
    print("localInfo version=======",self.localInfo.version)
    assert(self.remoteInfo, "remoteInfo is empty!")
    local local_arr = self:split(self.localInfo.version,".")
    local remote_arr = self:split(self.remoteInfo.version,".")
    local versionList = self.remoteInfo.version_list
    local debug = self.remoteInfo.isDebug or false
    local ver_diff = 0
    for k,v in pairs(versionList) do
        print(k,v)
    end
    if local_arr[1] == remote_arr[1] and local_arr[2] == remote_arr[2] 
        and tonumber(local_arr[3]) < tonumber(remote_arr[3]) then
        if #versionList > 0 then
            for i=#versionList,1,-1 do
                local cur_ver_arr = self:split(versionList[i],".")
                if tostring(self.localInfo.version) ~= tostring(versionList[i])
                and tonumber(local_arr[3]) < tonumber(cur_ver_arr[3])  then
                    ver_diff = ver_diff + 1
                end
            end
        end
        print("==========version_diff=",ver_diff)
        self.version_diff = ver_diff
        if not debug then
            if self.version_diff > 0 then
                self:startUpdate()
            else
                self:enterGame()
            end 
        else 
            self:enterGame()
        end
    else
        self:enterGame()
    end
end

function HotUpdate:updateRemoteInfo()
    local i, j = string.find(self.localInfo.update_url, "/config.lua")
    local url = string.sub(self.localInfo.update_url, 1, i)
    
    self.remoteInfo.version = self.remoteInfo.version_list[#self.remoteInfo.version_list - self.version_diff + self.pack_count]
    self.remoteInfo.package = url .. self.remoteInfo.version .. "/update.zip"

    CLIENT_VERSION = self.remoteInfo.version
end

function HotUpdate:split(str, delimiter)
    if (delimiter=='') then return false end
    local pos,arr = 0, {}
    -- for each divider found
    for st,sp in function() return string.find(str, delimiter, pos, true) end do
        table.insert(arr, string.sub(str, pos, st - 1))
        pos = sp + 1
    end
    table.insert(arr, string.sub(str, pos))
    return arr
end

function HotUpdate:goToDownLoad( de )
    if device.platform == "android" then        --Android设备打开游戏下载页
        self:goToUrl(de.android_url)
    else                                        --IOS设备打开游戏下载页
        self:goToUrl(de.ios_url)
    end
end


function HotUpdate:goToUrl(url)
	device.openURL(url)
	CCDirector:sharedDirector():endToLua()
end

function HotUpdate:enterGame()
    require("update/update_pre_files")
    require("game").new():run()
end

function HotUpdate:registerExit( obj )
    local function onKeypad(event)
        if device.platform == "android" then 
            if event.key == "back" then
                local function onButtonClicked(event)
                    if event.buttonIndex == 1 then
                        CCDirector:sharedDirector():endToLua()
                    else
                        -- device.cancelAlert()
                    end
                end
                device.showAlert("","Really quit the game?",{"yes","no"},onButtonClicked)
            elseif event.key == "menu" then
            end
        end
    end
    obj:addNodeEventListener(cc.KEYPAD_EVENT, onKeypad)
    obj:setKeypadEnabled(true)
end

function HotUpdate:setUpdateCount(cur, total)
    local m_text = "正在更新文件..."
    m_text = m_text .. cur .. "/" .. total
    self.updateScene.m_res_num:setString(m_text)
    -- if not self.updateScene.progress then
    --     self.updateScene.progress =  ui.newTTFLabel({
    --         text = m_text,
    --         font = "Arial",
    --         size = 20,
    --         x = self.updateScene.m_load_bar:getContentSize().width/2,
    --         y = self.updateScene.m_load_bar:getContentSize().height + 10,
    --         color = ccORANGE, -- 使用纯红色
    --     })
    --     self.updateScene.m_load_bar:addChild(self.updateScene.progress)
    --     self.updateScene.m_res_num:setString(m_text)
    -- else
    --     self.updateScene.progress:setString(m_text)
    -- end
end

function HotUpdate:enterScene(__scene)
    local sharedDirector = CCDirector:sharedDirector()
    if sharedDirector:getRunningScene() then
        sharedDirector:replaceScene(__scene)
    else
        sharedDirector:runWithScene(__scene)
    end
end

function openWait(title, msg)
    local className = "com/koramgame/ggplay/dzpken/KunlunSDK"
    local m_title = title or ""
    local m_msg = msg or "loading.."
    if device.platform == "android" then 
        luaj.callStaticMethod(className, "showProgressDialog", {m_title, m_msg}, "(Ljava/lang/String;Ljava/lang/String;)V")
    elseif device.platform == "ios" then
    end
end

function closeWait()
    local className = "com/koramgame/ggplay/dzpken/KunlunSDK"
    if device.platform == "android" then 
        luaj.callStaticMethod(className, "hideProgressDialog", {}, "()V")
    elseif device.platform == "ios" then
    end
end

return HotUpdate