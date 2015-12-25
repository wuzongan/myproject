
--------------------------------------  config data start --------------------------------------
game_data = 
{
    map = "map",
    map_title_detail = "map_title_detail",
    map_random_event = "map_random_event",
    map_main_story = "map_main_story",
    map_fight = "map_fight",
    enemy_detail = "enemy_detail",
    cityid_cityorderid = "cityid_cityorderid",
    formation = "formation",
    leader_skill = "leader_skill",
    character_detail = "character_detail",
    skill_detail = "skill_detail",
    leader_skill_tree = "leader_skill_tree",
    leader_skill_develop = "leader_skill_develop",
    character_base = "character_base",
    character_base_rate = "character_base_rate",
    character_train = "character_train",
    school_train_time_config = "school_train_time_config",
    school_train_type_config = "school_train_type_config",
    building_base_school = "building_base_school",
    building_mine = "building_mine",
    character_strengthen = "character_strengthen",
    equip = "equip",
    equip_evolution = "equip_evolution",
    equip_max_strongthen = "equip_max_strongthen",
    equip_strongthen = "equip_strongthen",
    building_base_harbor = "building_base_harbor",
    building_base_laboratory = "building_base_laboratory",
    building_factory = "building_factory",
    building_harbor = "building_harbor",
    building_hospital = "building_hospital",
    building_laboratory = "building_laboratory",
    building_school = "building_school",
    enemy_mine = "enemy_mine",
    food_mine = "food_mine",
    metal_mine = "metal_mine",
    middle_resource = "middle_resource",
    role = "role",
    skill_learn = "skill_learn",
    skill_levelup = "skill_levelup",
    gacha = "gacha",
    middle_map = "middle_map",
    middle_mine = "middle_mine",
    middle_building_mine = "middle_building_mine",
    middle_boss = "middle_boss",
    guild = "guild",
    guild_client = "guild_client",
    guild_shop = "guild_shop",
    daily_award = "daily_award",
    daily_award_loop = "daily_award_loop",--循环签到
    guild_funtion = "guild_funtion",--公会建筑
    guild_tech = "guild_tech",--公会科技
    drama = "drama",
    item = "item",
    role_detail = "role_detail",
    role_skill = "role_skill",
    chapter = "chapter",
    resource_detail = "resource_detail",
    building = "building",
    arena_shop = "arena_shop",
    arena_award = "arena_award",
    shop = "shop",
    vipguide = "vip_guide",  -- 消费计划
    guide = "guide",
    loading = "loading",
    loadingtips = "loadingtips",
    charge = "charge",
    reward_daily = "reward_daily",
    reward_once = "reward_once",
    code = "code",
    exchange = "exchange",
    combat_base = "combat_base",
    combat_skill = "combat_skill",
    active_chapter = "active_chapter",
    active_detail = "active_detail",
    guide_team = "guide_team",
    race = "race",
    random_last_name = "random_last_name",
    random_first_name = "random_first_name",
    occupation = "occupation",
    guide_button_open = "guide_button_open",
    guide_button_open_new = "guide_button_open_new",
    button_open = "button_open",
    character_exchange = "character_exchange",
    dirt_shop = "dirt_shop",
    error_cfg = "error",
    active_cfg = "active",
    world_boss = "world_boss",
    suit = "suit",
    world_boss_reward = "world_boss_reward",
    vip = "vip",
    chain = "chain",
    wanted = "wanted",
    month_award = "month_award",
    dailyscore = "dailyscore",
    active_fight_forever = "active_fight_forever",
    character_train_position = "character_train_position",
    pay = "pay",
    vip_shop = "vip_shop",
    online_award = "online_award",
    evolution = "evolution",
    evolution_3 = "evolution_3",
    evolution_4 = "evolution_4",
    evolution_5 = "evolution_5",
    character_book = "character_book",
    arena_award_milestone = "arena_award_milestone",
    character_train_rate = "character_train_rate",
    character_train_time = "character_train_time",
    reward_diary = "reward_diary",
    diaryscore = "diaryscore",
    commander_recipe = "commander_recipe",
    commander_type = "commander_type",
    opening = "opening",
    assistant = "assistant",
    adver = "adver",
    level_gift = "level_gift",
    equip_exchange = "equip_exchange",
    equip_st = "equip_st",
    month_award_coin = "month_award_coin",--充值签到
    commander_level = "commander_level",
    resoucequality = "resoucequality",
    request_code = "request_code",
    whats_inside = "whats_inside",
    equip_book = "equip_book",
    limit_hero_rank = "limit_hero_rank", -- gacha_gift = "gacha_gift",
    Integration_shop = "integration_shop",
    ranking = "ranking",
    -- inreview = "inreview",
    notice_active = "notice_active",
    integration_world = "integration_world",
    auto_sweep = "auto_sweep",
    guild_boss = "guild_boss",--公会boss
    week_award = "week_award",
    character_break = "character_break",
    break_control = "break_control",
    omni_exchange = "omni_exchange",
    treasure = "treasure",--罗杰的宝藏
    item_recipe = "item_recipe",
    item_recipe_show = "item_recipe_show",
    map_treasure_detail_battle = "map_treasure_detail_battle",--宝藏NPC战斗配置
    fight_treasure = "fight_treasure",--宝藏战斗配置
    player_boss = "player_boss",
    roulette = "roulette",--转盘
    roulette_rank_reward = "roulette_rank_reward",--转盘
    roulette_reward = "roulette_reward",--转盘
    book_character = "book_character", -- 卡牌图鉴
    book_equip = "book_equip", -- 装备图鉴
    tree_shop = "tree_shop",
    active_recharge = "active_recharge",--充值活动
    active_show = "active_show",
    super_all = "super_all",
    super_rich = "super_rich",
    face_icon = "face_icon",
    hero_chapter = "hero_chapter",
    star_reward = "star_reward",
    bandit = "bandit",
}

