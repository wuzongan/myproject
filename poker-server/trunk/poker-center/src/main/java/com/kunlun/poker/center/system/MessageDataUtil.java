package com.kunlun.poker.center.system;

import java.util.HashMap;
import java.util.Map;

public class MessageDataUtil {

    public static Map<String, Object> buildAttrData(int type, long value){
        Map<String, Object> data =  new HashMap<String, Object>(2);
        data.put("type", type );
        data.put("value", value);
        return data;
    }
    
    
    
}
