--更新热更前加载的文件

local files = {
    "game_version_config",
    "init",
    "platform_config",
    "game_config",
    "game_url_config",
    "string_config",
    "game_util",
    "game_scene",
    "skill_effect/battle_effect_offset",
    "game_data",
    "controller/game_guide_controller",
    "game_sound",
    "game_button_open",
    "game_data_statistics"
}
local f_util = CCFileUtils:sharedFileUtils()
local l_path =  f_util:getWritablePath() .. "lua/"
for k,v in pairs(files) do
    local file = l_path..v..".lua"
    if f_util:isFileExist(file) then
        package.loaded[v] = nil   
        loadfile(file) 
        file_data = dofile(file) 
        if v == "game_url_config" then
            game_url = file_data
        elseif v == "string_config" then
            string_config = file_data
        elseif v == "game_util" then
            game_util = file_data
        elseif v == "game_scene" then
            game_scene = file_data
        elseif v == "skill_effect/battle_effect_offset" then
            battle_effect_offset = file_data
        elseif v == "game_data" then
            game_data = file_data
        elseif v == "controller/game_guide_controller" then
            game_guide_controller = file_data
        elseif v == "game_sound" then
            game_sound = file_data
        elseif v == "game_button_open" then
            game_button_open = file_data
        elseif v == "game_data_statistics" then
            game_data_statistics = file_data
        end
    end
end