game_config_data = {}

function getConfigByName(configName, isTEAFlag)
    local codeBuffer = cc.HelperFunc:getFileData(configName)
    local config_json = nil
    if codeBuffer then
        if isTEAFlag then
            local key = "tywx"
            local resultBuffer = crypto.decryptXXTEA(codeBuffer, key)
            if resultBuffer then
                config_json = util_json:new(resultBuffer)   
            else
                config_json = util_json:new(codeBuffer)
            end
        else
            config_json = util_json:new(codeBuffer)
        end
    else
        cclog("getConfigByName can not get file data of %s", configName)
    end
    return config_json
end

--[[--
    获得配置
    @return util_json 数据
]]
function getConfig(configFileName)
    if configFileName ~= nil or type(configFileName) == "string" then
        if game_config_data[configFileName] == nil then
            local config_json = getConfigByName("data/" .. configFileName .. ".config",false)
            if config_json then
                game_config_data[configFileName] = config_json;
                -- cclog("return configFileName ==== " .. tostring(configFileName) .. ".config")
                return game_config_data[configFileName];
            else
                local config_json = getConfigByName("data/" .. configFileName .. ".configtea",true)
                if config_json then
                    game_config_data[configFileName] = config_json;
                    -- cclog("return configFileName ==== " .. tostring(configFileName) .. ".configtea")
                    return game_config_data[configFileName];
                end
            end
        else
            -- cclog("return --------------------" .. configFileName);
            return game_config_data[configFileName];
        end
    end
    -- cclog("configFileName ===is nill == " .. tostring(configFileName))
    return nil;
end

function deleteConfigData(configFileName)
    if configFileName then
        local tempCfg = game_config_data[configFileName]
        if tempCfg then
            tempCfg:delete();
            game_config_data[configFileName] = nil;
        end
    else
        for k,v in pairs(game_config_data) do
            v:delete();
        end
        game_config_data = {};
    end
end
--------------------------------------  config data end --------------------------------------
