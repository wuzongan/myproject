
local MainScene = class("MainScene", function()
    return display.newLayer()
end)
local kFindWayMoveTag = 1
local _moveDirection = 0
local _speed = 250
local _scale = 0.4

kDirection = {
    kDirNone = 0,
    kLeft = 1,
    kRight = -1,
}

function MainScene:ctor()
    local factory = db.DBCCFactory:getInstance()
    factory:loadDragonBonesData("armatures/dragon/skeleton.xml", "Dragon");
    factory:loadTextureAtlas("armatures/dragon/texture.xml", "Dragon");
    self.dbnode = factory:buildArmatureNode("Dragon")
    -- self.dbnode:getAnimation():gotoAndPlay("stand")--stand\walk\jump\fail
    self.dbnode:setPosition(display.cx,display.cy)
    self.dbnode:setScale(-_scale,_scale)
    
    local bg = game.ccbMgr.new():createCCB("ccb/ui/function/world_map.ccbi")
    self:addChild(bg)
    self:addChild(self.dbnode)
    local ui = game.ccbMgr.new("MainUI")
    ui.M["chatmenu"] = handler(self,self.func1)
    ui:createCCB("ccb/ui/main/main_ui.ccbi","Default Timeline",false,handler(self, self.func2))
    ui.M["money"]:setString("111")
    self:addChild(ui)

    self:setNodeEventEnabled(true)
    self:setTouchEnabled(true)
    -- self:setTouchSwallowEnabled(false)  
    function userServerList( ... )
        print("success connect network!!!!!")
    end
    local params = {}
    params.account = "wza"
    game.networkMgr.sendHttpRequest(userServerList, game_url.getUrlForKey("user_server_list"), "GET", {},"user_server_list",true,true);
    -- self:readJson()
    self:initStateMachine()

end

function MainScene:createButton(_state, _name, _pos)
    cclog(_pos)
    cc.ui.UIPushButton.new({})
        :setButtonLabel("normal", cc.ui.UILabel.new({
            UILabelType = 2,
            text = _name,
            size = 32,
            color = cc.c3b(255,0,0)
        }))
        :onButtonClicked(function()
            self:changeState(_state)
        end)
        :pos(_pos.x,_pos.y)
        :addTo(self)
end

--初始化状态机
function MainScene:initStateMachine()
    self.fsm_ = {}
    cc.GameObject.extend(self.fsm_)
    :addComponent("components.behavior.StateMachine")
    :exportMethods()
    -- stand\walk\jump\fail
    self.fsm_:setupState({
        events = {
            {name = "idle", from = {"none", "move", "jump", "fall"},   to = "idle" },
            {name = "move", from = "idle",  to = "move"},
            {name = "jump", from = {"*"},  to = "jump"},
            {name = "fall", from = {"*"}, to = "fall"},
        },
        callbacks = {
            onbeforestart = function(event) cclog("[FSM] STARTING UP") end,
            onstart       = function(event) cclog("[FSM] READY") end,
            -- onidle        = function(event) self:updateState(event.name) end,
            -- onmove        = function(event) self:updateState(event.name) end,
            -- onjump        = function(event) self:updateState(event.name) end,
            -- onfall        = function(event) self:updateState(event.name) end,
            onchangestate = function(event) 
                cclog("[FSM] CHANGED STATE: ".. event.name .. " from " .. event.from .. " to " .. event.to) 
                self:updateState(event.name)
            end,
        },
    })

    self:createButton("jump","跳跃",cc.p(display.cx + 40, display.top - 100))
    self:createButton("fall","下降",cc.p(display.cx + 40, display.top - 140))
end

function MainScene:updateState(state)
    if state == "idle" then
        self.dbnode:getAnimation():gotoAndPlay("stand")
    elseif state == "move" then
        self.dbnode:getAnimation():gotoAndPlay("walk")
    elseif state == "jump" then
        self.dbnode:getAnimation():gotoAndPlay("jump")
    elseif state == "fall" then
        self.dbnode:getAnimation():gotoAndPlay("fall")
    end
end

function MainScene:changeState(state)
    if self.fsm_:canDoEvent(state) then
        self.fsm_:doEvent(state)
    end
end

function MainScene:readJson()
    local util = require "common.util"
    local json = require "cjson"
    local tableName = "res/data/guide.config"
    local pathKey = cc.FileUtils:getInstance():fullPathForFilename(tableName)
    -- local dataJson = util.file_load(pathKey)
    local f = io.open(pathKey,"r")
    local t = f:read("*all")
    local result = json.decode(dataJson)
    f:close()
    return result
end

function MainScene:func1()
    print("--------------1")
end

function MainScene:func2()
end

function MainScene:onEnter()
    self:addNodeEventListener(cc.NODE_TOUCH_EVENT, function(event)
    	if event.name=='began' then
    		return true
    	elseif event.name=='ended' then
    		self:moveDes(event.x,event.y)
    	end
    end)
    self:changeState("idle")
end

function MainScene:moveDes(x,y)
	local destination = cc.p(x,y)
	local s_pos = cc.p(self.dbnode:getPosition())
	local pos = cc.pSub(destination, s_pos)
    _moveDirection = self:getDirectionFromOffset(pos)
    self:_updateBehavior()

    local time = cc.pGetDistance(destination, s_pos)/_speed
    local action = cc.Sequence:create(cc.MoveTo:create(time,destination),cc.CallFunc:create(function()
        self:changeState("idle")
    end))
    self.dbnode:stopActionByTag(kFindWayMoveTag)
    action:setTag(kFindWayMoveTag)
    self.dbnode:runAction(action)
end

function MainScene:_updateBehavior()
	if _moveDirection == 0 then
		self:changeState("idle")
	else
		self.dbnode:setScaleX(_moveDirection*_scale)
		self:changeState("move")
	end
end

function MainScene:getDirectionFromOffset(pos)
	local dir = kDirection.kLeft
    local angle = cc.pToAngleSelf(pos) * 57.29577951
    if angle >= -90.0 then
    	if angle <= 90.0 then
        	dir = kDirection.kRight
        else
        	dir = kDirection.kLeft
        end
    else
        dir = kDirection.kLeft
    end
    return dir
end

function MainScene:onExit()
end

return MainScene
