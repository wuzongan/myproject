local game_manager = {}

--游戏开启时调用,准备加载模块与加载界面
function game_manager.on_startload()
	--事件管理模块初始化
 --    game.pathMgr = require("game.manager.PathManager").new()
	-- game.baseMgr = require("game.manager.BaseManager").new()
    --弹出窗口管理
 --    game.popwndMgr = require("game.manager.PopWindowManager").new()
	-- game.loadingMgr = require("game.loading.LoadingManager").new()
   	--场景加载    
    -- game.sceneMgr = require("scenes.scene_manager")   
    -- game.soundMgr = require("game.manager.SoundManager").new()
    -- game.settingMgr = require("scenes.setting.SettingManager").new()
    --ccb
    game.ccbMgr = require("game.ccb.CCBManager")
    game.ccbCache = require("game.ccb.CCBCacheManager").new()
    

end

--游戏更新完成,连接服务器前调用
function game_manager.on_connect_server()
    --初始化网络
	-- game.networkMgr = require("game.network.NetworkManager").new()
    game.networkMgr = require("game.network.network_manager")
end

--连接游戏成功,进入登陆界面前调用
function game_manager.on_login()
	--初始化登陆模块,注册登陆消息监听
	-- require("game.login.AccountLogin").init()
    --初始化层玩家数据
    -- game.roleAttrInfo  = require("game.RoleAttrInfo").new()
	--加载游戏数据
	-- proto = require("proto.data")
end

--登陆成功,进入游戏前调用
function game_manager.init()
	--初始化各个进入游戏前需要的模块
	game.skillMgr = require("game.manager.SkillManager").new()	
    game.loadingMgr = require("game.manager.LoadingManager").new()    
end

function game_manager.initLayerMgr(scene)
    game.layerMgr = require("game.manager.LayerManager").new(scene)
end


return game_manager