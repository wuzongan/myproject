local Platform_Facebook = class("Platform_Facebook",function()
    return display.newLayer()
end)

function Platform_Facebook:ctor()
end


--登录
function Platform_Facebook:login()
    -- if plugin.FacebookAgent:getInstance():isLoggedIn() then
    --     print("already login in")
    -- else
        plugin.FacebookAgent:getInstance():login(function(ret, msg)
            print(string.format("type is %d, msg is %s", ret, msg))
        end
        )
    -- end
end

--分享

-- 例：
-- local params = {
--     description =  "Cocos2dx-lua is a great game engine",
--     title = "Cocos2dx-lua",
--     link = "http://www.cocos2d-x.org",
--     imageUrl = "http://files.cocos2d-x.org/images/orgsite/logo.png",
-- }
function Platform_Facebook:share(params)
    local params = {
        description =  "Cocos2dx-lua is a great game engine",
        title = "Cocos2dx-lua",
        link = "http://www.cocos2d-x.org",
        imageUrl = "http://files.cocos2d-x.org/images/orgsite/logo.png",
    }
    plugin.FacebookAgent:getInstance():share(params, function(ret, msg)
        print(msg)
    end)
end

return Platform_Facebook
