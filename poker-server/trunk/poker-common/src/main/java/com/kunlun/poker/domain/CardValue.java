package com.kunlun.poker.domain;

import java.io.Serializable;
import java.util.Arrays;

public class CardValue implements Comparable<CardValue>, Serializable {
	private static final long serialVersionUID = -4339460667058543207L;

	private final PokerHand pokerHand;
	private final Card[] cards;
	private final boolean straightInnerListening;
	private int pokerHandWeightIndex;

	public CardValue(PokerHand pokerHand, Card[] cards)
	{
		this.pokerHand = pokerHand;
		this.cards = cards;
		this.straightInnerListening = false;
	}
	
	public CardValue(Card[] cards,  boolean straightInnerListening)
	{
		this.pokerHand = PokerHand.STRAIGHT;
		this.cards = cards;
		this.straightInnerListening = straightInnerListening;
	}

	public PokerHand getPokerHand() {
		return pokerHand;
	}

	public Card[] getCards() {
		return cards;
	}

	@Override
	public int compareTo(CardValue o) {
		int ret = o.pokerHand.compareTo(pokerHand);
		if (ret == 0) {
			ret = pokerHand.compare(cards, o.cards);
		}
		return ret;
	}

	public boolean isStraightInnerListening() {
		return straightInnerListening;
	}
	
	public String toString() {
		return pokerHand + "$" + straightInnerListening + ":" + Arrays.toString(cards);
	}

    public int getPokerHandWeightIndex() {
        return pokerHandWeightIndex;
    }

    public void setPokerHandWeightIndex(int pokerHandWeightIndex) {
        this.pokerHandWeightIndex = pokerHandWeightIndex;
    }
    
    public void setPokerHandWeightIndexByCom(int numberOfCommunityCards){
        if(pokerHand == PokerHand.ONE_PAIR){
            if(numberOfCommunityCards == 3){
                this.setPokerHandWeightIndex(RobotAiConstants.FIVE_ONE_PAIR);
            }else if(numberOfCommunityCards == 4){
                this.setPokerHandWeightIndex(RobotAiConstants.SIX_ONE_PAIR);
            }
        }else if(pokerHand == PokerHand.TWO_PAIR){
            if(numberOfCommunityCards == 3){
                this.setPokerHandWeightIndex(RobotAiConstants.FIVE_TWO_PAIR);
            }else if(numberOfCommunityCards == 4){
                this.setPokerHandWeightIndex(RobotAiConstants.SIX_TWO_PAIR);
            }
        }else if(pokerHand == PokerHand.THREE_OF_A_KIND){
            if(numberOfCommunityCards == 3){
                this.setPokerHandWeightIndex(RobotAiConstants.FIVE_THREE_OF_A_KIND);
            }else if(numberOfCommunityCards == 4){
                this.setPokerHandWeightIndex(RobotAiConstants.SIX_THREE_OF_A_KIND);
            }
        }else if(pokerHand == PokerHand.FOUR_OF_A_KIND){
            if(numberOfCommunityCards == 3){
                this.setPokerHandWeightIndex(RobotAiConstants.FIVE_FOUR_OF_A_KIND);
            }else if(numberOfCommunityCards == 4){
                this.setPokerHandWeightIndex(RobotAiConstants.SIX_FOUR_OF_A_KIND);
            }
        }else if(pokerHand == PokerHand.FULL_HOUSE){
            if(numberOfCommunityCards == 3){
                this.setPokerHandWeightIndex(RobotAiConstants.FIVE_FULL_HOUSE);
            }else if(numberOfCommunityCards == 4){
                this.setPokerHandWeightIndex(RobotAiConstants.SIX_FULL_HOUSE);
            }
        }
    }

	
}
