local LayerManager = class("LayerManager")



function LayerManager:ctor(m_scene)

	self.mapLayer = self:createLayer()
    self.roleLayer = nil --精灵层直接加到地图层上
    self.mainUILayer = self:createLayer()
    self.uiLayer = self:createLayer()
    self.viewLayer = self:createLayer()
    self.guideLayer = self:createLayer()
    self.loadLayer = self:createLayer()
    self.waitLayer = self:createLayer()
    self.tipsLayer = self:createLayer()
    self.exitLayer = self:createLayer()

    local scene = m_scene or cc.Director:getInstance():getRunningScene()
    if scene then
        scene:addChild(self.mapLayer)
        scene:addChild(self.mainUILayer)
        scene:addChild(self.uiLayer)
        scene:addChild(self.viewLayer)
        scene:addChild(self.guideLayer)
        scene:addChild(self.loadLayer)
        scene:addChild(self.waitLayer)
        scene:addChild(self.tipsLayer)
        scene:addChild(self.exitLayer)
    end
end

function LayerManager:addUI(node, zorder, tag)
	self.uiLayer:addChild(node, zorder, tag)
end

function LayerManager:createLayer()
    local layer = display.newLayer()
    layer:setTouchSwallowEnabled(false)
    return layer
end


return LayerManager