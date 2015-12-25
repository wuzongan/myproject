local RoleManager = class("RoleManager")

function RoleManager:ctor()
	self.roles = {}
	self.ownRoles = {}
	self.enemys = {}
end

function RoleManager:createRole(data)
end

function RoleManager:removeRole(data)
end

function RoleManager:getOwnRoles()
end

function RoleManager:getEnemys()
end

function RoleManager:updateRoles()
end

return RoleManager