package com.kunlun.poker.domain;

import java.util.HashMap;
import java.util.Map;



/**
 * 3.1自由桌房间列表大厅展示信息： 1、房间名称 2、盲注 3、最小携带 4、房间类型 5、当前房间总人数（不展示给用户）
 * 6、当前房间总桌数（不展示给用户） 7、房间人数上限（不展示给用户） 8、房间桌数上限（不展示给用户） 9、房间Id（不展示给用户）
 * 10、房间内所有游戏桌，每局游戏用户获得的经验值（不展示给用户） 11、房间内所有游戏桌，每局游戏抽水百分比（不展示给用户）
 *
 * @author Administrator
 */
public class Room implements Simplifiable<Map<String, Object>>{
    /**
     * 构造函数，初始化一个ArrayList
     */
    public Room() {
    }
   
    private int id;
    private String name;
    private int smallBlindBets;
    private int type;
    private String description;
    private int carry;
    private int minStake;
    private int maxStake;
    private int userCapacity;
    private int deskCapacity;
    private int numberOfUsers;
    private int numberOfDesks;
    private int expPerRound;
    //收取服务费的系数，由于是经常变化，需要设置set，get方法
    private float coefficient = (float) 0.05;
    private GameType gameType;
    private int playerNums;
    private String buyInChipList;
    private String robotProb;

	public void setSmallBlindBets(int smallBlindBets) {
		this.smallBlindBets = smallBlindBets;
	}

	public float getCoefficient() {
            return coefficient;
    }

    public void setCoefficient(float coefficient) {
            this.coefficient = coefficient;
    }

    public int getMinStake() {
            return minStake;
    }

    public void setMinStake(int minStake) {
            this.minStake = minStake;
    }
    
    public void setMaxStake(int maxStake){
            this.maxStake = maxStake;
    }
    
    public int getMaxStake(){
            return maxStake;
    }

    /**
     * 获取房间类型
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取房间id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取房间名称
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取小盲注数量
     * @return the smallBlindBets
     */
    public int getSmallBlindBets() {
        return smallBlindBets;
    }

    /**
     * 获取大盲注数量
     * @return 
     */
    public int getBigBlindBets() {
        return smallBlindBets * 2;
    }

    /**
     * @param smallBlind the smallBlindBets to set
     */
    public void setSmallBlind(int smallBlind) {
        this.smallBlindBets = smallBlind;
    }

    /**
     * 房间描述
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 携带的最少筹码
     * @return the minimumStake
     */
    public int getCarry() {
        return carry;
    }

    /**
     * @param minimumStake the minimumStake to set
     */
    public void setCarry(int value) {
        this.carry = value;
    }

    /**
     * 房间人数上限
     * @return the userCapacity
     */
    public int getUserCapacity() {
        return userCapacity;
    }

    /**
     * @param userCapacity the userCapacity to set
     */
    public void setUserCapacity(int userCapacity) {
        this.userCapacity = userCapacity;
    }

    /**
     * 房间桌数上限
     * @return the deskCapacity
     */
    public int getDeskCapacity() {
        return deskCapacity;
    }

    /**
     * @param deskCapacity the deskCapacity to set
     */
    public void setDeskCapacity(int deskCapacity) {
        this.deskCapacity = deskCapacity;
    }

    /**
     * 每一局玩家获得的经验
     * @return the expPerRound
     */
    public int getExpPerRound() {
        return expPerRound;
    }

    /**
     * @param expPerRound the expPerRound to set
     */
    public void setExpPerRound(int expPerRound) {
        this.expPerRound = expPerRound;
    }

    /**
     * 房间内玩家数量
     * @return the numberOfUsers
     */
    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    /**
     * @param numberOfUsers the numberOfUsers to set
     */
    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    /**
     * 房间内桌子的数量
     * @return the numberOfDesks
     */
    public int getNumberOfDesks() {
        return numberOfDesks;
    }

    /**
     * @param numberOfDesks the numberOfDesks to set
     */
    public void setNumberOfDesks(int numberOfDesks) {
        this.numberOfDesks = numberOfDesks;
    }

    /**
     * 玩法
     * @return
     */
	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	/**
	 * 在线玩家人数
	 * @return
	 */
	public int getPlayerNums() {
        return playerNums;
    }

    public void setPlayerNums(int playerNums) {
        this.playerNums = playerNums;
    }
    
    /**
     * 最大买入额
     * @return
     */
    public int getMaxCarry() {
        return carry * 10;
    }
    
    /**
     * 返回房间信息
     * @return 
     */
    @Override
    public String toString() {
        return "[房间]" + name;
    }
    
    public String getBuyInChipList() {
		return buyInChipList;
	}

	public void setBuyInChipList(String buyInChipList) {
		this.buyInChipList = buyInChipList;
	}
	
	public String[] setChipList(String buyInChipList){
		return buyInChipList.split(",");
	}

    @Override
    public Map<String, Object> simplify() {
        Map<String, Object> simplified = new HashMap<>();
        simplified.put("id", getId());
        simplified.put("name", getName());
        simplified.put("type", getType());
        simplified.put("gameType", getGameType().toString());
        simplified.put("minStake", getMinStake());
        simplified.put("maxStake", getMaxStake());
        simplified.put("smallBlindBets", getSmallBlindBets());
        simplified.put("bigBlindBets", getBigBlindBets());
        simplified.put("userCapacity", getUserCapacity());
        simplified.put("playerNums", getPlayerNums());
        simplified.put("chipList", setChipList(getBuyInChipList()));
        simplified.put("robotProb", getRobotProb());
        return simplified;
    }

	public String getRobotProb() {
		return robotProb;
	}

	public void setRobotProb(String robotProb) {
		this.robotProb = robotProb;
	}
}
