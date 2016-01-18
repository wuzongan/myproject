package com.kunlun.poker.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.kunlun.poker.util.DataUtil;

/**
 * 继承自Comarable接口
 * 
 * @author Administrator
 */ 
@SuppressWarnings("serial")
public class Card implements Comparable<Card>, Simplifiable<Map<String, Object>>, Serializable{
	private final int face;
	private final ColorSuit suit;

	/**
	 * 构造函数 初始化牌面数据 牌面和花色
	 * 
	 * @param suit
	 * @param face
	 */
	public Card(ColorSuit suit, int face) {
		this.suit = suit;
		this.face = face;
	}

	/**
	 * 获取牌面
	 * 
	 * @return the face
	 */
	public int getFace() {
		return face;
	}

	/**
	 * 获取花色
	 * 
	 * @return the suit
	 */
	public ColorSuit getSuit() {
		return suit;
	}

	/**
	 * 比较两个牌1作为特殊处理
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Card o) {
		if(o == null)
			return 1;
		
		if (face != o.face) {
			if (face == 1)
				return 1;
			if (o.face == 1)
				return -1;

			return face - o.face;
		}

		return 0;
	}

	private String str;

	/**
	 * 返回牌面信息，包括点数显示转换
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		if (str == null) {
			char faceAlias;
			switch (face) {
			case 1:
				faceAlias = 'A';
				break;
			case 11:
				faceAlias = 'J';
				break;
			case 12:
				faceAlias = 'Q';
				break;
			case 13:
				faceAlias = 'K';
				break;
			default:
				faceAlias = 0;
			}

			if (faceAlias == 0) {
				str = suit + " " + face;
			} else {
				str = suit + " " + faceAlias;
			}
		}

		return str;
	}

	@Override
	public Map<String, Object> simplify() {
		Map<String, Object> simplified = new HashMap<>();
		simplified.put("suit", suit.simplify());
		simplified.put("face", face);
		return simplified;
	}

	public static Map<String, Object>[] simplifyAll(Card[] cards) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] simplifiedCards = new Map[cards.length];
		DataUtil.simplifyAll(cards, simplifiedCards);

		return simplifiedCards;
	}
}