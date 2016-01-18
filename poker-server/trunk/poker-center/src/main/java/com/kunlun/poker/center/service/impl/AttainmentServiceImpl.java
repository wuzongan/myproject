package com.kunlun.poker.center.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.center.data.AttainmentWrapper;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.domain.UserAttainment;
import com.kunlun.poker.center.domain.config.AttainmentConfig;
import com.kunlun.poker.center.service.AttainmentService;
import com.kunlun.poker.center.service.RoomService;
import com.kunlun.poker.center.system.ConfigData;
import com.kunlun.poker.center.system.GameProtocol;
import com.kunlun.poker.center.system.MessageDataUtil;
import com.kunlun.poker.domain.AttrEnum;
import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.GameType;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.util.DateUtil;

@Service("attainmentService")
public class AttainmentServiceImpl implements AttainmentService {
    private static final Logger logger = LoggerFactory
            .getLogger(AttainmentServiceImpl.class);

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private RoomService roomService;
    @Autowired
    private AttainmentWrapper attainmentWrapper;
    @Autowired
    private ResponseSender responseSender;

    private AttainmentConfig loginAttConfig;
    private AttainmentConfig getBankrollConfig;
    private AttainmentConfig inviteFriendConfig;

    private final Map<Integer, Set<Integer>> attainmentIdsOfAllUser = new ConcurrentHashMap<Integer, Set<Integer>>();

    public AtomicBoolean flag = new AtomicBoolean(false);

    private void saveAttainment(final int attainmentId, int prizeId, User user,
            boolean canSaveInDb) {
        try {
            ConfigData.givePrize(user, prizeId);
            Set<Integer> attainmentIds = attainmentIdsOfAllUser.get(user
                    .getId());
            if (flag.compareAndSet(false, true)) {
                if (attainmentIds == null) {
                    attainmentIds = new ConcurrentHashSet<Integer>();
                    attainmentIdsOfAllUser.put(user.getId(), attainmentIds);
                }
                attainmentIds.add(attainmentId);
                flag.set(false);
            }
            if (canSaveInDb) {
                // SchedulerUtil .submitPersistenceTask(() ->
                // attainmentWrapper.insertAttainment(userId, attainmentId),
                // scheduler, user.getId());
            }
            // 派发成就事件
            // ConfigData.getAttainmentConfig(attainmentId).dispatchEvent(new
            // AttainmentEvent(AttainmentEvent.ATTAINMENT_NOTIFY));
            this.notifyAttainment(attainmentId, user);
            this.logRecordAttainment(attainmentId, user);
            this.attNotify(attainmentId, user);
        } catch (Throwable e) {
            logger.error(String.format(
                    "成就数据异常(可能未成功登录)userName=%s,attainmentIdsOfAllUser,get()",
                    user.getName()), attainmentIdsOfAllUser.get(user.getId()),
                    e);
        }
    }

    private void logRecordAttainment(int attainmentId, User user) {
        AttainmentConfig config = ConfigData.getAttainmentConfig(attainmentId);
        int type = config.getType();
        int bankRoll = ConfigData.getPrizeConfig(config.getPrizeId())
                .getBankroll();

        if (type == 1) {
            LogClient.logLoginFreeChips(user.getId(), bankRoll);
        } else {
            LogClient.logAchievementFreeChips(user.getId(), bankRoll);
        }

    }

    private void notifyAttainment(int attainmentId, User user) {
        AttainmentConfig config = ConfigData.getAttainmentConfig(attainmentId);
        Map<String, Object> data = new HashMap<String, Object>(1);
//        data.put("type", config.getType());
//        data.put("description", config.getDescription());
//        PrizeConfig prizeConfig = ConfigData
//                .getPrizeConfig(config.getPrizeId());
//        data.put("exp", prizeConfig.getExp());
//        data.put("bankroll", prizeConfig.getBankroll());
        data.put("attaintmentId", config.getId());
        Response response = new Response(GameProtocol.S_ATTAINTMENT_NOTIFY);
        response.setScope(ResponseScope.SPECIFIED);
        List<User> users = new ArrayList<>(1);
        users.add(user);
        response.setRecievers(users);
        response.setData(data);
        responseSender.send(response);
    }

