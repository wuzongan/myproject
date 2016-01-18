package com.kunlun.poker.http;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kunlun.poker.util.HTTPUtil;

public abstract class HttpService {
    
    /**
     * 登录校验,http方式校验klsso
     * @param klsso
     * @return
     */
    public static String loginKlssoVerify(String klsso){
        try {
            String jsonString = HTTPUtil.readFromURL(DistrictProtocol.North_America + klsso);
            JSONParser parser = new JSONParser();
            @SuppressWarnings("unchecked")
            Map<String,Object> jsonMap = (Map<String,Object>)parser.parse(jsonString);
            JSONObject jsonObject = new JSONObject(jsonMap);
            Object retcodeObject = jsonObject.get("retcode");
            long retcode = (long)retcodeObject;
            
            if(retcode == 0){
                Object dataObject = jsonObject.get("data");
                @SuppressWarnings("unchecked")
                Map<String,Object> dataMap = (Map<String,Object>)dataObject;
                String klUid = (String)dataMap.get("uid");
                return klUid;
            }else if(retcode == 500){
                return "false";
            }else if(retcode == 501){
                return "false";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return "false";
    }
    
}
