package com.kunlun.poker.log.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kunlun.poker.Config;
import com.kunlun.poker.log.LogConstants;
import com.kunlun.poker.log.service.LogService;
import com.kunlun.poker.util.StringUtils;

@Service("logService")
public class LogServiceImpl implements LogService {
    private static final Logger logger = LoggerFactory
            .getLogger(LogServiceImpl.class);
    private static final  SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy_MM_dd");
    private final Map<String, Invoker> invokerMap = new HashMap<String, Invoker>();
    
    private volatile CSVPrinter csvUserStatisticsPrinter;
    private volatile CSVPrinter csvRoomChipStatisticsPrinter;
    private volatile CSVPrinter csvLoginChipStatisticsPrinter;
    private volatile CSVPrinter csvAchievementChipStatisticsPrinter;
    private volatile CSVPrinter csvLevelUpChipStatisticsPrinter;
    private volatile CSVPrinter csvSaleChipStatisticsPrinter;
    private volatile CSVPrinter csvDrawChipStatisticsPrinter;
    private volatile CSVPrinter csvDealerChipStatisticsPrinter;
	private volatile CSVPrinter csvFbFriendStatisticsPrinter;
    
    private AtomicBoolean userStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean roomChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean loginChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean achievementChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean levelUpChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean saleChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean drawChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean dealerChipStatisticsIsUpdating = new AtomicBoolean(false);
    private AtomicBoolean fbFriendStatisticsIsUpdating = new AtomicBoolean(false);
    
    private volatile CSVPrinter csvGameTypeUserStatisticsPrinter;
    private AtomicBoolean gameTypeUserStaticsIsUpdating = new AtomicBoolean(false);
    
    private volatile CSVPrinter csvRoomBankrollStatisticsPrinter;
    private AtomicBoolean roomBankrollStatisticsIsUpdating = new AtomicBoolean(false);
    
    public LogServiceImpl() {
        init();
    }
    
