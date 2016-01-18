package com.kunlun.poker.login.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.command.annotation.CanoeCommand;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.kunlun.poker.Config;
import com.kunlun.poker.http.HttpService;
import com.kunlun.poker.login.domain.User;
import com.kunlun.poker.login.service.UserService;
import com.kunlun.poker.login.system.GameProtocol;
import com.kunlun.poker.rmi.UserRService;
import com.kunlun.poker.service.SettingService;
import com.kunlun.poker.util.DataUtil;

@Controller
public class RequestCtrl {
	private static final Logger logger = LoggerFactory.getLogger(RequestCtrl.class);
    
	@Autowired
	private UserRService userRService;
	@Autowired
	private UserService userService;
	
    @CanoeCommand(value = GameProtocol.C_LOGIN, roleRequired = false)
	public void onLogin(Request request, ResponseSender responseSender) {
        logger.debug("接收到客户端消息++++++" + GameProtocol.C_LOGIN);
        Session session = request.getSession();
        @SuppressWarnings("unchecked")
        Map<String, String> data = (Map<String, String>) request.getData();
        Map<String, Object> responseData = new HashMap<String, Object>();
        String klsso = data.get("klsso");
        String uid = HttpService.loginKlssoVerify(klsso);
        if(uid != "false"){
            User user = userService.selectUserByUid(uid);
            if(user == null){
                //此处需要配置配置表来初始化
                user = new User();
                String name = data.get("name");
                //获得登陆的固定id为1
                int firstFreeChips = SettingService.getInstance().getFirstLoginChips();
                user.setBankRoll(firstFreeChips);
                user.setName("Guest"+ name.substring(0, 5));
                user.setUid(uid);
                user.setFb(1);
                userService.addUser(user);
            }else{
                user = userService.selectUserByUid(uid);
            }
            List<Integer> scope = new ArrayList<Integer>();
            int serverNum = Integer.parseInt(Config.getInstance().getProperty("recharge.num1"));
            scope.add(serverNum);
            responseData.put("result", DataUtil.booleanToInt(true));
            responseData.put("scope", scope);
            Response response = new Response(GameProtocol.S_LOGIN, session);
            response.setData(responseData);
            response.setScope(ResponseScope.SELF);
            userRService.login((int) session.getId(), user.getId());
            responseSender.send(response);
        }else{
            Response response = new Response(GameProtocol.S_LOGIN, session);
            response.setData(DataUtil.booleanToInt(false));
            response.setScope(ResponseScope.SELF);
            responseSender.send(response);
        }
	}
    /**
     * 测试用
     * @param request
     * @param responseSender
     */
    @CanoeCommand(value = GameProtocol.C_GUEST_LOGIN, roleRequired = false)
    public void onGuestLogin(Request request, ResponseSender responseSender) {
        logger.debug("接收到客户端消息++++++" + GameProtocol.C_GUEST_LOGIN);
        Session session = request.getSession();
        int data = (int)request.getData();
    
        //查询是否游客已存在
        User user = userService.selectUserById(data);
        if(user == null){
        	
        }else{
        	if(isLegal(session)){
	        	if(true){
	                Response response = new Response(GameProtocol.S_LOGIN, session);
	                response.setScope(ResponseScope.SELF);
	                userRService.login((int) session.getId(), user.getId());
	                responseSender.send(response);
	        	}
        	}
        }
    }
    
    private boolean isLegal(Session session){
    	Config config = Config.getInstance();
    	String ipLimit = (String)config.get("ipLimit");
    	System.out.println(ipLimit);
    	
    	//获取ip地址
    	String address = session.getRemoteAddress().toString();
    	String ipPorts[] = address.split("/");
    	String ipPort = ipPorts[1];
    	String ip = ipPort.split(":")[0];
    	
    	if(ipLimit != null){
    		String ips[] =  ipLimit.split(",");
    		for(String singleIp : ips){
    	    	if(singleIp.equals(ip)){
    	    		return true;
    	    	}
    		}
    	}else{
    		return true;
    	}
    	return false;
    }
}