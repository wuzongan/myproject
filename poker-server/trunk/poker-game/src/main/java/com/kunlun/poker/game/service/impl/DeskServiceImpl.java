package com.kunlun.poker.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.service.DeskService;
import com.kunlun.poker.game.service.PlayerService;
import com.kunlun.poker.game.service.RobotService;
import com.kunlun.poker.game.service.RoomService;

@Component("deskService")
public class DeskServiceImpl implements DeskService {
	public DeskServiceImpl() {
		desks = new ArrayList<>();
	}

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private RoomService roomService;
	@Autowired
	private RobotService robotService;

	public final List<Desk> desks;

	/**
	 * 选桌子,忽略当前桌子
	 * 
	 * @param igorn
	 * @return
	 */
	@Override
	public Desk fetchAndLockDesk(Desk igorn) {
		Room room = roomService.getRoom();
		Desk selectedDesk = null;
		int selectedNumberOfSittings = 0;
		for (Desk desk : desks) {
			int numberOfSittings;

			if (desk != igorn
					&& (numberOfSittings = desk.getNumberOfSittings()) < room
							.getType()
					&& (selectedDesk == null || (numberOfSittings != 0 && (selectedNumberOfSittings == 0 || numberOfSittings < selectedNumberOfSittings)))
					&& desk.lock()) {

				if (selectedDesk != null) {
					selectedDesk.unlock();
				}

				selectedDesk = desk;
				selectedNumberOfSittings = numberOfSittings;
			}
		}

		if (selectedDesk == null && desks.size() < room.getDeskCapacity()) {
			selectedDesk = new Desk(room);
			selectedDesk.setScheduler(scheduler);
			selectedDesk.setPlayerService(playerService);
			selectedDesk.setRobotService(robotService);
			selectedDesk.lock();
			desks.add(selectedDesk);
		}

		return selectedDesk;
	}

	/**
	 * 选桌子
	 * 
	 * @return
	 */
	@Override
	public Desk fetchAndLockDesk() {
		return fetchAndLockDesk(null);
	}

	public List<Desk> getDesks() {
		return new ArrayList<Desk>(desks);
	}

	@Override
	public void disposeAll() {
		for (Desk desk : desks) {
			desk.dispose();
		}
	}
}
