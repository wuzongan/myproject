package com.kunlun.poker.center.data;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.center.domain.config.AttainmentConfig;
import com.kunlun.poker.center.domain.config.LevelConfig;
import com.kunlun.poker.center.domain.config.PrizeConfig;

@Repository("configWrapper")
public interface ConfigWrapper {

    @Select("select * from config_levelExp")
    List<LevelConfig> selectLevelConfigs();
    
    @Select("select * from config_prize")
    List<PrizeConfig> selectPrizeConfigs();
    
    @Select("select * from config_attainment")
    List<AttainmentConfig> selectAttainmentConfigs();
}
