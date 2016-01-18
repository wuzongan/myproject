package com.kunlun.poker.recharge.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;

import com.kunlun.poker.recharge.service.FCGIService;
import com.kunlun.poker.recharge.system.RechargeProtocol;
import com.kunlun.poker.rmi.UserRService;
  
/**
 * 
 * @author zern
 *
 */
@Controller  
@RequestMapping("/RechargeService")  
public class RechargeController {  
    
    @Autowired
    private UserRService userRService;
    
    @Autowired
    private FCGIService fcgiService;
  
    @SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET)  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception{  
        try {
        	//获取充值回调的内容
	        String uid = ServletRequestUtils.getRequiredStringParameter(  
	                request, "uid");
	        String golds = ServletRequestUtils.getRequiredStringParameter(  
	                request, "golds");
	        String uname = ServletRequestUtils.getRequiredStringParameter(  
	        		request, "uname");
	        String amount = ServletRequestUtils.getRequiredStringParameter(  
    				request, "amount");
	        String blanace = ServletRequestUtils.getRequiredStringParameter(  
    				request, "blanace");
	        String orderid = ServletRequestUtils.getRequiredStringParameter(  
	        		request, "orderid");
	        String ext = ServletRequestUtils.getRequiredStringParameter(  
    				request, "ext");
	        String apiToken = ServletRequestUtils.getRequiredStringParameter(  
    				request, "api_token");
	      
	        response.setCharacterEncoding("UTF-8");
	        JSONObject jsonObj  = new JSONObject();
	        if(request.getRemoteAddr().equals(RechargeProtocol.TOKEN_IP)){
	        	int gameType = 0;
	        	//充值成功
	            jsonObj.put("retcode", 0);
	            jsonObj.put("retmsg", "success");
	            String jsonRet = jsonObj.toString();
	            response.encodeRedirectURL(jsonRet);
		        //获取玩家剩余金币
	            String balanceUrl = balanceRequest(RechargeProtocol.PID, RechargeProtocol.RID, 
	            		uid, RechargeProtocol.API_TOKEN);
		        String jsonBalancePost = getPostJson(RechargeProtocol.BALANCE_URL, balanceUrl);
		        String residueBalance = getRegionBalance(jsonBalancePost);
		        int finalGolds = canRecharge(residueBalance);
	            if(finalGolds != 0){
	            	//消耗玩家金币买道具\
	            	String consumerUrl = consumerRequest(RechargeProtocol.PID, RechargeProtocol.RID, 
	            			Integer.parseInt(uid), 0, 1, 1, finalGolds, finalGolds, "", RechargeProtocol.API_TOKEN);
	 		        String jsonConsumerPost = getPostJson(RechargeProtocol.CONSUMER_URL, consumerUrl);
	 		        int retCode = costGold(jsonConsumerPost);
	 		        if(isSuccessCost(retCode))
	 		        	userRService.buyChip(uid, finalGolds);
	            }
	        }else{
	            jsonObj.put("retcode", 1);
	            jsonObj.put("retmsg", "加金币失败");
	            String jsonRet = jsonObj.toString();
	            response.encodeRedirectURL(jsonRet);
	        } 
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}   
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	this.doGet(request, response);
    }
    
    

/*******************************************************
 * 发送post请求并返回内容的部分
 *******************************************************/  
    public String getPostJson(String urlString, String writeString){
    	String JsonString = null;
    	try {
    		//发送post请求给大区查询剩余金币数量
    		URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");
			out.write(writeString);
			out.flush();    
	        out.close();
	        //收取post返回的内容
		    String sCurrentLine;     
		    String sTotalString;     
		    sCurrentLine = "";     
		    sTotalString = "";     
		    InputStream l_urlStream;     
		    l_urlStream = connection.getInputStream();     
		    // 传说中的三层包装阿！     
		    BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));     
		    while ((sCurrentLine = l_reader.readLine()) != null) {     
		    	sTotalString += sCurrentLine;     
		    }
		    JsonString = sTotalString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonString;
    }
    
    
/*******************************************************
 * 查询用户余额的功能部分
 *******************************************************/  
    private String getRegionBalance(String jsonString){
    	JSONObject jsonObject = JSONObject.fromObject(jsonString);
    	String balance = (String)jsonObject.get("balance");
		return balance;
    }
    
    private int canRecharge(String residueBalance){
    	if(residueBalance != null){
    		int balance = Integer.parseInt(residueBalance);
    		return balance;
    	}else{
    		return 0;
    	}
    }
    
/*******************************************************
 * 花费金币购买道具的功能部分
 *******************************************************/
    
    private int costGold(String jsonString){
    	JSONObject jsonObject = JSONObject.fromObject(jsonString);
    	int retCode = (int)jsonObject.get("retcode");
    	return retCode;
    }
    
    private boolean isSuccessCost(int retCode){
    	if(retCode == 0)
    		return true;
    	return false;
    }
    
/*******************************************************
 * 拼接url字符串的内容
 *******************************************************/
    
    private String balanceRequest(int pid, int rid, String uid, String apiToken){
    	return "pid="+pid+"&rid="+rid+"&uid="+uid+"&api_token="+apiToken;
    }
    
    private String consumerRequest(int pid, int rid, int uid, int cid, int type, 
    		int goodsid, int coins, int nums, String ext, String apiToken){
    	return "pid="+pid+"&rid="+rid+"&uid="+uid+"&cid="+cid+"&type="+type+
    			"&goodsid="+goodsid+"&coins="+coins+"&nums="+nums+"&ext="+ext+"&api_token="+apiToken;
    }
    
}  
