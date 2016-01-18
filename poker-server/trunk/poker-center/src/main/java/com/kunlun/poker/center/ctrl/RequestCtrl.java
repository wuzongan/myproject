package com.kunlun.poker.center.ctrl;

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
import com.kunlun.poker.center.domain.Item;
import com.kunlun.poker.center.service.ItemService;
import com.kunlun.poker.center.system.GameProtocol;

@Controller("requestCtrl")
public class RequestCtrl {
    private static final Logger logger = LoggerFactory
            .getLogger(UserCtrl.class);
    @Autowired(required = true)
    private ItemService itemService;
    
    @CanoeCommand(value = GameProtocol.C_SHOPINFO, roleRequired = true)
    public void onShopInfo(Request request, ResponseSender responseSender) {
        logger.debug("接收到客户端消息++++++" + GameProtocol.C_SHOPINFO);

        Session session = request.getSession();
        List<Item> itemInfo = itemService.getAllItem();
        List<Map<String, Object>> goodInfo = new ArrayList<Map<String, Object>>();
        Map<String, Object> shopInfo = new HashMap<String, Object>();
        shopInfo.put("orderinfo", "poker_shop_items");
        Response response = new Response(GameProtocol.S_SHOPINFO, session);
        response.setScope(ResponseScope.SELF);
        
        if(!itemInfo.isEmpty()){
            for(Item item : itemInfo){
                Map<String, Object> goodMap = new HashMap<String, Object>();
                goodMap.put("gpid", item.getGpid());
                goodMap.put("doller", item.getDoller());
                goodMap.put("cdoller", item.getcDoller());
                goodMap.put("hdoller", item.gethDoller());
                goodMap.put("klcoin", item.getKlDcoin());
                goodMap.put("chips", item.getChips());
                goodMap.put("rebate", item.getRebate());
                goodMap.put("rebateRate", item.getRebatesRate());
                goodInfo.add(goodMap);
            }
            shopInfo.put("shop", goodInfo);
            response.setData(shopInfo);
        }else{
            response.setData("");
        }   
        responseSender.send(response); 
    }
}
