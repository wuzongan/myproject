local BattleManager = class("BattleManager")

function BattleManager:ctor()
	ccbCache.cacheRoleSkill = require("game.ccb.CacheRoleSkill").new(self)
end

--初始化副本数据
function BattleManager:initData()
end

--副本主渲染
function BattleManager:render()
end

function BattleManager:joinBattle()
end

function BattleManager:leaveBattle()
end

--更新战斗状态
function BattleManager:updateState(state)
end

--释放战斗资源
function BattleManager:releaseBattle()
end

function BattleManager:initMap(data)
	-- body
end

function BattleManager:initRolesToMap( ... )
	-- body
end

function BattleManager:setAutoBattle()
end

function BattleManager:setResult(data)
end


return BattleManager