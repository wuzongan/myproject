--- 游戏文字配置
--
local string_config = {
    m_name_err = "名字已被使用",      --姓名已被使用
    m_btn_cancel = "取消", 
    m_btn_sure = "确定", 
    m_btn_think = "我再想想",
    m_btn_agree = "同意",
    m_btn_reject = "拒绝",
    m_btn_use = "使用",
    m_btn_open = "开启",
    m_title_prompt = "提示",
    m_btn_close = "关闭",
    m_operating_success = "操作成功!",
    m_sel_hero = "请选择英雄!",
    m_hero_comp_success = "英雄合成成功!",
    m_hero_comp_alert_msg = "英雄在出战中，不能做为材料使用!", 
    m_equip_decom_success = "装备分解成功!",
    m_equip_decom_alert_msg = "装备使用中,不能分解!",
    m_hero_evo_success = "英雄进化成功!",
    m_hero_evo_alert_msg = "此英雄不能进化!",
    m_hero_decom_alert_msg = "英雄出战中,不能分解!",
    m_hero_decom_success = "英雄分解成功!",
    m_hero_sell_success = "英雄贩卖成功!",
    m_hero_sell_alert_msg = "英雄在出战中，不能出售!",
    m_e_cooling_alert_msg = "装备强化正在冷却中,不能强化!",
    m_e_level_alert_msg = "等级不够,不能强化!",
    m_e_evo_success = "装备强化成功!",
    m_net_req_failed = "网络请求失败,请重新再试!",
    m_net_no_data = "无网络数据!",
    m_data_error = "当前网络不稳定，无法连接到服务器，请稍后再试~",
    m_hero_played_in = "出战中",
    m_equip_use = "使用中",
    m_equip_merge_alert_msg = "材料不足不能合成!",
    m_equip_merge_success = "装备合成成功!",
    m_career_success = "转职成功",
    m_career_buyitems_success = "购买转职道具成功",
    m_equip_decomposition_auto = "是否确定自动分解1,2星的装备?",
    m_again_http_request = "重新请求",
    m_clear_cooling_time_success = "清除冷却时间成功!",
    m_change_ratio_equip_success = "装备强化成功率改变!",
    m_notselect_partsid = "请选择需要强化的零件",
    m_energy_stone = "能量块",
    m_gold_card_item = "金卡",
    m_silver_card_item = "银卡",
    reward = "奖励",

    m_input_role_name = "输入角色名称",
    m_re_input = "重新输入",
    m_no_input_name = "对不起，请您输入您的名字！",
    m_random = "随机",
    m_firts_opening_story = "公元2055年，平行宇宙发现的30年后\n力量宇宙的发现带来极其恐怖的病毒——灰雾\n灰雾导致大量人类变为丧尸，社会秩序一片混乱\n为了寻找解决问题的办法，各个宇宙都派出超级英雄进入\n力量宇宙，发现病毒背后的秘密，争夺更多的治疗资源\n新的大冒险篇章由此开启……",
    m_re_donwload = "重新下载",
    m_download_failed = "数据下载失败",
    m_re_conn = "重新连接",
    m_download_cfg = "下载配置",
    m_download_res = "加载资源",
    m_fight_cost_tips = "共%d场战斗，每场战斗消耗%d",
    m_no_msg = "无",
    m_map_title_battle_over = "地图已经打完",
    m_re_battle_lock_msg = "10级解锁",
    m_explore = "探索",
    m_re_explore = "扫荡",
    m_action_point_tips = "收复当前建筑一共要进行%d场战斗，总计消耗%d点体力，您当前的体力不足以完整收复这个建筑，确定要继续吗？",
    m_map_title_step = "完成%d/%d",
    m_warning = "警告",
    m_add_training_card = "添加训练伙伴",
    m_no_open = "暂未开启",
    m_open_condition = "达到%d级开启",
    m_stop_training = "停止训练",
    m_stop_training_tips = "停止训练只能获得部分经验，不返还多余部分所消耗的金钱。",
    m_speed_training = "立即完成",
    m_speed_training_tips = "立刻完成此次训练将瞬间获得所有经验，但会消耗 %d钻石，确定吗？",
    m_speed_training_free = "今日剩余免费加速次数剩余: %d次，确定使用吗？",
    m_free_times = "今日免费加速次数: %d",
    m_retreat_warning = "撤退后需要重新在扫荡，确定撤退吗？",
    m_activity_retreat_warning = "撤退后需要重新在战斗，确定撤退吗？",
    m_auto_battle_finish = "扫荡完成!",
    m_action_point_not_enough = "体力不足!",
    m_sel_strengthen_equip = "请选择需要强化的装备！",
    m_auto_battle_failed = "扫荡战斗失败！",
    m_equip_lv_is_max = "装备达到最大等级！",
    m_combat_is_not_enough = "扫荡需要%d战斗力",
    m_low_probability = "低概率",
    m_rebattle_count = "次数：%d/%d",
    m_normal_chapter = "选择章节",
    m_difficult_chapter = "精英关卡",
    m_error_tips_title = "错误提示",
    m_lock_str = "锁定",
    m_unlock_str = "解锁",
    m_metal_not_enough = "金属不够，需要%d金属",
    m_food_not_enough = "物资不够，需要%d物资",
    m_crystal_not_enough = "能量块不够，需要%d能量块",
    m_lv_is_max = "达到最大等级！",
    m_master_is_max = "主战伙伴数量达到上限！",
    m_substitute_is_max = "替补伙伴数量达到上限！",
    m_onclick_team_pos = "请点击位置选择阵型中伙伴！",
    m_equip_pos_no_open = "装备位置未开启！",
    m_master_at_least_one = "至少有一名主战伙伴！",
    m_all_equip_auto_success = "一键装备成功!",

    m_vip_1 = "",

    m_team_pos_no_open = "编队位置未开启",

    m_add_arena_time = "确定购买竞技场战斗次数？",
    m_open_position = "您确定使用%d钻石开启新的训练位？",
    m_exit_info = "注销后将退出，请重新打开游戏",
    m_session_invalid = "登录出问题了，请重新打开游戏",

    world_boss_push_text = "世界Boss讨伐战马上就开始了！速度推倒赢取丰厚奖励！",
    action_point_push_text = "体力已经满了，你的英雄已经迫不及待了！",
    live_push_text = "开饭的时间又到了，保暖才能思进取啊！",
    tomorow_push_text = "今日签到领赵云哟~神级暴力AOE输出，还不快来爽一下？",
    active_live = "极限挑战又开始了，诸葛亮大神的碎片不容错过",

    m_open_text = "时空裂痕、英雄复兴、复制体横行、混乱战争，\n杀戮与征伐的存在，这是一个弱小人类只能龟缩在\n高度科技化的城堡里苟延残喘的世界，\n所有这一切似乎都与一台叫做星零的超级电脑有关。\n大唐帝国皇帝唐龙，回想起自己以一介小兵的\n身份崛起于混沌的宇宙中，\n与星零一起统一宇宙的经历，不禁想问，\n星零为何突然叛变消失，外面的世界\n为何变的如此混乱不堪，这是神的\n恶作剧还是考验?为了回归正常秩序，\n终究要走出这个封闭的城堡，与外面的\n世界共同战斗。SK23训练\n营正式启动，挑选了一众能力\n出众的精英（玩家）派遣到外面\n的时空区域，寻找星零的踪迹。\n在训练营开幕式上，大唐帝国\n皇帝唐龙期望着学员们再创小兵\n的辉煌传奇。",

    --[[
        排序：进阶过的，等级大于10的，蓝色以上的
        1-3     出售卡牌
        4-6     技能升级
        7-8     伙伴进阶    只考虑进阶了和等级大于10的
        9-10    伙伴分解    只考虑进阶了和等级大于10的
    ]]
    m_seecial_tips_1 = "%s（LV.%d）是一名进阶至+%d的高级伙伴，在伙伴分解中兑换红宝石，您确定继续出售么？",
    m_seecial_tips_2 = "%s（LV.%d）是一名高等级的伙伴，在出售之前，您在伙伴传承中将他的经验传承给其他伙伴可以减少一些损失。",
    m_seecial_tips_3 = "%s（LV.%d）是一名%s色品质的卡牌，在伙伴分解中兑换红宝石或作为伙伴进阶材料都很有价值，您确定继续出售么？",

    m_seecial_tips_4 = "%s（LV.%d）是一名进阶至+%d的高级伙伴，作为升级材料时效果与未进阶伙伴相同，但是您可以获得额外的蓝宝石和红宝石。您确定使用么？",
    m_seecial_tips_5 = "%s（LV.%d）是一名高等级的伙伴，在技能升级之前，您在伙伴传承中将他的经验传承给其他伙伴可以减少一些损失。",
    m_seecial_tips_6 = "%s（LV.%d）是一名%s色品质的卡牌，在伙伴分解中兑换红宝石或作为伙伴进阶材料都很有价值，您确定继续作为升级材料么？",

    m_seecial_tips_7 = "%s（LV.%d）是一名进阶至+%d的高级伙伴，进阶材料时效果与未进阶伙伴相同，但是您可以获得额外的蓝宝石和红宝石。您确定使用么？",
    m_seecial_tips_8 = "%s（LV.%d）是一名高等级的伙伴，在伙伴进阶之前，您在伙伴传承中将他的经验传承给其他伙伴可以减少一些损失。",

    m_seecial_tips_9 = "%s（LV.%d）是一名进阶至+%d的高级伙伴，能为您提供非常多的战斗力，确定要继续分解吗？",
    m_seecial_tips_10 = "%s（LV.%d）是一名高等级的伙伴，在分解之前，您在伙伴传承中将他的经验传承给其他伙伴可以减少一些损失。",
    m_remove_apprentice = "确定要踢掉这个徒弟吗？",

    pirate_enter_tip = "\t\t探宝开始后无法修改阵容\n\t\t战斗结束后血量不会恢复",
    pirate_buff_tip = "增加BUFF成功",
    pirate_box_tip = "请选择你的宝物！",
    pirate_text_1 = "效果",
    pirate_text_2 = "概率获得",
    pirate_text_increase = "增加",
    pirate_hp = "%的血量",
    pirate_patk = "%的物理攻击",
    pirate_matk = "%的魔法攻击",
    pirate_def = "%的防御",
    pirate_speed = "%的速度",

    topplayer_info1 = "经过一番苦战，WINER战胜了LOSER",
    topplayer_info2 = "LOSER被WINER打得落花流水",
    topplayer_info3 = "WINER一记黑虎掏心，LOSER不敌而走",
    topplayer_info4 = "WINER大笑三声，一个回旋踢干翻了LOSER",
    topplayer_info5 = "LOSER大叫着“我会回来的！”飞走了",

    m_topplayer_unnowname = "神秘玩家",
    m_topplayer_noguild = "无",


    friend_recive_add_friedn_tips = "PLAYER向您发送好友申请",
    friend_send_add_friedn_tips = "成功向PLAYER发送好友申请",
    friend_send_hadadd_friedn_tips = "您已经向PLAYER发送过好友申请了",
    friend_beAdd_friend = "PLAYER已经通过了你的好友申请，你们现在已经是好友了，赶快私密他吧？",
    friend_hadbeen_friedn_tips = "添加PLAYER成为好友成功",


    guild_hadjoined__tips = "您已经加入了GUILD公会",
    guild_send_invite_tips = "成功向PLAYER发送公会邀请",

    -- friend_send_add_friedn_tips = "成功向PLAYER发送好友申请",
    -- friend_send_add_friedn_tips = "成功向PLAYER发送好友申请",
    game_ability_commander_snatch_001 = "您已经开启了保护功能，发起抢夺后保护功能将失效，继续抢夺么？",
    game_lucky_turntable_001 = "剩余:%d次",
    game_lucky_turntable_002 = "%d钻石",
    game_lucky_turntable_003 = "刷新成功！",
    game_lucky_turntable_004 = "剩余:%d",
    game_challange_active1 = "敬请期待",
    game_challange_active2 = "守护要塞8月末开启",

    m_forceGuide_10 = "你已经解锁了伙伴训练，是否要需要引导？",
    m_forceGuide_13 = "你已经解锁了英雄技能，是否要需要引导？",
    m_forceGuide_14 = "你已经解锁了装备进阶，是否要需要引导？",
    m_forceGuide_15 = "你已经解锁了属性改造，是否要需要引导？",
    m_forceGuide_17 = "你已经解锁了精英关卡，是否要需要引导？",
    m_forceGuide_18 = "你已经解锁了碎片抢夺，是否要需要引导？",

    m_role_story_soldier = "唐龙，大唐帝国缔造者，为了追寻大蛇星系入侵与星零失控之谜而独自踏上征程。",
    m_role_story_lolita = "尤娜，参与过漫兰星压制行动，主见与胆识均有过人之处，对超级电脑星零导致世界混乱一事，预感幕后必有原因。为查明真相，只身出现在地球上。",
    m_role_story_robot = "唐虎，大蛇星系入侵引起的一系列动荡，正是其建立势力报复人类的最好时机！",
    m_role_story_zombie = "小黑猫，控制情报终端瓦解了数次反唐同盟的阴谋。得知星零失控之后，为了制止帝国的即将发生的动荡，果断采取行动。",
    m_role_story_dracula = "唐星，星零拥有肉身后，所分化出来的另一AI。为了查出令星零失控的数据源来历，只身向着大蛇星系进发。",
}

