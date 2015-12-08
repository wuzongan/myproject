-- 白 绿 蓝 紫 橙 红 金
HERO_QUALITY_COLOR_TABLE=
{
    {color = cc.c3b(112,112,112),img1 = "public_hui.png",img2 = "public_huikuang.png",card_img = "kapai_huise.png",deco_img = "renwu_huisetiao.png",name="白色"},
    {color = cc.c3b(0,255,0),img1 = "public_lv.png",img2 = "public_lvkuang.png",card_img = "kapai_lvse.png",deco_img = "renwu_lvsetiao.png",name="绿色"},
    {color = cc.c3b(0,0,255),img1 = "public_lan.png",img2 = "public_lankuang.png",card_img = "kapai_lanse.png",deco_img = "renwu_lansetiao.png",name="蓝色"},
    {color = cc.c3b(128,0,128),img1 = "public_zi.png",img2 = "public_zikuang.png",card_img = "kapai_zise.png",deco_img = "renwu_zisetiao.png",name="紫色"},
    {color = cc.c3b(251,168,0),img1 = "public_cheng.png",img2 = "public_chengkuang.png",card_img = "kapai_chengse.png",deco_img = "renwu_chengsetiao.png",name="橙色"},
    {color = cc.c3b(255,0,0),img1 = "public_hong.png",img2 = "public_hongkuang.png",card_img = "kapai_hongse.png",deco_img = "renwu_hongsetiao.png",name="红色"},
    {color = cc.c3b(98,98,0),img1 = "public_jin.png",img2 = "public_jinkuang.png",card_img = "kapai_jinse.png",deco_img = "renwu_jinsetiao.png",name="金色"},
}

CARD_SORT_TAB = {
    {sortName="默认",sortType="default"},
    {sortName="品质",sortType="quality"},
    {sortName="等级",sortType="lv"},
    {sortName="职业",sortType="profession"},
}

EQUIP_SORT_TAB = {
    {sortName="默认",sortType="default"},
    {sortName="品质",sortType="quality"},
    {sortName="等级",sortType="lv"},
    {sortName="类型",sortType="sort"},
}

HERO_QUALITY_BG_IMG=
{
    "yxlb_baidi.png",
    "yxlb_lvdi.png",
    "yxlb_landi.png",
    "yxlb_zidi.png",
    "yxlb_chengdi.png",
    "yxlb_hongdi.png",
    "yxlb_MAXdi.png",
}

LEADER_SKILL_TREE_TABLE = 
{
    {name = "雷霆",iconBg = "icon_bg_shandian.png"},
    {name = "烈焰",iconBg = "icon_bg_lieyan.png"},
    {name = "冰冻",iconBg = "icon_bg_hanbing.png"},
    {name = "野蛮",iconBg = "icon_bg_liren.png"},
    {name = "鼓舞",iconBg = "icon_bg_guwu.png"},
    {name = "黑暗",iconBg = "icon_bg_zuzhou.png"},
    {name = "机甲",iconBg = "icon_bg_fangyu.png"},
    {name = "治疗",iconBg = "icon_bg_zhiliao.png"},
    {name = "空",iconBg = "icon_bg_lieyan.png"},
}

EQUIP_TYPE_TABLE =
{
    weapon={1,"武器"},
    accessories={2,"饰品"},
    armor={3,"防具"},
    shoes={4,"鞋子"},
    debris={0,"杂物"},
}

TEAM_POS_OPEN_TAB = {open10 = {0,1,0,0,0,0,0,0,0},open11 = {0,1,0,0,0,0,1,0,0},open12 = {0,1,0,0,0,0,1,1,0},open13 = {0,1,0,0,0,0,1,1,1},
                        open20 = {0,1,1,0,0,0,0,0,0},open21 = {0,1,1,0,0,0,1,0,0},open22 = {0,1,1,0,0,0,1,1,0},open23 = {0,1,1,0,0,0,1,1,1},
                        open30 = {0,1,1,0,1,0,0,0,0},open31 = {0,1,1,0,1,0,1,0,0},open32 = {0,1,1,0,1,0,1,1,0},open33 = {0,1,1,0,1,0,1,1,1},
                        open40 = {0,1,1,0,1,1,0,0,0},open41 = {0,1,1,0,1,1,1,0,0},open42 = {0,1,1,0,1,1,1,1,0},open43 = {0,1,1,0,1,1,1,1,1},
                        open50 = {1,1,1,1,1,1,0,0,0},open51 = {1,1,1,1,1,1,1,0,0},open52 = {1,1,1,1,1,1,1,1,0},open53 = {1,1,1,1,1,1,1,1,1},
                        }