    private boolean isExists(int userId, int attainmentId) {
        boolean isExists = false;
        Set<Integer> attIds = attainmentIdsOfAllUser.get(userId);
        if (attIds == null) {
            return isExists;
        }
        if (attIds.contains(attainmentId)) {
            isExists = true;
        }
        return isExists;
    }

    @Override
    public void trrigerlogin(User user, long lastLoginTime) {
        UserAttainment userAttainment = attainmentWrapper
                .selectUserAttainment(user.getId());
        Set<Integer> attainmentIdSet = attainmentIdsOfAllUser.get(user.getId());
        if (attainmentIdSet == null) {
            attainmentIdSet = new ConcurrentHashSet<Integer>();
            attainmentIdsOfAllUser.put(user.getId(), attainmentIdSet);
        }
        if (userAttainment != null) {
            String attainmentIdStr = userAttainment.getAttainmentIds();
            String[] strArr = attainmentIdStr.split(",");
            for (String str : strArr) {
                attainmentIdSet.add(Integer.valueOf(str));
            }
            // 第二天登陆，清理每天重复成就任务
            if (TimeUnit.MILLISECONDS.toDays(user.getLastLoginTime()
                    - lastLoginTime) >= 1) {
                this.clearRepeatAttainment(user);
            }
        }
        this.trrigerLoginAtt(user);

        // 每隔4小时领取通知
        try {
            AttainmentConfig config=null;
            if(user.isFirstLoginOfEveryDay()){
                config = this.getLoginAttConfig();
            }else{
                config = this
                        .getAttainmentConfig(AttainmentService.ATT_FOURINTERVARGETREWARD);
            }
            int bankroll = ConfigData.getPrizeConfig(config.getPrizeId())
                    .getBankroll();
            Map<String, Object> data = new HashMap<String, Object>(3);
            int nextTime = this.getNextTimeOfGetBankroll(user);
            data.put("nextGetBankroolTime", nextTime);
            data.put("err", 1);
            data.put("bankroll", bankroll);
            Response response = new Response(GameProtocol.S_GET_BANKROLLONFOUR);
            response.setScope(ResponseScope.SPECIFIED);
            List<User> users = new ArrayList<>(1);
            users.add(user);
            response.setRecievers(users);
            response.setData(data);
            responseSender.send(response);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void trrigerLoginAtt(User user) {
        if ((loginAttConfig = this.getLoginAttConfig()) == null) {
            return;
        }
        //int prizeId = loginAttConfig.getPrizeId();
        int attainmentId = loginAttConfig.getId();
        if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()
                - user.getLastLoginTime()) >= 1) {
            //this.saveAttainment(attainmentId, prizeId, user, false);
            user.setFirstLoginOfEveryDay(true);
        } else {
            if (!this.isExists(user.getId(), attainmentId)) {
                //this.saveAttainment(attainmentId, prizeId, user, false);
                user.setFirstLoginOfEveryDay(true);
            }
        }
    }

    private void clearRepeatAttainment(User user) {
        Set<Integer> attainmentIdSet = attainmentIdsOfAllUser.get(user.getId());
        if (!attainmentIdSet.isEmpty()) {
            for (Iterator<Integer> itr = attainmentIdSet.iterator(); itr
                    .hasNext();) {
                AttainmentConfig config = ConfigData.getAttainmentConfig(itr
                        .next());
                int type = config.getType();
                if (type == ATT_EVEREYDAYLOGIN
                        || type == ATT_FOURINTERVARGETREWARD
                        || type == ATT_PLAYAGAME || type == ATT_WINAGAME) {
                    itr.remove();
                }
            }
        }
    }

    private void validateAttainmentExpireTime(User user) {
        boolean change = false;
        if (!DateUtil.isSameDay(user.getLastLoginTime(),
                System.currentTimeMillis())) {
            change = true;
        }
        if (change) {
            this.clearRepeatAttainment(user);
        }
    }

