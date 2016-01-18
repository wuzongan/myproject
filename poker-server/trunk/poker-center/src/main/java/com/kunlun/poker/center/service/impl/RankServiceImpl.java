package com.kunlun.poker.center.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.event.Event;
import com.googlecode.canoe.event.EventUtil;
import com.googlecode.canoe.event.anno.EventListener;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.center.data.RankWrapper;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.domain.UserEvent;
import com.kunlun.poker.center.domain.rank.BankrollRank;
import com.kunlun.poker.center.domain.rank.LevelRank;
import com.kunlun.poker.center.domain.rank.Rank;
import com.kunlun.poker.center.domain.rank.SingleWinBankrollRank;
import com.kunlun.poker.center.domain.rank.WinRateRank;
import com.kunlun.poker.center.service.RankService;
import com.kunlun.poker.center.service.UserService;
import com.kunlun.poker.center.system.SchedulerUtil;

@Service("rankService")
public class RankServiceImpl implements RankService {

	private static final Logger logger = LoggerFactory
			.getLogger(RankServiceImpl.class);

	@Autowired
	private RankWrapper rankWrapper;
	@Autowired
	private UserService userService;
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private ResponseSender responseSender;

	public RankServiceImpl() {
		EventUtil.gatherListeners(this);
	}

	private final TreeSet<WinRateRank> winRateSet = new TreeSet<WinRateRank>();
	private final TreeSet<LevelRank> levelRankSet = new TreeSet<LevelRank>();
	private final TreeSet<SingleWinBankrollRank> singleWinBankrollSet = new TreeSet<SingleWinBankrollRank>();
	private final TreeSet<BankrollRank> bankrollRankSet = new TreeSet<BankrollRank>();

	private final Map<User, WinRateRank> winRateMap = new HashMap<User, WinRateRank>();
	private final Map<User, LevelRank> levelRankMap = new HashMap<User, LevelRank>();
	private final Map<User, SingleWinBankrollRank> singleWinBankrollMap = new HashMap<User, SingleWinBankrollRank>();
	private final Map<User, BankrollRank> bankrollRankMap = new HashMap<User, BankrollRank>();

