package com.kunlun.poker.center.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.command.CallbackEvent;
import com.googlecode.canoe.core.command.annotation.CanoeCallback;
import com.googlecode.canoe.core.command.annotation.CanoeCommand;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.Config;
import com.kunlun.poker.center.domain.Robot;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.domain.config.AttainmentConfig;
import com.kunlun.poker.center.domain.rank.Rank;
import com.kunlun.poker.center.service.AttainmentService;
import com.kunlun.poker.center.service.NoticeService;
import com.kunlun.poker.center.service.RankService;
import com.kunlun.poker.center.service.RobotService;
import com.kunlun.poker.center.service.RoomService;
import com.kunlun.poker.center.service.UserService;
import com.kunlun.poker.center.system.ConfigData;
import com.kunlun.poker.center.system.GameProtocol;
import com.kunlun.poker.domain.Notice;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.util.DataUtil;

@Controller("userCtrl")
public class UserCtrl {
	private static final Logger logger = LoggerFactory
			.getLogger(UserCtrl.class);	
	@Autowired(required = true)
	private UserService userService;
	@Autowired(required = true)
	private RoomService roomService;
	@Autowired(required = true)
	private RankService rankService;
	@Autowired(required = true)
	private AttainmentService attainmentService;
	@Autowired(required =true)
	private RobotService robotService;
    @Autowired
    private Scheduler scheduler;
    @Autowired(required = true)
    private NoticeService noticeService;

	@CanoeCommand(value = GameProtocol.C_PLAYERINFO, roleRequired = true)
	public void onMyINFO(Request request, ResponseSender responseSender) {
		logger.debug("接收到客户端消息++++++" + GameProtocol.C_PLAYERINFO);
		Session session = request.getSession();
		//String portraitPrefix = (String)Config.getInstance().getProperty("picture.IP1");
		
		if (session != null) {
			User user = (User) session.getRole();
			Map<String, Object> data = new HashMap<String, Object>();
			int id = user.getId();
			data.put("id", id);
			data.put("name", user.getName());
			data.put("level", user.getLevel());
			data.put("biggestBankWin", user.getSingleWinBankroll());
			data.put("winCardNum", user.getWinCardNum());
			data.put("cardNum", user.getCardNum());
			data.put("bestHand", user.getBestHand());
			logger.debug("最佳手牌："+user.getBestHand());
			data.put("sex", user.getSex());
			String attainmentInfoCheck = this.attainmentService.getAttainmentStr(user.getId());
			if(attainmentInfoCheck == null)
				attainmentInfoCheck = "";
			data.put("attainmentInfo", attainmentInfoCheck);
			data.put("portrait", user.getPortrait());
			data.put("carry", user.getBankroll());
//			data.put("nextGetBankroolTime", attainmentService.getNextTimeOfGetBankroll(user));
			
			Response response = new Response(GameProtocol.S_PLAYERINFO, session);
			response.setScope(ResponseScope.SELF);
			response.setData(data);
			responseSender.send(response);
		}
	}

