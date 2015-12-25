local game_pop_box = class("game_pop_box")

function game_pop_box:ctor()
end

function game_pop_box:show(str)
	device.showAlert(str)
end

function game_pop_box:close()
	device.cancelAlert()
end

return game_pop_box