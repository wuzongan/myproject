package com.kunlun.poker.center.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.center.data.ConfigWrapper;
import com.kunlun.poker.center.domain.config.AttainmentConfig;
import com.kunlun.poker.center.domain.config.LevelConfig;
import com.kunlun.poker.center.domain.config.PrizeConfig;
import com.kunlun.poker.center.service.ConfigDataService;
import com.kunlun.poker.center.system.ConfigData;

@Service("configDataService")
public class ConfigDataServiceImpl implements ConfigDataService {

    @Autowired
    private ConfigWrapper configWrapper;
    

        
    @Override
    public void loadAllConfig() {
        List<LevelConfig> levelConfigList = configWrapper.selectLevelConfigs();
        for(LevelConfig data : levelConfigList){
            ConfigData.getLevelconfigmap().put(data.getLevel(), data);
        }
        
        List<PrizeConfig> prizeConfigList = configWrapper.selectPrizeConfigs();
        for(PrizeConfig data : prizeConfigList){
            ConfigData.getPrizeconfigmap().put(data.getId(), data);
        }
        
        List<AttainmentConfig> attainmentConfigList = configWrapper.selectAttainmentConfigs();
        for(AttainmentConfig data : attainmentConfigList){
            ConfigData.getAttainmentconfigmap().put(data.getId(), data);
        }
    }
}