TEAM_LOCK_TIPS_TAB = {{lockImg = "dw_jiesuo_3.png",openLevel = 14},{lockImg = "dw_jiesuo_1.png",openLevel = 0},{lockImg = "dw_jiesuo_1.png",openLevel = 0},
                        {lockImg = "dw_jiesuo_3.png",openLevel = 14},{lockImg = "dw_jiesuo_1.png",openLevel = 5},{lockImg = "dw_jiesuo_2.png",openLevel = 10},
                        {lockImg = "dw_jiesuo_4.png",openLevel = 18},{lockImg = "dw_jiesuo_5.png",openLevel = 22},{lockImg = "dw_jiesuo_6.png",openLevel = 25}
                        }

-- 1：武器，2：饰品，3：防具，4：鞋子，0：杂物（不可装备）
EQUIP_TYPE_NAME_TABLE =
{
    type_1 = "武器",
    type_2 = "饰品",
    type_3 = "防具",
    type_4 = "鞋子",
    type_0="杂物"
}

EQUIP_ABILITY_TABLE = 
{
    {name = "物攻",icon = "public_icon_phsc.png",attrName = "patk",typeValue = 1},
    {name = "魔攻",icon = "public_icon_mgc.png",attrName = "matk",typeValue = 2},
    {name = "防御",icon = "public_icon_dfs.png",attrName = "def",typeValue = 3},
    {name = "速度",icon = "public_icon_speed.png",attrName = "speed",typeValue = 4},
    {name = "生命",icon = "public_icon_hp.png",attrName = "hp",typeValue = 5},
    {name = "生命",icon = "public_icon_hp.png",attrName = "hp2",typeValue = 5},
    {name = "生命",icon = "public_icon_hp.png",attrName = "hp3",typeValue = 5},
}

--复活技能
RELIFE_SKILL_NAME = 
{
    "dracula_s3",
}

public_config = {
    anim_scale = 0.8,
    anim_durition = 1/24,
    action_durition = 0.07,
    action_durition_temp = 0.07,
    action_rythm = 0.5,
    battleTickPause = false,
}

-- 排行榜
rank_type_name =
{
    rank_level = "rank_level",  -- 等级排行榜
    rank_combat = "rank_combat",  -- 战力排行榜
    rank_boss = "rank_boss",  -- boss战排行榜
    rank_activity = "active_top",  -- 活动排行榜
    rank_search = "city",  -- 探索排行榜
    rank_elite_level = "city",  -- 探索排行榜
    arena_top = "arena_top",  -- 竞技场排行榜
    active_top = "active_top", -- 生存大考验排行榜
    rank_guide = "association_guild_all", -- lian
}

anim_label_name_cfg = 
{
    dengchang = "dengchang",        -- 登场
    daiji = "daiji",
    qianjin = "qianjin",            -- 前进
    qianjin2 = "qianjin2",
    gongji1 = "gongji1",
    gongji2 = "gongji2",
    gongji3 = "gongji3",
    gongji4 = "gongji4",
    mofa = "mofa",
    beiji = "beiji1",
    siwang = "siwang",
    tibu = "tibu",                  -- 替补
    houtui = "houtui",              -- 后退
    qishoumofa = "qishoumofa",      -- 起手魔法
    beiji2 = "beiji2",
    shuaidao = "shuaidao",          -- 摔倒
    zhaohuan = "zhaohuan",          -- 召唤
    shengli = "shengli",            -- 胜利
    dafei1 = "dafei1",              -- 打飞
    dafei2 = "dafei2",    
}

http_request_method =
{
    GET = "GET",
    POST = "POST",
}

GLOBAL_TOUCH_PRIORITY = -130;

TYPE_FACE_TABLE = {
    Arial_BoldMT = "Arial-BoldMT";
}