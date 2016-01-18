package com.kunlun.poker.game.util;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseSender;
import com.kunlun.poker.game.system.GameProtocol;
import com.kunlun.poker.util.DataUtil;

public class ResponseUtil {
    
    public static void error(ResponseSender responseSender, int errorCode)
    {
        Response response = new Response(GameProtocol.S_ERROR);
	
        response.setData(DataUtil.createMap(new Object[]{
		"errorCode", errorCode,
        }));
	
        responseSender.send(response);
    }
	public static void error(ResponseSender responseSender, int errorCode, Object errorData)
	{
		Response response = new Response(GameProtocol.S_ERROR);
		
		response.setData(DataUtil.createMap(new Object[]{
			"errorCode", errorCode,
			"errorData", errorData
		}));
		
		responseSender.send(response);
	}
}
