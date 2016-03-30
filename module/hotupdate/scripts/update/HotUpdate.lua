-- require "shared.extern"
require("update.resinfo")
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

function HotUpdate:startCheck()
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
                local deviceRes = {"scripts", "res"}
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
            local node = require("func.YesOrNo").new("更新失败，请检查SD状态或者存储空间是否已满。'重试'重新更新，'跳过'直接进入游戏。","重试","跳过")
            node:setHandler(function()
                self.updateScene.startUpdate(func)
            end)
            node:noHandler(handler(self,self.enterGame))
            self.updateScene:addChild(node)
        end
    end
    self.updateScene.startUpdate(func)
end


function HotUpdate:sendRequest()
    function getRequest(event)
        local ok = (event.name == "completed")
        local request = event.request
        if not ok then
            -- request failure服务器连接失败
            local node = require("func.YesOrNo").new("网络连接异常，确实需要重连？","确定","取消")
            node:noHandler(function()
                CCDirector:sharedDirector():endToLua()
            end)
            node:setHandler(handler(self,self.sendRequest))
            self.updateScene:addChild(node)
            return
        end

        local code = request:getResponseStatusCode()
        if code ~= 200 then
            return
        end

        local response = request:getResponseString()
        self.remoteInfo = loadstring(response)()
        self:checkVersion()
    end

    print(self.localInfo.update_url)
    local request = network.createHTTPRequest(getRequest, self.localInfo.update_url, "GET")
    request:start()
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
    -- require("update/update_pre_files")
    require("game")
    game:startup()
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
end

function HotUpdate:enterScene(__scene)
    local sharedDirector = CCDirector:sharedDirector()
    if sharedDirector:getRunningScene() then
        sharedDirector:replaceScene(__scene)
    else
        sharedDirector:runWithScene(__scene)
    end
end

return HotUpdate