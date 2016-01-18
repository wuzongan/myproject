package com.kunlun.poker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.kunlun.poker.domain.SettingItem;

public class SettingService {
    public static final int ID_FIRST_LOGIN_FREE_CHIPS = 1;

    private static final SettingService instance = new SettingService();

    public static SettingService getInstance() {
        return instance;
    }

    public Map<Integer, Object> settingMap;

    private SettingService() {

    }

/*****************************************************************************************/
/**								获取配置文件中的配置信息											**/
/*****************************************************************************************/
    public int getFirstLoginChips()
    {
		return (int)getSetting(ID_FIRST_LOGIN_FREE_CHIPS);
    	
    }
    
    public int getDealerChips(int id)
    {
    	return (int)getSetting(id);
    }
    
    private Object getSetting(int id)
    {
        loadSettings();
        
        return settingMap.get(id);
    }
/*****************************************************************************************/

    private synchronized void loadSettings() {
        if (settingMap == null) {
            settingMap = new HashMap<Integer, Object>();

            List<SettingItem> settingItems = null;
            SqlSessionFactory ssf = SqlSessionFactoryHolder
                    .getSqlSessionFactory();
            if (ssf != null) {
                try (SqlSession ss = ssf.openSession()) {
                    settingItems = ss.selectList("selectSettings");
                }
            }

            for (SettingItem item : settingItems) {
                settingMap.put(item.getId(),
                        item.getType().parse(item.getValue()));
            }
        }
    }
}
