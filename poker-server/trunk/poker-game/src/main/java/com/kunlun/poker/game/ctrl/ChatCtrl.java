package com.kunlun.poker.game.ctrl;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.command.annotation.CanoeCommand;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.domain.Seat;
import com.kunlun.poker.game.system.GameProtocol;
import com.kunlun.poker.rmi.UserRService;

/**
 *
 * @author kl
 */

@Controller
public class ChatCtrl {
	private static final Logger logger = LoggerFactory
			.getLogger(ChatCtrl.class);
	@Resource(name = "userRService")
	private UserRService userRService;

	@CanoeCommand(value = GameProtocol.C_CHAT, roleRequired = true)
	public void onChat(Request request, ResponseSender responseSender) {
		logger.debug("接收到客户端聊天消息++++++++++++++++++" + GameProtocol.C_CHAT);
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		Desk desk = player.getDesk();
		Seat seat = desk.getSeatByPlayer(player);
		if (seat != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) request.getData();
			String content = (String) data.get("content");
			// Todo content输入规范检测
			data.put("seat", seat.getId());
			data.put("content", content);

			Response response = new Response(GameProtocol.S_CHAT);
			Collection<Player> players = desk.getPlayers();
			response.setScope(ResponseScope.SPECIFIED);
			response.setRecievers(players);
			response.setData(data);
			responseSender.send(response);
		} else {
			return;
		}
	}

}
