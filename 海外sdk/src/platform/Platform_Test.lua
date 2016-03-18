local scheduler = require("framework.scheduler")
local Platform_GooglePlay = require("app.platform.Platform_GooglePlay")
local Platform_Appstore = require("app.platform.Platform_Appstore")
local Platform_Facebook = require("app.platform.Platform_Facebook")
local Platform_Wchat = require("app.platform.Platform_Wchat")

local Platform_Test = class("Platform_Test",function()
    return display.newLayer()
end)

--需要哪个平台，把对应的源码注释打开和关闭
function Platform_Test:ctor()
    -- self:createMenu_fb_googlepay()    
    -- self:createMenu_fb_appstore()
    self:createMenu_wchat_appstore()
end

function Platform_Test:createMenu_fb_googlepay()
    
    self:createButton("facebook_login",cc.p(display.cx, display.cy + 100), function()
        local fb = Platform_Facebook.new()
        fb:login()
    end)
    self:createButton("facebook_share",cc.p(display.cx, display.cy), function()
        local fb = Platform_Facebook.new()
        fb:share()
    end)
    -- self:createButton("googleplay_pay",cc.p(display.cx, display.cy - 100), function()
        
    -- end)
    local gp = Platform_GooglePlay.new()
    gp:createMenu_Googleplay(self)
    gp:addTo(self)
    
end

function Platform_Test:createMenu_fb_appstore()
    local fb = Platform_Facebook.new()
    self:createButton("facebook_login",cc.p(display.cx, display.cy + 100), function()
        local fb = Platform_Facebook.new()
        fb:login()
    end)
    self:createButton("facebook_share",cc.p(display.cx, display.cy), function()
        local fb = Platform_Facebook.new()
        fb:share()
    end)
    self:createButton("appstore_pay",cc.p(display.cx, display.cy - 100), function()
        local as = Platform_Appstore.new()
        as:pay("com.lein.pppoker.appstore1.pay1")
    end)
end

function Platform_Test:createMenu_wchat_appstore()
    self:createButton("wchat_login",cc.p(display.cx, display.cy + 100), function()
        local wchat = Platform_Wchat.new()
        wchat:login()
    end)
    self:createButton("wchat_share",cc.p(display.cx, display.cy), function()
        local wchat = Platform_Wchat.new()
        wchat:share()
    end)
end



function Platform_Test:createButton(_name, _pos, _func)
    cc.ui.UIPushButton.new({})
        :setButtonLabel("normal", cc.ui.UILabel.new({
            UILabelType = 2,
            text = _name,
            size = 50,
            color = cc.c3b(255,0,0)
        }))
        :onButtonClicked(_func)
        :pos(_pos.x,_pos.y)
        :addTo(self)
end


return Platform_Test