    @Override
    public void trrigerGameAtt(User user, int roomId, boolean isWin) {
        /** 判断当前时间跟上次登录时间不在同一天，需要清理重复成就任务 */
        this.validateAttainmentExpireTime(user);

        for (AttainmentConfig config : ConfigData.getAllAttainmentConfig()) {
            if (this.isExists(user.getId(), config.getId())) {
                continue;
            }
            int configRoomId;
            String condition = config.getCondition();
            int prizeId = config.getPrizeId();
            switch (config.getType()) {
            case ATT_PLAYAGAME:
                configRoomId = Integer.valueOf(condition);
                if (roomId == configRoomId) {
                    this.saveAttainment(config.getId(), prizeId, user, false);
                    logger.debug("玩家：" + user.getName() + "  达成"
                            + config.getId() + " 成就");
                }
                break;
            case ATT_PLAYGAME:
                int playNum = Integer.valueOf(condition);
                // FIXME 跟房间无关
                if (playNum < user.getCardNum()) {
                    this.saveAttainment(config.getId(), prizeId, user, true);
                    logger.debug("玩家：" + user.getName() + "  达成"
                            + config.getId() + " 成就");
                }
                break;
            case ATT_WINAGAME:
                configRoomId = Integer.valueOf(condition);
                if (isWin && roomId == configRoomId) {
                    this.saveAttainment(config.getId(), prizeId, user, false);
                    logger.debug("玩家：" + user.getName() + "  达成"
                            + config.getId() + " 成就");
                }
            default:
                break;
            }
        }

    }

    private AttainmentConfig getLoginAttConfig() {
        if (loginAttConfig == null) {
            for (AttainmentConfig config : ConfigData.getAllAttainmentConfig()) {
                if (config.getType() == ATT_EVEREYDAYLOGIN) {
                    loginAttConfig = config;
                }
            }
        }
        return loginAttConfig;
    }

    private AttainmentConfig getGetBankrollConfig() {
        if (getBankrollConfig == null) {
            for (AttainmentConfig config : ConfigData.getAllAttainmentConfig()) {
                if (config.getType() == ATT_FOURINTERVARGETREWARD) {
                    getBankrollConfig = config;
                    break;
                }
            }
        }
        return getBankrollConfig;
    }

    private AttainmentConfig getInviteFriendConfig() {
        if (inviteFriendConfig == null) {
            for (AttainmentConfig config : ConfigData.getAllAttainmentConfig()) {
                if (config.getType() == ATT_INVITEFBFIREND) {
                    inviteFriendConfig = config;
                    break;
                }
            }
        }
        return inviteFriendConfig;
    }

