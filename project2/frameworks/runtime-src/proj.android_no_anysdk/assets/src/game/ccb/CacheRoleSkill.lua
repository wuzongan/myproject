local CacheRoleSkill = class("CacheRoleSkill", function()
    return display.newNode()
end)

function CacheRoleSkill:ctor()
	self.ccbCacheList = {}
	self.effectPath = "ccb/par/"
	self.rolePath = "ccb/char/"
	LayerManager.spriteLayer:addChild(self)
	self:cacheMostersSkill()
	self:cacheOther()
end

function CacheRoleSkill:cacheOther()
	self:addCache("ccb/par/numbers_ani", 1)
end

function CacheRoleSkill:cacheMostersSkill()
	if Global.stage then
		local skillList,role_ccbs = Global.stage:getSkillIdList()
		for k,v in pairs(skillList) do
			local sd = data.skill_config(v)
	        local sv = require("func.SkillVO").new(sd,v)
			self:cacheCCBBySkill(sv)
		end
		for k,v in pairs(role_ccbs) do
			self:addCache(v, 2)
		end
	end
end

function CacheRoleSkill:cacheByRole(role)
	local skillList = {}
	if role and Global.roleManager:isSourceRole(role, RoleOpType.OPERATION) then
		skillList = Global.skillMgr:getRoleSkillList(role.roleId)
		for k,v in pairs(skillList) do
			self:cacheCCBBySkill(v)
		end
	end
end

function CacheRoleSkill:cacheCCBBySkill(skill)
	local cacheSkillCCBS = {}
	if skill then
		cacheSkillCCBS = {
			skill.prepare_effect[1],
			skill.attack_effect[1],
			skill.attacking_effect[1],
			skill.attacked_effect[1],
			skill.damage_effect[1],
			skill.aura_effect[1],
			skill.attack_fly[1]
		}
	end
	for k,v in pairs(cacheSkillCCBS) do
		if "" ~= v then
			self:addCache(self.effectPath..tostring(v), 1)
		end
	end
	
end

function CacheRoleSkill:addCache(ccbName, cache_type)
	local ccb = nil
	if not self.ccbCacheList[ccbName] then
		if cache_type == 1 then
			ccb = CCBManager.new("", true):createCCB(ccbName, "", false)
		else
			ccb = require("role.RoleCCB").new(self.rolePath..tostring(ccbName), true)
		end
		ccb:setVisible(false)
		self.ccbCacheList[ccbName] = ccb
		self:addChild(ccb)
	end
end

function CacheRoleSkill:hasCacheByName(ccbName)
	if self.ccbCacheList[ccbName] then
		return true
	end
	return false
end

function CacheRoleSkill:removeCCBByName(ccbName)
	if self.ccbCacheList[ccbName] then
		self.ccbCacheList[ccbName]:removeFromParent()
		self.ccbCacheList[ccbName] = nil
	end
end

function CacheRoleSkill:handleCaCheByName(ccbName)
	local hasCCB = self:hasCacheByName(ccbName)
    if hasCCB then
        self:removeCCBByName(ccbName)
    end
end

return CacheRoleSkill