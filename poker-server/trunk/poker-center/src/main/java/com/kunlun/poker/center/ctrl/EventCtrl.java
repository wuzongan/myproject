package com.kunlun.poker.center.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionEvent;
import com.googlecode.canoe.event.Event;
import com.googlecode.canoe.event.EventUtil;
import com.googlecode.canoe.event.anno.EventListener;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.domain.UserEvent;
import com.kunlun.poker.center.service.AttainmentService;
import com.kunlun.poker.center.system.ConfigData;
import com.kunlun.poker.center.system.GameProtocol;
import com.kunlun.poker.center.system.MessageDataUtil;
import com.kunlun.poker.domain.AttrEnum;
import com.kunlun.poker.server.CommonProtocol;

@Controller("eventCtrl")
public class EventCtrl {
	private static final Logger logger = LoggerFactory
			.getLogger(EventCtrl.class);
	
	private static final int TIPS_BUY_CHIPS = 1;
	
	public EventCtrl()
	{
		EventUtil.gatherListeners(this);
		logger.debug("创建eventCtrl");
	}
	
	@Autowired
	private ResponseSender responseSender;
	@Autowired
	private AttainmentService attainmentService;

	@EventListener(event = UserEvent.ATTR_CHANGED)
	public void onUserInfo(Event<User> event) {
	    User user = event.getTarget();
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", user.getId());
        data.put("name", user.getName());
        
        data.put("level", user.getLevel());
        data.put("biggestBankWin", user.getSingleWinBankroll());
        data.put("winCardNum", user.getWinCardNum());
        data.put("cardNum", user.getCardNum());
        data.put("bestHand", user.getBestHand());
        data.put("sex", user.getSex());
        data.put("attainmentInfo", this.attainmentService.getAttainmentStr(user.getId()));
        
        data.put("portrait",  user.getPortrait());
        data.put("carry", user.getBankroll());
//        data.put("nextGetBankroolTime", attainmentService.getNextTimeOfGetBankroll(user));
        Response response = new Response(GameProtocol.S_PLAYERINFO);
        response.setScope(ResponseScope.SPECIFIED);
        List<User> users = new ArrayList<>();
        users.add(user);
        response.setRecievers(users);
        response.setData(data);
        responseSender.send(response);
	}
	
	@SuppressWarnings("unchecked")
    @EventListener(event = UserEvent.LEVEL_CHANGED)
    public void onLevelUp(Event<User> event) {
        User user = event.getTarget();
        
        Map<String, Object>[] datas = new HashMap[4];
        datas[0]=MessageDataUtil.buildAttrData(AttrEnum.LEVEL.getType(), user.getLevel());
        datas[1]=MessageDataUtil.buildAttrData(AttrEnum.CURRENT_EXP.getType(), user.getExp());
        int maxExp=0;
        for(int idx=1;idx<=user.getLevel();idx++){
            maxExp += ConfigData.getLevelConfig(idx).getExp();
        }
        datas[2]=MessageDataUtil.buildAttrData(AttrEnum.MAX_EXP.getType(), maxExp);
        datas[3]=MessageDataUtil.buildAttrData(AttrEnum.BANKROLL.getType(), user.getBankroll());
        
        Response response = new Response(GameProtocol.S_ATTRIBUTE_UPDATE_NOTIFY_INT);
        response.setScope(ResponseScope.SPECIFIED);
        List<User> users = new ArrayList<>(1);
        users.add(user);
        response.setRecievers(users);
        response.setData(datas);
        responseSender.send(response);
    }
	
	@SuppressWarnings("unchecked")
    @EventListener(event = UserEvent.BANKROLL_CHANGED)
    public void onBankrollChange(Event<User> event) {
        User user = event.getTarget();
        
        Map<String, Object>[] datas = new HashMap[1];
        Map<String, Object> data =  new HashMap<String, Object>(2);
        data.put("type", AttrEnum.BANKROLL.getType() );
        data.put("value", user.getBankroll());
        datas[0]=data;
        
        Response response = new Response(GameProtocol.S_ATTRIBUTE_UPDATE_NOTIFY_INT);
        response.setScope(ResponseScope.SPECIFIED);
        List<User> users = new ArrayList<>(1);
        users.add(user);
        response.setRecievers(users);
        response.setData(datas);
        responseSender.send(response);
	}
	
	@SuppressWarnings("unchecked")
	@EventListener(event = UserEvent.BUY_CHIPS)
	public void onBuyChips(Event<User> event){
		User user = event.getTarget();
		Map<String, Object> datas[] = new HashMap[1];
		Map<String, Object> data =  new HashMap<String, Object>(2);
		data.put("type", AttrEnum.BANKROLL.getType());
		data.put("value", user.getBankroll());
		datas[0]=data;
		
		Response response = new Response(GameProtocol.S_ATTRIBUTE_UPDATE_NOTIFY_INT);
		response.setScope(ResponseScope.SPECIFIED);
        List<User> users = new ArrayList<>(1);
        users.add(user);
        response.setRecievers(users);
        response.setData(datas);
        responseSender.send(response);
	}
	
	@EventListener(event = UserEvent.TOOL_TIP)
	public void onToolTip(Event<User> event){
		UserEvent userEvent = (UserEvent) event;
		int chips = userEvent.getChips();
		Map<String, Object> data =  new HashMap<String, Object>();
		data.put("title", TIPS_BUY_CHIPS);
		data.put("value", chips);
		
		Response response = new Response(GameProtocol.S_TOOLTIP);
		response.setScope(ResponseScope.SELF);
        response.setData(data);
        responseSender.send(response);
	}
	
	@EventListener(event = SessionEvent.ROLE_CHANGED)
	public void onRoleChanged(Event<Session> e) {
		Session session = e.getTarget();
		if(session.getRole() == null)
		{
			Response response = new Response(CommonProtocol.DISCONNECT, session);
			response.setScope(ResponseScope.SELF);
	        responseSender.send(response);
		}
	}
}