    private void init(){
            this.invokerMap.put(LogConstants.TL_CSV_USER_STATIC, new Invoker() {
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                        if(userStatisticsIsUpdating.compareAndSet(false, true)){
                            //定长
                            String roomId = map.get("roomId");
                            if(roomId.length()<2){
                                roomId="0"+roomId;
                            }
                            
                            String userId = map.get("userId");
                            userId = getSupplementStr(10 - userId.length()) + userId;
                            
                            String selectType = map.get("selectType");

                            LogServiceImpl.this.print(csvUserStatisticsPrinter, roomId + "\t", userId + "\t", selectType, String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            userStatisticsIsUpdating.set(false);
                            
                            csvUserStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_ROOM_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                    	String roomId = map.get("roomId");
                    	if(roomId.length()<2){
                    		roomId = "0" + roomId;
                    	}
                    	
                    	String totalChips = map.get("totalChips");
                    	totalChips = getSupplementStr(20-totalChips.length()) + totalChips;
                    	
                    	String averageChips = map.get("averageChips");
                    	averageChips = getSupplementStr(10-averageChips.length()) + averageChips;
                    			
                        if(roomChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvRoomChipStatisticsPrinter, roomId + "\t", totalChips + "\t", 
                                    averageChips + "\t", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            roomChipStatisticsIsUpdating.set(false);
                            
                           
                           csvRoomChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_LOGIN_FREE_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {                	
                    	String userId = map.get("userId");
                    	userId = getSupplementStr(10-userId.length()) + userId;
                    	
                    	String loginFreeChips = map.get("loginFreeChips");
                    	loginFreeChips = getSupplementStr(20-loginFreeChips.length()) + loginFreeChips;
                    	
                        if(loginChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvLoginChipStatisticsPrinter, userId + "\t", loginFreeChips + "\t", 
                            		String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            loginChipStatisticsIsUpdating.set(false);
                            
                            csvLoginChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_ACHIEVEMENT_FREE_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                    	String userId = map.get("userId");
                    	userId = getSupplementStr(10-userId.length())  + userId ;
                    	
                    	String achievementFreeChips = map.get("achievementFreeChips");
                    	achievementFreeChips = getSupplementStr(20-achievementFreeChips.length()) + achievementFreeChips;
                    	
                        if(achievementChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvAchievementChipStatisticsPrinter, userId + "\t", achievementFreeChips + "\t", 
                            		String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            achievementChipStatisticsIsUpdating.set(false);
                            
                            csvAchievementChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_LEVELUP_FREE_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                    	String userId = map.get("userId");
                    	userId = getSupplementStr(10-userId.length()) + userId;
                    	
                    	String levelUpFreeChips = map.get("levelUpFreeChips");
                    	levelUpFreeChips = getSupplementStr(20-levelUpFreeChips.length()) + levelUpFreeChips;
                    	
                        if(levelUpChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvLevelUpChipStatisticsPrinter, userId + "\t", levelUpFreeChips + "\t",
                                    String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            levelUpChipStatisticsIsUpdating.set(false);
                            
                            csvLevelUpChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_SALE_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                    	String saleChips = map.get("saleChips");
                    	saleChips = getSupplementStr(20-saleChips.length()) + saleChips;
                    	
                        if(saleChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvSaleChipStatisticsPrinter, saleChips + "\t",
                            		String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            saleChipStatisticsIsUpdating.set(false);
                            
                            csvSaleChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_DRAW_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                    	String drawChips = map.get("drawChips");
                    	drawChips = getSupplementStr(20-drawChips.length()) + drawChips;
                    	
                        if(drawChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvDrawChipStatisticsPrinter, drawChips + "\t",
                            		String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            drawChipStatisticsIsUpdating.set(false);
                            
                            csvDrawChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_DEALER_CHIPS_STATIC, new Invoker() {  
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                    	String dealerChips = map.get("dealerChips");
                    	dealerChips = getSupplementStr(20-dealerChips.length()) + dealerChips;
                    	
                        if(dealerChipStatisticsIsUpdating.compareAndSet(false, true)){
                            LogServiceImpl.this.print(LogServiceImpl.this.csvDealerChipStatisticsPrinter, dealerChips + "\t",
                            		String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            dealerChipStatisticsIsUpdating.set(false);
                            
                            csvDealerChipStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        
            this.invokerMap.put(LogConstants.TL_CSV_FB_FRIEND_STATIC, new Invoker() {
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                        if(fbFriendStatisticsIsUpdating.compareAndSet(false, true)){
                            String userId = map.get("userId");
                            userId = getSupplementStr(10 - userId.length()) + userId;
                            
                            String shareFbCount = map.get(LogConstants.SHARE_FB_COUNT);
                            String inviteFriendCount = map.get(LogConstants.INVITE_FRIEND_COUNT);
                            
                            if(!StringUtils.isEmpty(shareFbCount)){
                                shareFbCount = getSupplementStr(10 - shareFbCount.length()) + shareFbCount;
                                LogServiceImpl.this.print(csvFbFriendStatisticsPrinter, userId + "\t", shareFbCount + "\t", getSupplementStr(10) + "\t", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            }else if(!StringUtils.isEmpty(inviteFriendCount)){
                                inviteFriendCount = getSupplementStr(10 - inviteFriendCount.length()) + inviteFriendCount;
                                LogServiceImpl.this.print(csvFbFriendStatisticsPrinter, userId + "\t", getSupplementStr(10) + "\t", inviteFriendCount + "\t", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            }
                            
                            userStatisticsIsUpdating.set(false);
                            
                            csvFbFriendStatisticsPrinter.flush();
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.invokerMap.put(LogConstants.TL_CSV_GAMETYPE_USER_STATIC, new Invoker() {
                @Override
                public void invoke(Map<String, String> map) {
                    
                    try {
                        if(gameTypeUserStaticsIsUpdating.compareAndSet(false, true)){
                            String roomId = map.get("roomId");
                            if(roomId.length()<2){
                                roomId="0"+roomId;
                            }
                            
                            String userId = map.get("userId");
                            userId = getSupplementStr(10 - userId.length()) + userId;
                            
                            String bankroll = map.get(LogConstants.TOTAL_BANKROLL);
                            bankroll = getSupplementStr(20 - bankroll.length()) + bankroll;
                            
                            String startTime = map.get(LogConstants.START_TIME);
                            String endTime = map.get(LogConstants.END_TIME);
//                            if(!StringUtils.isEmpty(startTime)){
                                LogServiceImpl.this.print(csvGameTypeUserStatisticsPrinter, roomId + "\t", userId + "\t", bankroll + "\t", startTime, endTime);
//                           }else if(!StringUtils.isEmpty(endTime)){
//                               LogServiceImpl.this.print(csvGameTypeUserStatisticsPrinter, roomId, userId, bankroll, getSupplementStr(10), endTime);
//                            }
                            
                            gameTypeUserStaticsIsUpdating.set(false);
                            csvGameTypeUserStatisticsPrinter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            
            this.invokerMap.put(LogConstants.TL_CSV_ROOM_BANKROLL_STATIC, new Invoker() {
                @Override
                public void invoke(Map<String, String> map) {
                    try {
                        if(roomBankrollStatisticsIsUpdating.compareAndSet(false, true)){
                            String roomId = map.get("roomId");
                            if(roomId.length()<2){
                                roomId="0"+roomId;
                            }
                            
                            String bankroll = map.get(LogConstants.TOTAL_BANKROLL);
                            bankroll = getSupplementStr(20 - bankroll.length()) + bankroll;
                            
                            String serviceBankroll = map.get(LogConstants.SERVICE_BANKROLL);
                            serviceBankroll = getSupplementStr(20 - serviceBankroll.length()) + serviceBankroll;
                            LogServiceImpl.this.print(csvRoomBankrollStatisticsPrinter, roomId + "\t", bankroll + "\t", serviceBankroll + "\t", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
                            
                            roomBankrollStatisticsIsUpdating.set(false);
                            
                            csvRoomBankrollStatisticsPrinter.flush();
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
            });
            
    }
    
    private static String getSupplementStr(int remainLength){
        if(remainLength > 0){
            StringBuilder sb = new StringBuilder();
            for(int idx=0; idx< remainLength; idx++){
                sb.append("0");
            }
            return sb.toString();
        }
        return null;
    }
    
    private static void checkLogFile(String path){
        File file =new File(path);    
        if(!file.exists() && !file.isDirectory()){       
            file.mkdir();    
         }
    }
    
    private static boolean isExist(String path){
        File file=new File(path);    
        if(file.exists()){
            return true;
        }
        return false;
    }
    
    private static File createFile(String path){
        File file=new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    private static Object[] getBeforeData(File file, boolean isNew) throws Exception{
        if(!isNew){
            try(CSVParser parse = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.EXCEL)){
                List<CSVRecord> list = parse.getRecords();
                if(list.size() > 0){
                    list.remove(0);
                }
                return  list.toArray();
            }
        }
        return null;
    }
    
    private void print(CSVPrinter printer, Object... object) throws Exception{
        printer.printRecord(object);
    }
    
    private void createTomorrowTable(String logDir, String type, String tomStamp){
        String namePath = String.format("%s%s_%s%s", logDir, type, tomStamp,".csv");
        if(!isExist(namePath)){
            createFile(namePath);
        }
    }
    
    private void createTable(String logDir, String type) throws Exception{
        String timeStamp = DATAFORMAT.format(new Date());
        String tomStamp = DATAFORMAT.format((new Date().getTime() + 86400000));
        //String name = String.format("%s%s_%s", prefixName, type, timeStamp);
        //String namePath = relativePath + name + ".csv";

        String namePath = String.format("%s%s_%s%s", logDir, type, timeStamp,".csv");
        
        boolean isNew = false;
        File file=null;
        if(!isExist(namePath)){
            file = createFile(namePath);
            isNew = true;
        }else{
            file = new File(namePath);
        }
        Object[] beforeData = getBeforeData(file, isNew);
        this.createTomorrowTable(logDir, type, tomStamp);
        switch (type) {
        case LogConstants.TL_CSV_USER_STATIC :{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("roomId","userId","selectType","time")*/);
            if(this.userStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvUserStatisticsPrinter != null){
                    this.csvUserStatisticsPrinter.flush();
                    this.csvUserStatisticsPrinter.close();
                    this.csvUserStatisticsPrinter = null;
                }
                this.csvUserStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvUserStatisticsPrinter.printRecords(beforeData);
                }
                this.userStatisticsIsUpdating.set(false);
            }
            break;
        }
        case  LogConstants.TL_CSV_FB_FRIEND_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("userId","shareFbCount","inviteFirendCount","time")*/);
            
            if(this.fbFriendStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvFbFriendStatisticsPrinter != null){
                    this.csvFbFriendStatisticsPrinter.flush();
                    this.csvFbFriendStatisticsPrinter.close();
                    this.csvFbFriendStatisticsPrinter = null;
                }
                this.csvFbFriendStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvFbFriendStatisticsPrinter.printRecords(beforeData);
                }
                this.fbFriendStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        case  LogConstants.TL_CSV_ROOM_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("roomId","totalChips","averageChips","time")*/);
            if(this.roomChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvRoomChipStatisticsPrinter != null){
                    this.csvRoomChipStatisticsPrinter.flush();
                    this.csvRoomChipStatisticsPrinter.close();
                    this.csvRoomChipStatisticsPrinter = null;
                }
                this.csvRoomChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvRoomChipStatisticsPrinter.printRecords(beforeData);
                }
                this.roomChipStatisticsIsUpdating.set(false);
            }
        }
        case LogConstants.TL_CSV_GAMETYPE_USER_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("roomId","userId","bankroll","startTime","endTime")*/);
            
            if(this.gameTypeUserStaticsIsUpdating.compareAndSet(false, true)){
                if(this.csvGameTypeUserStatisticsPrinter != null){
                    this.csvGameTypeUserStatisticsPrinter.flush();
                    this.csvGameTypeUserStatisticsPrinter.close();
                    this.csvGameTypeUserStatisticsPrinter = null;
                }
                this.csvGameTypeUserStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvGameTypeUserStatisticsPrinter.printRecords(beforeData);
                }
                this.gameTypeUserStaticsIsUpdating.set(false);
            }
            
            break;
        }
        
        case LogConstants.TL_CSV_ROOM_BANKROLL_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("roomId","bankroll","serviceBankroll","time")*/);
            
            if(this.roomBankrollStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvRoomBankrollStatisticsPrinter != null){
                    this.csvRoomBankrollStatisticsPrinter.flush();
                    this.csvRoomBankrollStatisticsPrinter.close();
                    this.csvRoomBankrollStatisticsPrinter = null;
                }
                this.csvRoomBankrollStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvRoomBankrollStatisticsPrinter.printRecords(beforeData);
                }
                
                this.roomBankrollStatisticsIsUpdating.set(false);
            }
            
            break;
        }
        
        case  LogConstants.TL_CSV_LOGIN_FREE_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("userId","loginFreeChips","time")*/);
            
            if(this.loginChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvLoginChipStatisticsPrinter != null){
                    this.csvLoginChipStatisticsPrinter.flush();
                    this.csvLoginChipStatisticsPrinter.close();
                    this.csvLoginChipStatisticsPrinter = null;
                }
                this.csvLoginChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvLoginChipStatisticsPrinter.printRecords(beforeData);
                }
                this.loginChipStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        case  LogConstants.TL_CSV_ACHIEVEMENT_FREE_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("userId","achievementFreeChips","time")*/);
            
            if(this.achievementChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvAchievementChipStatisticsPrinter != null){
                    this.csvAchievementChipStatisticsPrinter.flush();
                    this.csvAchievementChipStatisticsPrinter.close();
                    this.csvAchievementChipStatisticsPrinter = null;
                }
                this.csvAchievementChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvAchievementChipStatisticsPrinter.printRecords(beforeData);
                }
                this.achievementChipStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        case  LogConstants.TL_CSV_LEVELUP_FREE_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("userId","levelUpChips","time")*/);
            
            if(this.levelUpChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvLevelUpChipStatisticsPrinter != null){
                    this.csvLevelUpChipStatisticsPrinter.flush();
                    this.csvLevelUpChipStatisticsPrinter.close();
                    this.csvLevelUpChipStatisticsPrinter = null;
                }
                this.csvLevelUpChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvLevelUpChipStatisticsPrinter.printRecords(beforeData);
                }
                this.levelUpChipStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        case  LogConstants.TL_CSV_SALE_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("saleChips","time")*/);
            
            if(this.saleChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvSaleChipStatisticsPrinter != null){
                    this.csvSaleChipStatisticsPrinter.flush();
                    this.csvSaleChipStatisticsPrinter.close();
                    this.csvSaleChipStatisticsPrinter = null;
                }
                this.csvSaleChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvSaleChipStatisticsPrinter.printRecords(beforeData);
                }
                this.saleChipStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        case  LogConstants.TL_CSV_DRAW_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("drawChips","time")*/);
            
            if(this.drawChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvDrawChipStatisticsPrinter != null){
                    this.csvDrawChipStatisticsPrinter.flush();
                    this.csvDrawChipStatisticsPrinter.close();
                    this.csvDrawChipStatisticsPrinter = null;
                }
                this.csvDrawChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvDrawChipStatisticsPrinter.printRecords(beforeData);
                }
                this.drawChipStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        case  LogConstants.TL_CSV_DEALER_CHIPS_STATIC:{
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL/*CSVFormat.EXCEL.withHeader("dealerChips","time")*/);
            
            if(this.dealerChipStatisticsIsUpdating.compareAndSet(false, true)){
                if(this.csvDealerChipStatisticsPrinter != null){
                    this.csvDealerChipStatisticsPrinter.flush();
                    this.csvDealerChipStatisticsPrinter.close();
                    this.csvDealerChipStatisticsPrinter = null;
                }
                this.csvDealerChipStatisticsPrinter = csvPrinter;
                if(beforeData != null && beforeData.length != 0){
                    this.csvDealerChipStatisticsPrinter.printRecords(beforeData);
                }
                this.dealerChipStatisticsIsUpdating.set(false);
            }
            break;
        }
        
        default:
            break;
        }
    }
    
    @Override
    public void createTables() throws Exception {
//        String relativePath = System.getProperty("user.dir")+"\\";
//        String prefixName = Config.getInstance().getLogPrifexName();
//        if(StringUtils.isEmpty(prefixName)){
//            prefixName = "log";
//        }
//        String logFile = relativePath + prefixName;
        String logDir = Config.getInstance().getLogDirName();
        
        checkLogFile(logDir);
        
        logDir += File.separator;
        
        this.createTable(logDir, LogConstants.TL_CSV_USER_STATIC);
        /*** csvRoomChipStatisticsr**/
        this.createTable(logDir, LogConstants.TL_CSV_ROOM_CHIPS_STATIC);
        /*** csvLoginChipStatistics**/
        this.createTable(logDir, LogConstants.TL_CSV_LOGIN_FREE_CHIPS_STATIC);
        /*** csvAchievementChipStatistics**/
        this.createTable(logDir, LogConstants.TL_CSV_ACHIEVEMENT_FREE_CHIPS_STATIC);
        /*** csvLevelUpChipStatistics**/
        this.createTable(logDir, LogConstants.TL_CSV_LEVELUP_FREE_CHIPS_STATIC);
        /*** csvSaleChipStatistics**/
        this.createTable(logDir, LogConstants.TL_CSV_SALE_CHIPS_STATIC);
        /*** csvDrawChipStatistics**/
        this.createTable(logDir, LogConstants.TL_CSV_DRAW_CHIPS_STATIC);
        /*** csvDealerChipStatistics**/
        this.createTable(logDir, LogConstants.TL_CSV_DEALER_CHIPS_STATIC);
        
        this.createTable(logDir, LogConstants.TL_CSV_FB_FRIEND_STATIC);
        
        this.createTable(logDir, LogConstants.TL_CSV_GAMETYPE_USER_STATIC);
        
        this.createTable(logDir, LogConstants.TL_CSV_ROOM_BANKROLL_STATIC);
                
    }
    
    
 
    @Override
    public void doLog(Map<String, String> map) {
        if(map == null){
            logger.error("消息内容为空");
            return;
        }
        String table = map.get(LogConstants.TABLE);
        logger.debug("接收到的表名-------------------------："+ table);
        this.invokerMap.get(table).invoke(map);
    }

    @Override
    public void stop() {
        try {
            if(this.csvUserStatisticsPrinter != null){
                this.csvUserStatisticsPrinter.flush();
                this.csvUserStatisticsPrinter.close();
            }
            
            if(this.csvFbFriendStatisticsPrinter != null){
                this.csvFbFriendStatisticsPrinter.flush();
                this.csvFbFriendStatisticsPrinter.close();
            }
            
            if(this.csvGameTypeUserStatisticsPrinter != null){
                this.csvGameTypeUserStatisticsPrinter.flush();
                this.csvGameTypeUserStatisticsPrinter.close();
            }
            
            if(this.csvRoomBankrollStatisticsPrinter != null){
                this.csvRoomBankrollStatisticsPrinter.flush();
                this.csvRoomBankrollStatisticsPrinter.close();
            }
            
            if(this.csvAchievementChipStatisticsPrinter != null){
                this.csvAchievementChipStatisticsPrinter.flush();
                this.csvAchievementChipStatisticsPrinter.close();
            }
            
            if(this.csvDealerChipStatisticsPrinter != null){
                this.csvDealerChipStatisticsPrinter.flush();
                this.csvDealerChipStatisticsPrinter.close();
            }
            
            if(this.csvDrawChipStatisticsPrinter != null){
                this.csvDrawChipStatisticsPrinter.flush();
                this.csvDrawChipStatisticsPrinter.close();
            }
            
            if(this.csvLevelUpChipStatisticsPrinter != null){
                this.csvLevelUpChipStatisticsPrinter.flush();
                this.csvLevelUpChipStatisticsPrinter.close();
            }
            
            if(this.csvLoginChipStatisticsPrinter != null){
                this.csvLoginChipStatisticsPrinter.flush();
                this.csvLoginChipStatisticsPrinter.close();
            }
            
            if(this.csvRoomChipStatisticsPrinter != null){
                this.csvRoomChipStatisticsPrinter.flush();
                this.csvRoomChipStatisticsPrinter.close();
            }
            
            if(this.csvSaleChipStatisticsPrinter != null){
                this.csvSaleChipStatisticsPrinter.flush();
                this.csvSaleChipStatisticsPrinter.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    interface Invoker{
        void invoke(Map<String, String> map);
    }
    
    
    public static void main(String[] args) throws Exception{
        LogServiceImpl logService = new LogServiceImpl();
        Map<String,String> map = new HashMap<String, String>();
        map.put("userId", "7");
        map.put("levelUpFreeChips", "4000");
        map.put("table", "tl_csv_levelUp_free_chips_statics");
        logService.createTable("D:\\pok\\poker-server\\trunk\\poker-log\\log\\", LogConstants.TL_CSV_LEVELUP_FREE_CHIPS_STATIC);
        logService.doLog(map);
//        String relativePath = System.getProperty("user.dir")+"\\";
//        String prefixName = Config.getInstance().getLogDirName();
//        if(StringUtils.isEmpty(prefixName)){
//            prefixName = "log";
//        }
//        String logFile = relativePath + prefixName;
//        checkLogFile(logFile);
//        
//        prefixName += "\\";
//        
//        String timeStamp = DATAFORMAT.format(new Date());
//        
//        String name = String.format("%s%s_%s", prefixName, LogConstants.TL_CSV_USER_STATIC , timeStamp);
//        String namePath = relativePath + name + ".csv";
//        
//        File file=null;
//        boolean isNew = false;
//        if(!isExist(namePath)){
//             file = createFile(namePath);
//             isNew =false;
//        }else{
//            file = new File(namePath);
//        }
//        
//        
//        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(file)), CSVFormat.EXCEL);
//        Object[] obs = getBeforeData(file, isNew);
//        if(obs != null && obs.length!=0){
//            csvPrinter.printRecords(obs);
//        }
//        csvPrinter.print("0000000001\t");
//        csvPrinter.print("2222222222\t");
//        csvPrinter.print("3333333333\t");
//        csvPrinter.print("2554444444\t");
//        csvPrinter.flush();
//        
//        csvPrinter.println();
//        
//        csvPrinter.print("tgg");
//        csvPrinter.print("asds");
//        csvPrinter.print("f");
//        csvPrinter.print("dfdf");
//        csvPrinter.flush();
//        
//        
//        csvPrinter.close();
        
//        try{
//             FileOutputStream writer=new FileOutputStream(file);
//             writer.write("dsd12,ffff,200000000000000000000,ssssssssss\n".getBytes());
//             writer.write("dsd12,ffff,200000000000000000001,ssssssssss\n".getBytes());
//             writer.close();
//         } catch (IOException e){
//                 e.printStackTrace();
//         }
        
     }

}
