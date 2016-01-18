package com.kunlun.poker.center.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.mina.util.ConcurrentHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.kunlun.poker.center.data.NoticeWrapper;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.service.NoticeService;
import com.kunlun.poker.center.system.GameProtocol;
import com.kunlun.poker.domain.Notice;
import com.kunlun.poker.util.StringUtils;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeWrapper noticeWrapper;
    @Autowired
    private ResponseSender responseSender;
    @Autowired
    private SessionRoleManager sessionRoleManger;
    
    private final Set<Integer> noticeIdSet = new ConcurrentHashSet<Integer>();
    private final List<Notice> noticeList = new CopyOnWriteArrayList<Notice>();
    /** 已读列表*/
    private final Map<Integer, Set<Integer>> alReadMap = new ConcurrentHashMap<Integer, Set<Integer>>();
    /** 未读列表*/
    private final Map<Integer, Set<Integer>> notReadMap = new ConcurrentHashMap<Integer, Set<Integer>>();
    /** 删除列表*/
    private final Map<Integer, Set<Integer>> delMap = new ConcurrentHashMap<Integer, Set<Integer>>();
    
    /***
     * 系统启动的时候调用，运维修改公告的时候调用，调用不频繁
     * @param isBroadcast
     * @return
     */
    @Override
    public List<Notice> loadNotices(boolean isBroadcast) {
        List<Notice> list = this.noticeWrapper.selectNotices();
        if(!noticeList.isEmpty()){
            noticeList.clear();
            noticeIdSet.clear();
        }
        noticeList.addAll(list);
        for(Notice notice : list){
            noticeIdSet.add(notice.getId());
        }
        
        if(isBroadcast){
            if(!alReadMap.isEmpty()){
                for(Map.Entry<Integer, Set<Integer>> entry : alReadMap.entrySet()){
                    Set<Integer> set = entry.getValue();
                    for(Iterator<Integer> itr = set.iterator(); itr.hasNext();){
                        int noticeId = itr.next();
                        if(!noticeIdSet.contains(noticeId)){
                            itr.remove();
                        }
                        
                    }
                }
            }
            
            if(!delMap.isEmpty()){
                for(Map.Entry<Integer, Set<Integer>> entry : delMap.entrySet()){
                    Set<Integer> set = entry.getValue();
                    for(Iterator<Integer> itr = set.iterator(); itr.hasNext();){
                        int noticeId = itr.next();
                        if(!noticeIdSet.contains(noticeId)){
                            itr.remove();
                        }
                        
                    }
                }
            }
            
            if(!notReadMap.isEmpty()){
                for(Map.Entry<Integer, Set<Integer>> entry : notReadMap.entrySet()){
                    Set<Integer> set = entry.getValue();
                    for(Iterator<Integer> itr = set.iterator(); itr.hasNext();){
                        int noticeId = itr.next();
                        if(!noticeIdSet.contains(noticeId)){
                            itr.remove();
                        }
                        
                    }
                    
                    Set<Integer> alRead = alReadMap.get(entry.getKey());
                    Set<Integer> dlRead = delMap.get(entry.getKey());
                    //新增公告
                    for(int id : noticeIdSet){
                        if(!set.contains(id) && !alRead.contains(id) && !dlRead.contains(id)){
                            set.add(id);
                        }
                            
                    }
                }
            }
        
            //广播
            for(Role role : this.sessionRoleManger.getRoles()){
                User user = (User) role;
                this.notifyNewNotice(user);
            }
        }
        return noticeList;
    }

    @Override
    public void login(User user) {
        this.loginAndConvertNoticeInfo(user);
        this.notifyNewNotice(user);
    }
    
    private void loginAndConvertNoticeInfo(User user){
        String noticeInfo = user.getNoticeInfo();
        Set<Integer> alReadSet = new ConcurrentHashSet<Integer>();
        Set<Integer> notReadSet = new ConcurrentHashSet<Integer>();
        Set<Integer> delReadSet = new ConcurrentHashSet<Integer>();
        this.alReadMap.put(user.getId(), alReadSet);
        this.notReadMap.put(user.getId(), notReadSet);
        this.delMap.put(user.getId(), delReadSet);
        
        if(!StringUtils.isEmpty(noticeInfo)){
            String[] strArr = noticeInfo.split(";");
            
            String alReadStr = null;
            String notReadStr = null;
            String delReadStr = null;
            int length = strArr.length;
            if(length==3 || length == 2 || length == 4){
                alReadStr = strArr[0];
                notReadStr = strArr[1];
                delReadStr = strArr[2];
            }
            
            if(!StringUtils.isEmpty(alReadStr)){
                String[] alResdStrArr = alReadStr.split(",");
                for(String noticeId : alResdStrArr){
                    alReadSet.add(Integer.valueOf(noticeId));
                }
                
            }
            
            if(!StringUtils.isEmpty(notReadStr)){
                String[] notReadStrArr = notReadStr.split(",");
                for(String noticeId : notReadStrArr){
                    notReadSet.add(Integer.valueOf(noticeId));
                }
                
            }
            
            if(!StringUtils.isEmpty(delReadStr)){
                String[] delReadStrArr = delReadStr.split(",");
                for(String noticeId : delReadStrArr){
                    delReadSet.add(Integer.valueOf(noticeId));
                }
                
            }
            
        }
        
        if(!alReadSet.isEmpty()){
            for(Iterator<Integer> itr = alReadSet.iterator(); itr.hasNext();){
                int noticeId = itr.next();
                if(!noticeIdSet.contains(noticeId)){
                    itr.remove();
                }
            }
        }
        
        if(!notReadSet.isEmpty()){
            for(Iterator<Integer> itr = notReadSet.iterator(); itr.hasNext();){
                int noticeId = itr.next();
                if(!noticeIdSet.contains(noticeId)){
                    itr.remove();
                }
            }
            
        }
        for( int id : noticeIdSet){
            if(!notReadSet.contains(id) && !delReadSet.contains(id) && !alReadSet.contains(id)){
                notReadSet.add(id);
            }
        }
        
        if(!delReadSet.isEmpty()){
            for(Iterator<Integer> itr = delReadSet.iterator(); itr.hasNext();){
                int noticeId = itr.next();
                if(!noticeIdSet.contains(noticeId)){
                    itr.remove();
                }
                
            }
        }
        
    }
    
    private void resetUserNoticeInfo(User user){
        int userId = user.getId();
        StringBuilder sb = new StringBuilder();
        Set<Integer> alReadNoticeSet = alReadMap.remove(userId);
        if(alReadNoticeSet != null && !alReadNoticeSet.isEmpty()){
            int size=alReadNoticeSet.size();
            int index=0;
            for(int id : alReadNoticeSet){
                sb.append(id);
                
                if(index != size-1){
                    sb.append(",");
                }
                index++;
            }
            
        }
        
        sb.append(";");
        
        Set<Integer> notReadNoticeSet = notReadMap.remove(userId);
        if(notReadNoticeSet != null && !notReadNoticeSet.isEmpty()){
            int size = notReadNoticeSet.size();
            int index=0;
            for(int id : notReadNoticeSet){
                sb.append(id);
                
                if(index != size-1){
                    sb.append(",");
                }
                index++;
            }
        }
     
        sb.append(";");
        
        Set<Integer> delReadSet = delMap.remove(userId);
        if(delReadSet != null && !delReadSet.isEmpty()){
            int size = delReadSet.size();
            int index = 0;
            for(int id : delReadSet){
                sb.append(id);
                
                if(index != size-1){
                    sb.append(",");
                }
                index++;
            }
        }
        
        sb.append(";").append(" ");
        
        user.setNoticeInfo(sb.toString());
        this.noticeWrapper.updateNoticeInfo(userId, sb.toString());
    }

    @Override
    public void exit(User user) {
        this.resetUserNoticeInfo(user);
        
    }

    @Override
    public void stop() {
        for(Role role : this.sessionRoleManger.getRoles()){
            User user = (User) role;
            this.resetUserNoticeInfo(user);
        }
    }

    @Override
    public void notifyNewNotice(User user) {
        Set<Integer> notReadSet = this.notReadMap.get(user.getId());
        int notReadCount=0;
        if(notReadSet != null && !notReadSet.isEmpty()){
            notReadCount = notReadSet.size();
            Map<String, Object> data = new HashMap<String, Object>(1);
            data.put("notReadCount", notReadCount);
            Response response = new Response(GameProtocol.S_NEW_NOTICE_NOTIFY);
            response.setScope(ResponseScope.SPECIFIED);
            List<User> users = new ArrayList<>(1);
            users.add(user);
            response.setRecievers(users);
            response.setData(data);
            responseSender.send(response);
            
            
        }
        
    }

    @Override
    public boolean whetheRead(User user, int noticeId) {
        int userId = user.getId();
        Set<Integer> alReadSet = this.alReadMap.get(userId);
        Set<Integer> notReadSet = this.notReadMap.get(userId);
        
        if(alReadSet != null && !alReadSet.isEmpty() && alReadSet.contains(noticeId)){
            return true;
        }
        
        if(notReadSet != null && !notReadSet.isEmpty() && notReadSet.contains(noticeId)){
            return false;
        }
        
        return false;
    }
    
    
    @Override
    public boolean whetheDel(User user, int noticeId) {
        Set<Integer> delReadSet = this.delMap.get(user.getId());
        if(delReadSet != null && !delReadSet.isEmpty() && delReadSet.contains(noticeId)){
            return true;
        }
        return false;
    }

    @Override
    public boolean queryNoticeContent(User user, int noticeId) {
        int userId = user.getId();
        
        Set<Integer> alReadSet = this.alReadMap.get(userId);
        Set<Integer> notReadSet = this.notReadMap.get(userId);
        
        boolean flag = false;
        flag = alReadSet.add(noticeId);
        flag =  notReadSet.remove(noticeId);
        
        return flag;
    }

    @Override
    public boolean delNotice(User user, int noticeId) {
        Set<Integer> delReadSet = this.delMap.get(user.getId());
        Set<Integer> alReadSet = this.alReadMap.get(user.getId());
        Set<Integer> notReadSet = this.notReadMap.get(user.getId());
        
        alReadSet.remove(noticeId);
        notReadSet.remove(noticeId);
        boolean flag = delReadSet.add(noticeId);
        
        return flag;
    }

    @Override
    public List<Notice> getNotices() {
        return this.noticeList;
    }
    
    

}
