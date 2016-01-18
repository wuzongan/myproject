package com.kunlun.poker.center.system;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.domain.config.AttainmentConfig;
import com.kunlun.poker.center.domain.config.LevelConfig;
import com.kunlun.poker.center.domain.config.PrizeConfig;

public class ConfigData {
    private static final Logger logger = LoggerFactory
            .getLogger(ConfigData.class);

    private static final Map<Integer,LevelConfig> levelConfigMap = new HashMap<Integer, LevelConfig>();
    private static final Map<Integer,PrizeConfig> prizeConfigMap = new HashMap<Integer, PrizeConfig>();
    private static final Map<Integer,AttainmentConfig> attainmentConfigMap = new HashMap<Integer,AttainmentConfig>();
    
    public static Map<Integer, LevelConfig> getLevelconfigmap() {
        return levelConfigMap;
    }

    public static Map<Integer, PrizeConfig> getPrizeconfigmap() {
        return prizeConfigMap;
    }

    public static Map<Integer, AttainmentConfig> getAttainmentconfigmap() {
        return attainmentConfigMap;
    }
    
    public static LevelConfig getLevelConfig(int id){
        return levelConfigMap.get(id);
    }
    
    public static PrizeConfig getPrizeConfig(int id){
        return prizeConfigMap.get(id);
    }
    
    public static AttainmentConfig getAttainmentConfig(int id){
        return attainmentConfigMap.get(id);
    }
    
    public static Collection<AttainmentConfig> getAllAttainmentConfig(){
        return attainmentConfigMap.values();
    }
    
    public static void givePrize(final User user, int prizeId){
        PrizeConfig config = prizeConfigMap.get(prizeId);
        if(config==null){
            logger.warn("prizeId:"+prizeId+" is not exist");
            return;
        }
        
        synchronized (user) {
            if(config.getBankroll() > 0){
                user.setBankroll(user.getBankroll() + config.getBankroll());
            }
            if(config.getExp() > 0){
                user.setExp(user.getExp() + config.getExp());
                //user.dispatchEvent(new UserEvent(UserEvent.BANKROLL_CHANGED));
            }
            
        }
    }

}
