local Platform_Wchat = class("Platform_Wchat",function()
    return display.newLayer()
end)

function Platform_Wchat:ctor()
    
end


--登录
function Platform_Wchat:login()
    local wchat = plugin.PluginManager:getInstance():loadPlugin("UserWchat")
    wchat:login(function(code, msg)
        print(code,msg)
    end)
end

--分享
function Platform_Wchat:share(params)
    params = {}

    local wchat = plugin.PluginManager:getInstance():loadPlugin("ShareWchat")
    local param = {RoomNumber = tostring(nRoomNumber),
        Invitetime = os.date("%m/%d %H:%M"),
        RoomName = params.RoomName or "PPPoker",
        RoomType = params.RoomType or "自由场",
    }
    wchat:share(param, function(code, msg)
        print("msg is wchat code----"..msg)
    end)
end

return Platform_Wchat
