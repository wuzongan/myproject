package com.kunlun.poker.domain;

import com.kunlun.poker.util.ArrayUtil;
import com.kunlun.poker.util.RandomUtil;

/**
 *
 * @author Administrator
 */
public class Deck {
	/**
	 * 最大牌面为13，A1，J11，Q12，K13
	 */
	public static final int FACE_NUMBER = 13;
	/**
	 * 牌面，即所有的牌
	 */
	private static final Card[] TEMPLATE;

	/**
	 * 皇家德州 去掉 2-9牌面
	 */
	public static final int ROYAL_FACE_NUMBER = 5;
	/**
	 * 皇家德州 所有牌面
	 */
	private static final Card[] ROYAL_TEMPLATE;

	static {
		ColorSuit[] suits = ColorSuit.values();
		int numberOfSuits = suits.length;

		TEMPLATE = new Card[numberOfSuits * FACE_NUMBER];
		int i = 0;
		for (ColorSuit suit : suits) {
			for (int j = 1; j <= FACE_NUMBER; j++) {
				TEMPLATE[i] = new Card(suit, j);
				i++;
			}
		}

		ROYAL_TEMPLATE = new Card[numberOfSuits * ROYAL_FACE_NUMBER];
		int[] temp = new int[] { 1, 10, 11, 12, 13 };
		i = 0;
		for (ColorSuit suit : suits) {
			for (int idx = 0; idx < ROYAL_FACE_NUMBER; idx++) {
				ROYAL_TEMPLATE[i] = new Card(suit, temp[idx]);
				i++;
			}
		}
	}

	private final Card[] cards;
	private int position = 0;

	/**
	 * 构造函数，初始化一套牌
	 */
	public Deck(boolean isRoyal) {
		if (isRoyal) {
			cards = ROYAL_TEMPLATE.clone();
		} else {
			cards = TEMPLATE.clone();
		}
	}

	public Card getByOffset(int offset)
	{
		return cards[position + offset];
	}
	
	/**
	 * 发牌操作
	 * 
	 * @return
	 */
	public Card shift() {
		return cards[position++];
	}

	/**
	 * 洗牌操作
	 */
	public void shuffle() {
		for (int i = 0, length = cards.length; i < length; i++) {
			//ArrayUtil.each(cards, i, (int) Math.floor(Math.random() * length));
		    //ArrayUtil.each(cards, i, ThreadLocalRandom.current().nextInt(length));
		    ArrayUtil.each(cards, i, RandomUtil.nextInt(length));
		}
		
		position = 0;
	}
	
	public static void main(String[] args) {
		Deck deck = new Deck(false);
		long start = System.currentTimeMillis();
		deck.shuffle();
		System.out.println(System.currentTimeMillis() - start);
	}
}