function string_config.getTextByKey(self,key)
    return self[tostring(key)] or "";
end

function string_config.getTextByKeyAndReplaceOne(self,key, rkey, rvalue)
    local text = self[tostring(key)] or ""
    if  type(rkey) == "string" and type(rvalue) =="string" then
        text = string.gsub(text, rkey, rvalue)
    end
    return text 
end

function string_config.getTextByKeyAndReplace(self,key, ...)
    local args = {...}
    if #args <=2 then return self:getTextByKeyAndReplaceOne(key,args[1], args[2]) end
    local text = self[tostring(key)] or ""
    local patt = nil
    local info = {}
    for i=1, #args - 1, 2 do
        if  type(args[i]) == "string" and type(args[i + 1]) == "string" then
            if not patt then patt = "[" else patt = patt .. "," end
            info[args[i]] = args[i+1]
            patt = patt .. args[i]
        end
    end
    patt = patt and patt .. "]+" 
    if patt then
        text = string.gsub(text, patt, function ( key )
            return info[key] or key
        end)
    end
    return text
end

table.tostring = function(t)  
    local mark={}  
    local assign={}  
    local function ser_table(tbl,parent)  
        mark[tbl]=parent  
        local tmp={}  
        for k,v in pairs(tbl) do  
            local key = type(k)=="number" and "["..k.."]" or "[".. string.format("%q", k) .."]"  
            if type(v)=="table" then  
                local dotkey= parent.. key  
                if mark[v] then  
                    table.insert(assign,dotkey.."='"..mark[v] .."'")  
                else  
                    table.insert(tmp, key.."="..ser_table(v,dotkey))  
                end  
            elseif type(v) == "string" then  
                table.insert(tmp, key.."=".. string.format('%q', v))  
            elseif type(v) == "number" or type(v) == "boolean" then  
                table.insert(tmp, key.."=".. tostring(v))  
            end  
        end  
        return "{"..table.concat(tmp,",").."}"  
    end  
    return ser_table(t,"ret")..table.concat(assign," ")
end 

return string_config;