local Platform_GooglePlay = class("Platform_GooglePlay",function()
    return display.newNode()
end)

function Platform_GooglePlay:ctor()
    self.iap = plugin.PluginManager:getInstance():loadPlugin("IAPGooglePlay")
end


function Platform_GooglePlay:createMenu_Googleplay(node)
    node:createButton("googleplay_pay",cc.p(display.cx, display.cy - 100), function()
        self:pay()
    end)
end

--支付
function Platform_GooglePlay:pay()
    local param = {}
    param.IAPId = "com.lein.pppoker.pay1"
    local uid = 101
    local serverId = 1
    param.IAPSecKey = tostring(uid) .. "-" .. tostring(serverId) .. '-' .. tostring(param.IAPId) .. '-' .. tostring(os.time())

    self.iap:payForProduct(param,function(code, info)
        print("=========服务端验证http://www.pengmj.com/285.html===========")
        print(info)
    end)
end

return Platform_GooglePlay
