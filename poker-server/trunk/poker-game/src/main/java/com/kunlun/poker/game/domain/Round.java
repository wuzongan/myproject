package com.kunlun.poker.game.domain;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.Deck;
import com.kunlun.poker.domain.Simplifiable;

/**
 * 枚举Round类型，round不同情况下，逻辑不统一
 *
 * @author kl
 */
public enum Round implements Simplifiable<Integer>{

    /**
     * 前翻牌圈 公共牌出现以前的第一轮叫注
     */
    PRE_FLOP {
                @Override
                public Round next() {
                    return FLOP;
                }

                @Override
                public void preprocess(Desk desk) {
                    Deck deck = desk.getDeck();
                    Seat smallBlind = desk.getSmallBlind();

                    deck.shuffle();

            		int startingHandCards = desk.getRoom().getGameType().getNumberOfStartingHands();
                    for (int i = 0; i < startingHandCards; i++) {
                        Seat seat = smallBlind;
                        do {
                            seat.getStartingHand()[i] = deck.shift();
                            seat = desk.getNextPlayingSeat(seat.getIndex());
                        } while (seat != smallBlind);
                    }

                    logger.debug("发牌:");
                    Seat seat = smallBlind;
                    do {
                        logger.debug(seat + "手牌为" + Arrays.toString(seat.getStartingHand()));
                        seat = desk.getNextPlayingSeat(seat.getIndex());
                    } while (seat != smallBlind);
                }

                @Override
                public String toString() {
                    return "翻牌前"; //To change body of generated methods, choose Tools | Templates.
                }

				@Override
				public int getDealTimesRemain() {
					return 3;
				}

				@Override
				public Integer simplify() {
					return 1; 
				}
            },
    /**
     * 翻牌 首三张公共牌出现以后的押注圈
     */
    FLOP {
                @Override
                public Round next() {
                    return TURN;
                }

                @Override
                public void preprocess(Desk desk) {
                    Card[] communityCards = desk.getCommunityCards();
                    Deck deck = desk.getDeck();

                    for (int i = 0; i < 3; i++) {
                        communityCards[i] = deck.shift();
                    }

                    logger.debug("公共牌" + Arrays.toString(communityCards));
                }

                @Override
                public String toString() {
                    return "翻牌"; //To change body of generated methods, choose Tools | Templates.
                }

				@Override
				public int getDealTimesRemain() {
					return 2;
				}

				@Override
				public Integer simplify() {
					return 2; 
				}

            },
    /**
     * 转牌 第四张公共牌出现以后的押注圈
     */
    TURN {
                @Override
                public Round next() {
                    return RIVER;
                }

                @Override
                public void preprocess(Desk desk) {
                    
                    Card[] communityCards = desk.getCommunityCards();
                    communityCards[3] = desk.getDeck().shift();

                    logger.debug("公共牌" + Arrays.toString(communityCards));
                }

                @Override
                public String toString() {
                    return "转牌"; //To change body of generated methods, choose Tools | Templates.
                }

				@Override
				public int getDealTimesRemain() {
					return 1;
				}


				@Override
				public Integer simplify() {
					return 3; 
				}
            },
    /**
     * 河牌 第五张公共牌出现以后 , 也即是摊牌以前的押注圈
     */
    RIVER {
                @Override
                public Round next() {
                    return SHOWDOWN;
                }

                @Override
                public void preprocess(Desk desk) {
                    Card[] communityCards = desk.getCommunityCards();
                    communityCards[4] = desk.getDeck().shift();

                    logger.debug("公共牌" + Arrays.toString(communityCards));
                }

                @Override
                public String toString() {
                    return "河牌"; //To change body of generated methods, choose Tools | Templates.
                }

				@Override
				public int getDealTimesRemain() {
					return 0;
				}

				@Override
				public Integer simplify() {
					return 4; 
				}

            },
    /**
     * 比牌 在最后一圈押注以后仍没有人放弃,大家就得当面“摊牌”,把底牌亮出来比个高下。
     */
    SHOWDOWN {
                @Override
                public Round next() {
                    return null;
                }

                @Override
                public void preprocess(Desk desk) {
                    desk.divideStake();
                }

                @Override
                public String toString() {
                    return "比牌"; //To change body of generated methods, choose Tools | Templates.
                }

				@Override
				public int getDealTimesRemain() {
					return 0;
				}

				@Override
				public Integer simplify() {
					return 5; 
				}
            };

    private static final Logger logger = LoggerFactory.getLogger(Round.class);

    abstract public Round next();

    abstract public void preprocess(Desk desk);

    abstract public int getDealTimesRemain();
}