    @Override
    public boolean trrigerGetbankrollAtt(User user) {
        /** 判断当前时间跟上次登录时间不在同一天，需要清理重复成就任务 */
        this.validateAttainmentExpireTime(user);

        if ((getBankrollConfig = getGetBankrollConfig()) == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        int timeNum = Integer.valueOf(getBankrollConfig.getCondition());
        int interval = (int) TimeUnit.MILLISECONDS.toSeconds(now
                - user.getLastGetBankrollTime());
        logger.debug("AttainmentServiceImpl.trrigerGetbankrollAtt. 剩余间隔时间 ="
                + interval);
        if (interval >= timeNum) {
            user.setLastGetBankrollTime(now);
            if(user.isFirstLoginOfEveryDay()){
                this.saveAttainment(getLoginAttConfig().getId(),
                        loginAttConfig.getPrizeId(), user, false);
            }else{
                this.saveAttainment(getBankrollConfig.getId(),
                        getBankrollConfig.getPrizeId(), user, false);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getNextTimeOfGetBankroll() {
        return Integer.valueOf(this.getGetBankrollConfig().getCondition());
    }

    @Override
    public int getNextTimeOfGetBankroll(User user) {
        int nextTime = this.getNextTimeOfGetBankroll();
        long lastGetBankrollTime = user.getLastGetBankrollTime();
        if (lastGetBankrollTime > 0) {
            nextTime = (int) (nextTime - TimeUnit.MILLISECONDS.toSeconds(System
                    .currentTimeMillis() - lastGetBankrollTime));
            if (nextTime < 0) {
                return 0;
            }
            return nextTime;
        }
        return 0;
    }

    private String strToSet(Set<Integer> set) {
        StringBuilder sb = new StringBuilder();
        int size = set.size();
        int index = 0;
        for (int aid : set) {
            sb.append(aid);
            if (index != size - 1) {
                sb.append(",");
            }
            index++;
        }
        return sb.toString();
    }

    @Override
    public void exit(User user) {
        Set<Integer> set = this.attainmentIdsOfAllUser.remove(user.getId());
        if (set != null && !set.isEmpty()) {
            String sb = this.strToSet(set);

            boolean flag = this.attainmentWrapper.updateAttainment(
                    user.getId(), sb);
            if (!flag) {
                this.attainmentWrapper.insertAttainment(user.getId(), sb);
            }
        }

    }

    @Override
    public void stop() {
        try {
            if (!this.attainmentIdsOfAllUser.isEmpty()) {
                for (Map.Entry<Integer, Set<Integer>> entry : this.attainmentIdsOfAllUser
                        .entrySet()) {
                    int userId = entry.getKey();
                    Set<Integer> aidSet = entry.getValue();
                    if (!aidSet.isEmpty()) {
                        String sb = this.strToSet(aidSet);
                        boolean flag = this.attainmentWrapper.updateAttainment(
                                userId, sb);
                        if (!flag) {
                            this.attainmentWrapper.insertAttainment(userId, sb);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String getAttainmentStr(int userId) {
        Set<Integer> set = this.attainmentIdsOfAllUser.get(userId);
        if (set != null && !set.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int size = set.size();
            int index = 0;
            for (int id : set) {
                AttainmentConfig att = ConfigData.getAttainmentConfig(id);
                int type = att.getType();
                if (type == ATT_PLAYAGAME || type == ATT_WINAGAME) {
                    Room room = this.roomService.getRoomById(Integer
                            .valueOf(att.getCondition()));
                    sb.append(type).append("_").append(room.getName());
                    if (index != size - 1) {
                        sb.append(",");
                    }
                } else if (type == ATT_PLAYGAME || type == ATT_POKERHAND) {
                    sb.append(type).append("_").append(att.getCondition());
                    if (index != size - 1) {
                        sb.append(",");
                    }

                }
                index++;
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    public void trrigerPokerHandAtt(User user, int roomId, PokerHand pokerHand) {
        try {
            if (pokerHand == null) {
                return;
            }
            Card[] bestCardArray = user.getBestCardArray();
            Room room = this.roomService.getRoomById(roomId);
            GameType gameType = room.getGameType();
            if(gameType == GameType.ROYAL){
                logger.debug("皇家玩法不参与 牌型解锁");
                return;
            }
            logger.debug(user.getName()+"触发牌型成就"+pokerHand.toString()+",index="+pokerHand.ordinal());
            
            /** 判断当前时间跟上次登录时间不在同一天，需要清理重复成就任务 */
            this.validateAttainmentExpireTime(user);
            
            for (AttainmentConfig config : ConfigData.getAllAttainmentConfig()) {
                if (config.getType() != ATT_POKERHAND) {
                    continue;
                }
                if (this.isExists(user.getId(), config.getId())) {
                    continue;
                }
                int pokerHandIndex = Integer.valueOf(config.getCondition());
                if (pokerHandIndex == pokerHand.ordinal() || (pokerHandIndex == -1 && pokerHand.ordinal() == 0 && bestCardArray[4].getFace() == 1)) {
                    this.saveAttainment(config.getId(), config.getPrizeId(),
                            user, false);
                    logger.debug(user.getName()+"获得牌型成就"+pokerHand.toString()+",index="+pokerHand.ordinal());
                    break;
                }
            }

        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public AttainmentConfig getAttainmentConfig(int type) {
        if (type == ATT_FOURINTERVARGETREWARD) {
            return this.getGetBankrollConfig();
        }else if(type == ATT_EVEREYDAYLOGIN){
            return this.getLoginAttConfig();
        }
        return null;
    }

    /***
     * 属性更新通知
     * 
     * @param attainmentId
     * @param user
     */
    private void attNotify(int attainmentId, User user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> datas[] = new HashMap[1];
        datas[0] = MessageDataUtil.buildAttrData(AttrEnum.BANKROLL.getType(),
                user.getTotalBankroll());

        Response response = new Response(
                GameProtocol.S_ATTRIBUTE_UPDATE_NOTIFY_INT);
        response.setScope(ResponseScope.SPECIFIED);
        List<User> users = new ArrayList<>(1);
        users.add(user);
        response.setRecievers(users);
        response.setData(datas);
        responseSender.send(response);
    }

    @Override
    public void trrigerInviterFriendAtt(User user) {
        if ((inviteFriendConfig = getInviteFriendConfig()) == null) {
            return;
        }
        try {
            int attainmentId = inviteFriendConfig.getId();
            if (!this.isExists(user.getId(), attainmentId)) {
                this.saveAttainment(attainmentId,
                        inviteFriendConfig.getPrizeId(), user, false);
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

    }

}
