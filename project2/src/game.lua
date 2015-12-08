require("config.game_config")
scheduler = require(cc.PACKAGE_NAME .. ".scheduler")
local AppBase = cc.mvc.AppBase
local gameManager = require("game.manager.game_manager")

game = {
	loadingMgr = nil,
	skillMgr = nil,
	networkMgr = nil,
	ccbMgr = nil,
	ccbCache = nil,
	layerMgr = nil
}

--游戏启动,开始加载
function game.startup()

    local app = AppBase.new("tywx")
	app:addEventListener( AppBase.APP_ENTER_BACKGROUND_EVENT, game.onEnterBackground, nil)
	app:addEventListener( AppBase.APP_ENTER_FOREGROUND_EVENT, game.onEnterForeground, nil)
    
    gameManager.on_startload()
	    
    game.onLoadComplete()
end

--加载完成,初始化游戏
function game.onLoadComplete()
	gameManager.on_connect_server()
    gameManager.on_login()

    local scene = display.newScene()
	display.replaceScene(scene)
	-- gameManager.initLayerMgr(scene)
	scene:addChild(require("scenes.MainScene").new())
	-- scheduler.performWithDelayGlobal(function()
		-- game.sceneMgr.showlogin()
	-- end, 1)
end

function game.onNetConnect()
	-- showLoadingEff()
	scheduler.performWithDelayGlobal(function()
		-- hideLoadingEff() 
		-- game.sceneMgr.entergame()
	end, 2)
end

--玩家断线后重连成功
function game.onNetWorkReConnect()
	-- game.worldMgr:reJoinWorld()
end

function game.onJoinGame()
	local platform = PLATFORM_NAME
	local server_id = game.roleAttrInfo:getSeverId()
	local account = game.roleAttrInfo:getAccountValue()
	
	gameManager.init()
	game.worldMgr:joinWorld({fun = function(p) print(p) end,params = "SDSDSDS"})
end

function game.onInBreakThrough()
	game.sceneMgr.showbreakthrough()
end

function game.exit()
	-- if game.networkMgr ~= nil then
	-- 	game.networkMgr:close()
	-- end
	-- game.settingMgr:onExitGame()
    AppBase:endToLua()
   
end

-------------------------------------------------------------------------------------------------------

function game.addExitGameListener(scenes)
	-- if device.platform ~="android" then return end
	-- scenes:performWithDelay(function()
	-- 	local layer =display.newLayer()
	-- 	layer:addKeypadEventListener(function(event)
	-- 		if event =="back" then game.onExitDialog(game.onExitButton) end 
	-- 	end)
	-- 	scenes:addChild(layer)
	-- 	layer:setKeypadEnabled(true)
	-- end,0.5)
end


function game.onExitDialog(listenerFun)

	-- device.showAlert(title, message, buttonLabels, listenerFun)
end

function game.onExitButton(event)
	if 1 == event.buttonIndex then
		-- 确定
		game.exit()
	else
		-- 取消
	end
end

-------------------------------------------------------------------------------------------------------
--响应进入后台
function game.onEnterBackground()
	-- game.settingMgr:onEnterBackground()
end

--响应进入前台
function game.onEnterForeground()
	-- game.settingMgr:onEnterForeground()
end


return game