	@CanoeCommand(value = GameProtocol.C_ROOMINFO, roleRequired = true)
	public void onRoomInfo(Request request, ResponseSender responseSender) {
		logger.debug("接收到客户端消息++++++" + GameProtocol.C_ROOMINFO);
		Session session = request.getSession();
		List<Room> allRoom = roomService.getAll();
		Map<Integer, Integer> allRoomPlayNumsMap = userService
				.getAllRoomPlayerNums();
		if (!allRoomPlayNumsMap.isEmpty()) {
			for (Room room : allRoom) {
				Integer num = allRoomPlayNumsMap.get(room.getId());
				room.setPlayerNums(num == null ? 0 : num);
			}
		}

		@SuppressWarnings("unchecked")
		Map<String, Object>[] roomInfos = new Map[allRoom.size()];
		int i = 0;
		for (Room room : allRoom) {
			Map<String, Object> roomData = room
					.simplify();
			roomInfos[i] = roomData;
			i++;
		}
		Response response = new Response(GameProtocol.S_ROOMINFO, session);
		response.setScope(ResponseScope.SELF);
		response.setData(roomInfos);
		responseSender.send(response);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CanoeCommand(value = GameProtocol.C_RANKINFO, roleRequired = true)
	public void queryRankInfo(Request request, ResponseSender responseSender) {
		Map<String, Object> data = (Map<String, Object>) request.getData();
		int type = (int) data.get("rankType");
		Collection<? extends Rank<?, ?>> collection = rankService
				.getRanks(type);

		final int numberOfRanks = Math.min(collection.size(),
				RankService.MAX_SIZE);
		Map<String, Object>[] rankInfos = new HashMap[numberOfRanks];
		int idx = 0;
		for (Rank rank : collection) {
			if (idx >= numberOfRanks) {
				break;
			}

			User user = rank.getUser();
			Map<String, Object> rankInfo = new HashMap<String, Object>(4);
			rankInfo.put("id", user.getId());
			rankInfo.put("name", user.getName());
			rankInfo.put("portrait", user.getPortrait());
			rankInfo.put("value", rank.getValue());
			rankInfos[idx] = rankInfo;
			idx++;
		}

		Response response = new Response(GameProtocol.S_RANKINFO,
				request.getSession());
		response.setScope(ResponseScope.SELF);
		response.setData(rankInfos);
		responseSender.send(response);
	}
	
	 private Map<String, Object> getMap(int type, Rank<?, ?> rank){
	     if(rank==null){
	         return null;
	     }
         Map<String, Object> data = new HashMap<String, Object>(2);
         data.put("rankType", type);
         int id = rank.getUser().getId();
         Map<String, Object> rankInfoMap = new HashMap<String, Object>(4);
         rankInfoMap.put("id", id);
         rankInfoMap.put("name", rank.getUser().getName());
        
         String portraitPrefix = (String)Config.getInstance().get("picture.IP1");
         String upLoadPath = DataUtil.setUploadPath(portraitPrefix,
         DataUtil.firstFolderAddress(id),
         DataUtil.secondFolderAddress(id));
        
         rankInfoMap.put("portrait", upLoadPath + "/" + id +".png");
         rankInfoMap.put("value", rank.getValue());
         data.put("rankInfo", rankInfoMap);
         return data;
     }
	
	@SuppressWarnings("static-access")
    @CanoeCommand(value = GameProtocol.C_FIRST_RANK_INFO, roleRequired = true)
	public void queryFirstRankInfo(Request request, ResponseSender responseSender){
	     @SuppressWarnings("unchecked")
        Map<String, Object>[] datas = new HashMap[4];
	    
	    datas[0] = this.getMap(rankService.RANK_TYPE_WINRATE, rankService.getFirstRank(rankService.RANK_TYPE_WINRATE));
	    datas[1] = this.getMap(rankService.RANK_TYPE_LEVEL, rankService.getFirstRank(rankService.RANK_TYPE_LEVEL));
	    datas[2] = this.getMap(rankService.RANK_TYPE_SINGELWINBANKROLL, rankService.getFirstRank(rankService.RANK_TYPE_SINGELWINBANKROLL));
	    datas[3] = this.getMap(rankService.RANK_TYPE_BANKROLL, rankService.getFirstRank(rankService.RANK_TYPE_BANKROLL));
	    
	    Response response = new Response(GameProtocol.S_FIRST_RANK_INFO, request.getSession());
	    response.setScope(ResponseScope.SELF);
	    response.setData(datas);
	    responseSender.send(response);
	}
	
	/***
	 * 每隔4小时领取筹码
	 * @param request
	 * @param responseSender
	 */
	@CanoeCommand(value = GameProtocol.C_GET_BANKROLLONFOUR, roleRequired = true)
	public void getBankrollByFourHourInterval(Request request, ResponseSender responseSender){
	    User user = (User)request.getSession().getRole();
	    boolean err = attainmentService.trrigerGetbankrollAtt(user);
	    Map<String, Object> data = new HashMap<String, Object>(3);
	    
	    boolean isFirstLogin = user.isFirstLoginOfEveryDay();
	    if(isFirstLogin){
            user.setFirstLoginOfEveryDay(false);
        }
	    
	    AttainmentConfig config = attainmentService.getAttainmentConfig(AttainmentService.ATT_FOURINTERVARGETREWARD);
	    
	    int bankroll =  ConfigData.getPrizeConfig(config.getPrizeId()).getBankroll();
	    if(err){
	        int nextTime = attainmentService.getNextTimeOfGetBankroll();
	        data.put("nextGetBankroolTime", nextTime);
	        data.put("err", 1);
	        data.put("bankroll", bankroll);
	        
	    }else{
            data.put("nextGetBankroolTime", 0);
            data.put("err", 0);
            data.put("bankroll", bankroll);
	    }
        Response response = new Response(GameProtocol.S_GET_BANKROLLONFOUR,
                request.getSession());
        response.setScope(ResponseScope.SELF);
        response.setData(data);
        responseSender.send(response);
	}
	
    @SuppressWarnings("unchecked")
    @CanoeCommand(value = GameProtocol.C_OPERATE_NOTITY, roleRequired = true)
	public void logOperateNotify(Request request, ResponseSender responseSender){
       User user = (User)request.getSession().getRole();
       int userId = user.getId();
       Map<String, Object> data = (Map<String, Object>) request.getData();
       int opKey = (int) data.get("opKey");
       switch (opKey) {
       case 1://快速开始
           //废弃
        break;
       case 2://邀请好友次数
           LogClient.logInviteFriend(userId);
           //this.attainmentService.trrigerInviterFriendAtt(user);
           break;
       case 3://分享fb次数
           LogClient.logShareFbCount(userId);
           break;
       default:
        break;
       }
	}

    @CanoeCallback(value = CallbackEvent.ONDISCONNECTED)
    public void onDisconnect(Session session, ResponseSender responseSender) {
        User user = (User)session.getRole();
        if(user != null){
        	session.setRole(null);
            this.userService.saveUser(user);
            this.attainmentService.exit(user);
            this.noticeService.exit(user);
            //LogClient.sendLogMessage(LogClient.buildTlUserOnlineLogMessage(user.getId()));
        }
    }
    
    @SuppressWarnings("unchecked")
    @CanoeCommand(value = GameProtocol.C_QUERY_NOTICE_LIST, roleRequired = true)
    public void queryNoticeList(Request request, ResponseSender responseSender){
        User user = (User)request.getSession().getRole();
        List<Notice> noticeList = this.noticeService.getNotices();
        Map<String, Object>[] datas = new HashMap[noticeList.size()];
        int index=0;
        for(Notice notice : noticeList){
            int noticeId = notice.getId();
            if(this.noticeService.whetheDel(user, noticeId)){
                continue;
            }
            boolean flag = this.noticeService.whetheRead(user, noticeId);
            byte status = (byte) (flag?1:0);
            
            Map<String, Object> data = new HashMap<String, Object>(5);
            data.put("noticeId", notice.getId());
            data.put("time", notice.getCreateTime());
            data.put("title", notice.getTitle());
            data.put("content", notice.getContent());
            data.put("status", status);
            datas[index] = data;
            
            index++;
        }
        
        Response response = new Response(GameProtocol.S_QUERY_NOTICE_LIST,
                request.getSession());
        response.setScope(ResponseScope.SELF);
        response.setData(datas);
        responseSender.send(response);
    }
    
    @SuppressWarnings("unchecked")
    @CanoeCommand(value = GameProtocol.C_QUERY_NOTICE_CONTENT, roleRequired = true)
    public void queryNoticeContent(Request request, ResponseSender responseSender){
        User user = (User)request.getSession().getRole();
        Map<String, Object> requestData = (Map<String, Object>) request.getData();
        int noticeId = (int) requestData.get("noticeId");
        boolean flag = this.noticeService.queryNoticeContent(user, noticeId);
        byte err = (byte) (flag?1:0);
        
        Map<String, Object> data = new HashMap<String, Object>(2);
        data.put("noticeId", noticeId);
        data.put("err", err);
        
        Response response = new Response(GameProtocol.S_QUERY_NOTICE_CONTENT,
                request.getSession());
        response.setScope(ResponseScope.SELF);
        response.setData(data);
        responseSender.send(response);
    }
    
    @SuppressWarnings("unchecked")
    @CanoeCommand(value = GameProtocol.C_DEL_NOTICE, roleRequired = true)
    public void delNotice(Request request, ResponseSender responseSender){
        User user = (User)request.getSession().getRole();
        Map<String, Object> requestData = (Map<String, Object>) request.getData();
        int noticeId = (int) requestData.get("noticeId");
        boolean flag = this.noticeService.delNotice(user, noticeId);
        byte err = (byte) (flag?1:0);
        
        Map<String, Object> data = new HashMap<String, Object>(2);
        data.put("noticeId", noticeId);
        data.put("err", err);
        
        Response response = new Response(GameProtocol.S_DEL_NOTICE,
                request.getSession());
        response.setScope(ResponseScope.SELF);
        response.setData(data);
        responseSender.send(response);
        
    }  
    
	@CanoeCommand(value = GameProtocol.C_OTHER_PLAYER_INFO, roleRequired = true)
	public void onOtherPlayerInfo(Request request, ResponseSender responseSender) {
		int id = (int)request.getData();
		logger.debug("接收到客户端消息++++++" + GameProtocol.C_OTHER_PLAYER_INFO);
		Session session = request.getSession();
//		String portraitPrefix = (String)Config.getInstance().get("picture.IP1");
		if (session != null) {
			Map<String, Object> data = new HashMap<String, Object>();
			User user = userService.getUserById(id, false);
			if(user != null){
				data.put("id", user.getId());
				data.put("name", user.getName());
				data.put("level", user.getLevel());
				data.put("biggestBankWin", user.getSingleWinBankroll());
				data.put("winCardNum", user.getWinCardNum());
				data.put("cardNum", user.getCardNum());
				data.put("bestHand", user.getBestHand());
				logger.debug("最佳手牌："+user.getBestHand());
				data.put("sex", user.getSex());
				data.put("attainmentInfo", this.attainmentService.getAttainmentStr(user.getId()));
				data.put("portrait", user.getPortrait());
				data.put("carry", user.getBankroll());
//				data.put("nextGetBankroolTime", attainmentService.getNextTimeOfGetBankroll(user));
				Response response = new Response(GameProtocol.S_OTHER_PLAYER_INFO, session);
				response.setScope(ResponseScope.SELF);
				response.setData(data);
				responseSender.send(response);
			}else{
				Robot robot = robotService.getRobot(id);
				if(robot != null){
					data.clear();
					data.put("id", robot.getId());
					data.put("name", robot.getName());
					data.put("level", robot.getLevel());
					data.put("biggestBankWin", robot.getSingleWinBankroll());
					data.put("winCardNum", robot.getWinCardNum());
					data.put("cardNum", robot.getCardNum());
					data.put("bestHand", robot.getBestHand());
					data.put("sex", robot.getSex());
					data.put("attainmentInfo","");
					data.put("portrait", robot.getPortrait());
					//生成机器人金钱
					robot.setBankroll();
					data.put("carry", robot.getBankroll());
					Response response = new Response(GameProtocol.S_OTHER_PLAYER_INFO, session);
					response.setScope(ResponseScope.SELF);
					response.setData(data);
					responseSender.send(response);
				}
			}
		}
	}
	
    @CanoeCommand(value = GameProtocol.C_ALTER_PLAYERINFO, roleRequired = true)
    public void on(Request request, ResponseSender responseSender) {
        logger.debug("接收到客户端消息++++++" + GameProtocol.C_ALTER_PLAYERINFO);
        Session session = request.getSession();
        User user = (User)request.getSession().getRole();
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) request.getData();
		String name = (String) data.get("name");
		int sex = (int)data.get("sex");
		if(user != null){
			user.setName(name);
			if(sex == 1 && sex== 0)
			user.setName(name);
			user.setSex(sex);
			userService.saveUser(user);
		}
        Response response = new Response(GameProtocol.S_ALTER_PLAYERINFO, session);
		response.setScope(ResponseScope.SELF);
		response.setData(data);
		responseSender.send(response);
    }
}