	/**
	 * 加载所有排行榜数据
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void loadAllRank() {
		List<HashMap<String, Number>> rankMapList = rankWrapper.selectRanks();
		Set<Integer> userSet = new HashSet<Integer>();
		for (HashMap<String, Number> map : rankMapList) {
			userSet.add(map.get("userId").intValue());
		}
		if (userSet.isEmpty()) {
			return;
		}
		List<User> userList = rankWrapper.selectUsers(new ArrayList(userSet));
		Map<Integer, User> userMap = new HashMap<Integer, User>();
		for (User user : userList) {
			userMap.put(user.getId(), user);
		}

		for (HashMap<String, Number> map : rankMapList) {
			int type = map.get("type").intValue();
			int userId = map.get("userId").intValue();
			User user = userMap.get(userId);

			if (type == RANK_TYPE_WINRATE) {
				WinRateRank winRateRank = new WinRateRank(user);
				winRateSet.add(winRateRank);
				winRateMap.put(user, winRateRank);
			} else if (type == RANK_TYPE_LEVEL) {
				LevelRank levelRank = new LevelRank(user);
				levelRankSet.add(levelRank);
				levelRankMap.put(user, levelRank);
			} else if (type == RANK_TYPE_SINGELWINBANKROLL) {
				SingleWinBankrollRank singleWinBankrollRank = new SingleWinBankrollRank(
						user);
				singleWinBankrollSet.add(singleWinBankrollRank);
				singleWinBankrollMap.put(user, singleWinBankrollRank);
			} else if (type == RANK_TYPE_BANKROLL) {
				BankrollRank bankrollRank = new BankrollRank(user);
				bankrollRankSet.add(bankrollRank);
				bankrollRankMap.put(user, bankrollRank);
			}
		}
	}

	@EventListener(event = UserEvent.ATTR_CHANGED)
	public void recordRank(Event<User> event) {
		User user = event.getTarget();
		SchedulerUtil.submitRankTask(
				() -> {

					recordRank(new WinRateRank(user), winRateSet, winRateMap,
							RANK_TYPE_WINRATE);
					recordRank(new SingleWinBankrollRank(user),
							singleWinBankrollSet, singleWinBankrollMap,
							RANK_TYPE_SINGELWINBANKROLL);
					recordRank(new LevelRank(user), levelRankSet, levelRankMap,
							RANK_TYPE_LEVEL);
					recordRank(new BankrollRank(user), bankrollRankSet,
							bankrollRankMap, RANK_TYPE_BANKROLL);
				}, scheduler);
	}
	
//	private <E extends Rank<? extends Number, E>> void remove(TreeSet<E> rankSet, E rank){
//	    for(Iterator<E> itr =  rankSet.iterator(); itr.hasNext();){
//	        Rank<?,?> r = itr.next();
//	        if(r.getUser().getId() == rank.getUser().getId()){
//	            itr.remove();
//	        }
//	    }
//	}

	private  <E extends Rank<? extends Number, E>> void recordRank(E rank,
			TreeSet<E> rankSet, Map<User, E> rankMap, int type) {

		User user = rank.getUser();
		int userId = user.getId();

		E oldRank = rankMap.get(user);
		if (oldRank != null) {
			rankSet.remove(oldRank);
//			if(!flag){
//			    remove(rankSet, oldRank);
//			}
//         System.out.println("排行榜类型：" + type +"删除："+oldRank.getUser().getName()+",删除标志flag："+ flag+",oldRank:"+oldRank+",rank:"+rank);
			rankSet.add(rank);
			rankMap.put(user, rank);
		} else if (rankSet.size() < MAX_SIZE * 2) {
			boolean flag = rankSet.add(rank);
			if (flag)
				rankWrapper.insertRank(userId, type);
			rankMap.put(user, rank);
		} else {
			E lastRank = rankSet.last();
			if (lastRank.compareTo(rank) < 0) {
				User lastUser = lastRank.getUser();
				rankSet.remove(lastRank);
				rankMap.remove(lastUser);
				rankSet.add(rank);
				rankWrapper.updateRank(userId, lastUser.getId(), type);
				rankMap.put(user, rank);
			}
		}
	}

	public Collection<Rank<?, ?>> getRanks(final int type) {
		Future<Collection<Rank<?, ?>>> future = SchedulerUtil.submitRankTask(
				() -> {
					TreeSet<? extends Rank<?, ?>> ranks = null;
					switch (type) {
					case RANK_TYPE_LEVEL:
						ranks = levelRankSet;
						break;
					case RANK_TYPE_BANKROLL:
						ranks = bankrollRankSet;
						break;
					case RANK_TYPE_SINGELWINBANKROLL:
						ranks = singleWinBankrollSet;
						break;
					case RANK_TYPE_WINRATE:
						ranks = winRateSet;
						break;
					}

					if (ranks != null) {
						ArrayList<Rank<?, ?>> ret = new ArrayList<>();

						int avaliable = Math.min(ranks.size(),
								RankService.MAX_SIZE);
						for (Rank<?, ?> rank : ranks) {
							if (avaliable <= 0) {
								break;
							}

							ret.add(rank);
						}

						return ret;
					}

					return null;

				}, scheduler);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public Rank<?, ?> getFirstRank(int type) {
		Future<Rank<?, ?>> future = SchedulerUtil.submitRankTask(() -> {
			switch (type) {
			case RANK_TYPE_LEVEL:
				return levelRankSet.first();
			case RANK_TYPE_BANKROLL:
				return bankrollRankSet.first();
			case RANK_TYPE_SINGELWINBANKROLL:
				return singleWinBankrollSet.first();
			case RANK_TYPE_WINRATE:
				return winRateSet.first();
			}
			return null;
		}, scheduler);

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
	// private Map<String, Object> getMap(int type, Rank<?, ?> rank){
	// Map<String, Object> data = new HashMap<String, Object>(2);
	// data.put("rankType", type);
	// int id = rank.getUser().getId();
	// Map<String, Object> rankInfoMap = new HashMap<String, Object>(4);
	// rankInfoMap.put("id", id);
	// rankInfoMap.put("name", rank.getUser().getName());
	//
	// String portraitPrefix = (String)Config.getInstance().get("picture.IP1");
	// String upLoadPath = DataUtil.setUploadPath(portraitPrefix,
	// DataUtil.firstFolderAddress(id),
	// DataUtil.secondFolderAddress(id));
	//
	// rankInfoMap.put("portrait", upLoadPath + "/" + id +".png");
	// rankInfoMap.put("value", rank.getValue());
	// data.put("rankInfo", rankInfoMap);
	// return data;
	// }

	// @SuppressWarnings("unchecked")
	// @Override
	// public void sendFirstRankInfo(User user) {
	// Map<String, Object>[] datas = new HashMap[4];
	//
	// if (!this.winRateSet.isEmpty()) {
	// WinRateRank rank = this.firstWRank;
	// if(rank == null){
	// rank = winRateSet.first();
	// }
	//
	// // Map<String, Object> data = new HashMap<String, Object>(2);
	// // data.put("rankType", RANK_TYPE_WINRATE);
	// //
	// // Map<String, Object> rankInfo = new HashMap<String, Object>(4);
	// // rankInfo.put("id", rank.getUser().getId());
	// // rankInfo.put("name", rank.getUser().getName());
	// // rankInfo.put("portrait", rank.getUser().getPortrait());
	// // rankInfo.put("value", rank.getValue());
	// // data.put("rankInfo", rankInfo);
	//
	// datas[0] = this.getMap(RANK_TYPE_WINRATE, rank);
	// }
	//
	// if (!this.levelRankSet.isEmpty()) {
	// LevelRank rank = this.firstLRank;
	// if(rank == null){
	// rank = levelRankSet.first();
	// }
	//
	// datas[1] = this.getMap(RANK_TYPE_LEVEL, rank);
	// }
	//
	// if (!this.singelWinBankrollSet.isEmpty()) {
	// SingleWinBankrollRank rank = this.firstSRank;
	// if(rank == null){
	// rank = singelWinBankrollSet.first();
	// }
	//
	// datas[2] = this.getMap(RANK_TYPE_SINGELWINBANKROLL, rank);
	// }
	//
	// if (!this.bankrollRankSet.isEmpty()) {
	// BankrollRank rank = this.firstBRank;
	// if(rank == null){
	// rank = bankrollRankSet.first();
	// }
	//
	// datas[3] = this.getMap(RANK_TYPE_BANKROLL, rank);
	// }
	//
	// Response response = new Response(GameProtocol.S_FIRST_RANK_INFO);
	// response.setScope(ResponseScope.SPECIFIED);
	// response.setData(datas);
	// List<User> users = new ArrayList<>(1);
	// users.add(user);
	// response.setRecievers(users);
	// this.responseSender.send(response);
	// }
	
//   public static int random(int low, int hi) {
//        return (int) (low + (hi - low + 0.9) * Math.random());
//    }
//	
//	public static void main(String[] args){
//	    TreeSet<LevelRank> levelRankSet = new TreeSet<LevelRank>();
//	    Map<User, LevelRank> levelRankMap = new HashMap<User, LevelRank>();
//	    
//	    for(int idx=0; idx< 1000; idx++){
//                
//            User user = new User();
//            user.setId(1);
//            user.setName("1");
//            user.setBankroll(random(1,10000));
//            user.setLevel(random(1,100));
//            user.setCardNum(random(1,1000));
//            user.setWinCardNum(random(1,1000));
//            System.out.println(levelRankMap.containsKey(user));
//            recordRank(new LevelRank(user), levelRankSet, levelRankMap, RANK_TYPE_LEVEL);
//                    
//	    }
//	    System.out.println(levelRankSet.size()+",最后一个：" + levelRankSet.last());
//	    System.out.println(levelRankMap);
//	}
	
}
