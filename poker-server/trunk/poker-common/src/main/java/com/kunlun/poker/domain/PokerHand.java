package com.kunlun.poker.domain;

import java.util.ArrayList;
import java.util.List;

import com.kunlun.poker.util.ArrayUtil;

/**
 *
 * @author Administrator
 */
public enum PokerHand {

	/**
	 * 同花顺
	 */
	STRAIGHT_FLUSH {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int number = 0;
			Card ace = null;
			Card prevCard = null;

			for (Card card : cards) {
				if (card.getSuit() != colorSuit)
					continue;

				int face = card.getFace();

				if (prevCard != null && (prevCard.getFace() != 1 || face != 13)
						&& face != prevCard.getFace() - 1) {
					number = 0;
				}

				ref[number++] = card;
				prevCard = card;

				if (face == 1) {
					ace = card;
				} else if (number == 4 && ref[0].getFace() == 5 && ace != null) {
					ref[number++] = ace;
				}

				if (number == 5)
					return true;
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 四条
	 */
	FOUR_OF_A_KIND {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int number = 0, i, length;

			for (i = 0, length = cards.length; i < length; i++) {
				Card card = cards[i];
				if (number == 4) {
					break;
				}

				if (number > 0 && ref[number - 1].getFace() != card.getFace()) {
					number = 0;
				}

				ref[number++] = card;
			}

			if (number == 4) {
				if (number != i) {
					ref[number] = cards[0];
				} else {
					ref[number] = cards[i];
				}

				return true;
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0, 4 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 葫芦
	 */
	FULL_HOUSE {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int numberOfThree = 0;
			int numberOfTwo = 0;
			boolean twoDone = false;

			for (Card card : cards) {
				int face = card.getFace();
				if (!twoDone) {
					if (numberOfTwo < 2) {
						if (numberOfTwo == 1
								&& ref[numberOfTwo + 2].getFace() != face) {
							numberOfTwo = 0;
						}

						ref[3 + numberOfTwo] = card;
						numberOfTwo++;
					} else if (numberOfThree < 3
							&& ref[numberOfTwo + 2].getFace() == face) {
						System.arraycopy(ref, 3, ref, 0, 2);
						ref[3] = card;
						numberOfThree = 3;
						numberOfTwo = 0;
					} else {
						twoDone = true;
					}
				}

				if (twoDone) {
					if (numberOfThree == 3) {
						return true;
					}

					if (numberOfThree > 0
							&& ref[numberOfThree - 1].getFace() != face) {
						numberOfThree = 0;
					}

					ref[numberOfThree++] = card;
				}
			}

			return (twoDone || numberOfTwo == 2) && numberOfThree == 3;
		}

		private final int[] comparisonIndexes = new int[] { 0, 3 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 同花
	 */
	FLUSH {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int number = 0;
			for (Card card : cards) {
				if (card.getSuit() == colorSuit) {
					ref[number++] = card;

					if (number == 5) {
						return true;
					}
				}
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0, 1, 2, 3, 4 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 顺子
	 */
	STRAIGHT {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int number = 0;
			Card ace = null;
			Card prevCard = null;
			for (Card card : cards) {
				if (prevCard != null && card.getFace() == prevCard.getFace())
					continue;

				int face = card.getFace();

				if (prevCard != null && (prevCard.getFace() != 1 || face != 13)
						&& face != prevCard.getFace() - 1) {
					number = 0;
				}

				ref[number++] = card;
				prevCard = card;

				if (face == 1) {
					ace = card;
				} else if (number == 4 && ref[0].getFace() == 5 && ace != null) {
					ref[number++] = ace;
				}

				if (number == 5)
					return true;
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 三条
	 * 
	 * 听葫芦、四条
	 */
	THREE_OF_A_KIND {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int number = 0, i, length;

			for (i = 0, length = cards.length; i < length; i++) {
				Card card = cards[i];
				if (number == 3) {
					break;
				}

				if (number > 0 && ref[number - 1].getFace() != card.getFace()) {
					number = 0;
				}

				ref[number++] = card;
			}

			if (number == 3) {
				for (int j = 0; j < 2; j++) {
					if (number < i) {
						ref[number] = cards[j];
					} else {
						ref[number] = cards[number];
					}
					number++;
				}

				return true;
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0, 3, 4 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			return new CardValue[] {
					new CardValue(FOUR_OF_A_KIND, new Card[] { cards[0],
							cards[1], cards[2], null, cards[3] }),
					new CardValue(FULL_HOUSE, new Card[] { cards[0], cards[1],
							cards[2], cards[3], null }) };
		}
	},
	/**
	 * 两对
	 * 
	 * 听葫芦
	 */
	TWO_PAIR {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int numberOfPairs = 0, numberInPair = 0, index1 = -1, index2 = -1;

			Card prevCard = null;
			for (int i = 0, length = cards.length; i < length; i++) {
				Card card = cards[i];
				if (numberInPair > 0 && prevCard.getFace() != card.getFace()) {
					numberInPair = 0;
				}

				ref[(numberOfPairs << 1) + numberInPair] = card;
				prevCard = card;
				numberInPair++;

				if (numberInPair == 2) {
					numberOfPairs++;
					numberInPair = 0;
					prevCard = null;

					if (numberOfPairs == 1) {
						index1 = i - 1;
					} else {
						index2 = i - 1;
						break;
					}
				}
			}

			if (numberOfPairs == 2) {
				if (index1 > 0) {
					ref[4] = cards[0];
				} else if (index2 > 2) {
					ref[4] = cards[2];
				} else {
					ref[4] = cards[4];
				}

				return true;
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0, 2, 4 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			return new CardValue[] {
					new CardValue(FOUR_OF_A_KIND, new Card[] { cards[0],
							cards[1], cards[2], null, cards[3] }),
					new CardValue(FULL_HOUSE, new Card[] { cards[0], cards[1],
							cards[2], cards[3], null }) };
		}
	},
	/**
	 * 一对 听三条、两对
	 */
	ONE_PAIR {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			int number = 0, i, length;

			for (i = 0, length = cards.length; i < length; i++) {
				Card card = cards[i];
				if (number == 2) {
					break;
				}

				if (number > 0 && ref[number - 1].getFace() != card.getFace()) {
					number = 0;
				}

				ref[number++] = card;
			}

			if (number == 2) {
				for (int j = 0; j < 3; j++) {
					if (number < i) {
						ref[number] = cards[j];
					} else {
						ref[number] = cards[number];
					}

					number++;
				}

				return true;
			}

			return false;
		}

		private final int[] comparisonIndexes = new int[] { 0, 2, 3, 4 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			return new CardValue[] {
					new CardValue(THREE_OF_A_KIND, new Card[] { cards[0],
							cards[1], null, cards[2], cards[3] }),
					new CardValue(TWO_PAIR, new Card[] { cards[0], cards[1],
							cards[2], null, cards[3] }) };
		}
	},
	/**
	 * 散牌 听一对
	 */
	HIGH_CARD {
		@Override
		protected boolean validate(Card[] cards, Card[] ref, ColorSuit colorSuit) {
			System.arraycopy(cards, 0, ref, 0, 5);
			return true;
		}

		private final int[] comparisonIndexes = new int[] { 0, 1, 2, 3, 4 };

		@Override
		protected int[] getComparisonIndexes() {
			return comparisonIndexes;
		}

		@Override
		protected CardValue[] doListen(Card[] cards) {
			return new CardValue[] { new CardValue(ONE_PAIR, new Card[] {
					cards[0], null, cards[1], cards[2], cards[3] }) };
		}
	};
	
	protected abstract boolean validate(Card[] cards, Card[] ref,
			ColorSuit colorSuit);

	protected abstract int[] getComparisonIndexes();

	protected abstract CardValue[] doListen(Card[] cards);

	public int compare(Card[] cards1, Card[] cards2) {
		for (int i : getComparisonIndexes()) {
			int ret;
			if (cards1[i] == null)
				if (cards2[i] == null)
					ret = 0;
				else
					ret = -1;
			else
				ret = cards1[i].compareTo(cards2[i]);

			if (ret != 0) {
				return ret;
			}
		}

		return 0;
	}

	public static CardValue determine(Card[] cards, Card[] communityCards) {
		return determine(cards, communityCards, communityCards.length, null);
	}

	public static List<CardValue> listen(Card[] cards, Card[] communityCards,
			int numberOfCommunityCards) {
	    if(cards[0] == null){
	        return null;
	    }
		if(numberOfCommunityCards == 0)
			return listenStartingHand(cards);

		List<CardValue> listenings = new ArrayList<>();
		if (numberOfCommunityCards == 0) {
			listenings = listenStartingHand(cards);
		} else if (numberOfCommunityCards > 0) {
			determine(cards, communityCards, numberOfCommunityCards, listenings);
		}
		return listenings;
	}

	private static List<CardValue> listenStartingHand(Card[] cards) {
		List<CardValue> listenings = new ArrayList<>();
		int ret = cards[0].compareTo(cards[1]);
		if (ret == 0) {

			listenings.add(new CardValue(ONE_PAIR, new Card[] { cards[0],
					cards[1], null, null, null }));

			CardValue cardValue = new CardValue(THREE_OF_A_KIND, new Card[] {
					cards[0], cards[1], null, null, null });
			cardValue
					.setPokerHandWeightIndex(RobotAiConstants.TWO_THREE_OF_A_KIND);
			listenings.add(cardValue);
		} else {
			Card card1, card2;
			if (ret > 0 && (cards[0].getFace() != 1 || cards[1].getFace() != 2)) {
				card1 = cards[0];
				card2 = cards[1];
			} else {
				card1 = cards[1];
				card2 = cards[0];
			}

			listenings.add(new CardValue(HIGH_CARD, new Card[] { card1, card2,
					null, null, null }));

			CardValue cardValue = new CardValue(ONE_PAIR, new Card[] {
					cards[0], null, cards[1], null, null });
			cardValue.setPokerHandWeightIndex(RobotAiConstants.TWO_ONE_PAIR);
			listenings.add(cardValue);

			if ((cards[0].getFace() == 1 && cards[1].getFace() == 13)
					|| cards[0].getFace() - cards[1].getFace() == 1) {
				cardValue = new CardValue(STRAIGHT, new Card[] { card1, card2,
						null, null, null });
				cardValue
						.setPokerHandWeightIndex(RobotAiConstants.TWO_STRAIGHT);
				listenings.add(cardValue);
			}

			if (cards[0].getSuit() == cards[1].getSuit()) {
				cardValue = new CardValue(FLUSH, new Card[] { card1, card2,
						null, null, null });
				cardValue.setPokerHandWeightIndex(RobotAiConstants.TWO_FLUSH);
				listenings.add(cardValue);
			}
		}

		return listenings;
	}

	private static CardValue determine(Card[] cards, Card[] communityCards,
			int numberOfCommunityCards, List<CardValue> listenings) {
		Card[] auxiliaryCards = new Card[cards.length + numberOfCommunityCards];
		System.arraycopy(cards, 0, auxiliaryCards, 0, cards.length);
		System.arraycopy(communityCards, 0, auxiliaryCards, cards.length,
				numberOfCommunityCards);

		sortCardBySuit(auxiliaryCards);
		ColorSuit colorSuit = auxiliaryCards[numberOfCommunityCards / 2]
				.getSuit();

		ArrayUtil.selectionSort(auxiliaryCards, 0, cards.length
				+ numberOfCommunityCards);
		ArrayUtil.reverse(auxiliaryCards);

		PokerHand finalHand = HIGH_CARD;
		Card[] keyCards = new Card[5];
		for (PokerHand hand : values()) {
			if (hand.validate(auxiliaryCards, keyCards, colorSuit)) {
				finalHand = hand;
				break;
			}
		}

		CardValue cardValue = new CardValue(finalHand, keyCards);

		if (listenings != null) {
			listenings.add(cardValue);
			
			CardValue[] cardValues;
			if (numberOfCommunityCards < communityCards.length && (cardValues = finalHand.doListen(keyCards)) != null) {
				for (CardValue doListening : cardValues) {
					listenings.add(doListening);
					doListening
							.setPokerHandWeightIndexByCom(numberOfCommunityCards);
				}

				CardValue listening = listenFlush(auxiliaryCards, colorSuit);
				if (listening != null) {
					listenings.add(listening);
					listening
							.setPokerHandWeightIndexByCom(numberOfCommunityCards);
				}

				listening = listenStright(auxiliaryCards);
				if (listening != null) {
					listenings.add(listening);
					listening
							.setPokerHandWeightIndexByCom(numberOfCommunityCards);
				}

			}
		}

		return cardValue;
	}

	private static CardValue listenFlush(Card[] cards, ColorSuit colorSuit) {
		Card[] ref = new Card[5];
		int number = 0;
		for (Card card : cards) {
			if (card.getSuit() == colorSuit) {
				ref[number++] = card;

				if (number == 4) {
					return new CardValue(FLUSH, ref);
				}
			}
		}

		return null;
	}

	private static CardValue listenStright(Card[] cards) {
		int innerListeningIndex = -1;

		Card[] ref = new Card[5];
		int number = 0;
		Card ace = null;
		Card prevCard = null;
		for (Card card : cards) {
			if (prevCard != null && card.getFace() == prevCard.getFace())
				continue;

			int face = card.getFace();
			int diff;

			if (prevCard != null && (prevCard.getFace() != 1 || face != 13)
					&& (diff = prevCard.getFace() - face) != 1) {
				if (diff == 2) {
					if (innerListeningIndex != -1) {
						number -= innerListeningIndex + 1;
						try
						{
							System.arraycopy(ref, innerListeningIndex + 1, ref, 0,
									number);
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							e.printStackTrace();
						} 
					}	

					innerListeningIndex = number;
					ref[number++] = null;
				} else {
					number = 0;
					innerListeningIndex = -1;
				}
			}

			ref[number++] = card;
			prevCard = card;

			if (face == 1) {
				ace = card;
			} else if (ace != null
					&& (ref[0].getFace() == 4 && number == 4)
					|| (innerListeningIndex == -1 && ref[0].getFace() == 5 && number == 3)) {
				if (number == 3) {
					innerListeningIndex = number;
					number++;
				}
				ref[number++] = ace;
			}

			if ((innerListeningIndex == -1 && number == 4) || number == 5)
				return new CardValue(ref, innerListeningIndex != -1);
		}

		return null;
	}

	public static CardValue determinOmaha(Card[] cards, Card[] communityCards) {
		int handLength = cards.length;
		int commLength = communityCards.length;

		int[] commIndexes = { 0, 1, 2 };
		Card[] tmpCommCards = new Card[3];

		int[] handIndexes = new int[2];
		Card[] tmpHandCards = new Card[2];

		CardValue maxCardValue = null;

		do {
			tmpCommCards[0] = communityCards[commIndexes[0]];
			tmpCommCards[1] = communityCards[commIndexes[1]];
			tmpCommCards[2] = communityCards[commIndexes[2]];

			handIndexes[0] = 0;
			handIndexes[1] = 1;
			do {
				tmpHandCards[0] = cards[handIndexes[0]];
				tmpHandCards[1] = cards[handIndexes[1]];

				CardValue cardValue = determine(tmpHandCards, tmpCommCards);

				if (maxCardValue == null
						|| maxCardValue.compareTo(cardValue) < 0) {
					maxCardValue = cardValue;
				}
			} while (ArrayUtil.nextCombinationIndexes(handIndexes, handLength));
		} while (ArrayUtil.nextCombinationIndexes(commIndexes, commLength));

		return maxCardValue;
	}

	private static void sortCardBySuit(Card[] cards) {
	    try {
	        for (int sorted = 0; sorted < cards.length - 1; sorted++) {
	            int smallist = sorted;
	            
	            for (int j = sorted + 1; j < cards.length; j++) {
	                if (cards[smallist].getSuit().compareTo(cards[j].getSuit()) > 0) {
	                    smallist = j;
	                }
	            }
	            
	            ArrayUtil.each(cards, sorted, smallist);
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public static PokerHand getPokerHand(int index) {
		if (index == FLUSH.ordinal()) {
			return FLUSH;
		} else if (index == FOUR_OF_A_KIND.ordinal()) {
			return FOUR_OF_A_KIND;
		} else if (index == FULL_HOUSE.ordinal()) {
			return FULL_HOUSE;
		} else if (index == HIGH_CARD.ordinal()) {
			return HIGH_CARD;
		} else if (index == ONE_PAIR.ordinal()) {
			return ONE_PAIR;
		} else if (index == STRAIGHT.ordinal()) {
			return STRAIGHT;
		} else if (index == STRAIGHT_FLUSH.ordinal()) {
			return STRAIGHT_FLUSH;
		} else if (index == THREE_OF_A_KIND.ordinal()) {
			return THREE_OF_A_KIND;
		} else if (index == TWO_PAIR.ordinal()) {
			return TWO_PAIR;
		}
		return null;
	}

	public static void main(String[] args) {
		Deck deck = new Deck(false);
		
		for(int i = 0; i < 100000; i ++)
		{
			deck.shuffle();

			Card[] cards = new Card[]{
					deck.shift(),
					deck.shift(),
					deck.shift(),
					deck.shift(),
					deck.shift(),
					deck.shift()
			};
			ArrayUtil.selectionSort(cards);
			ArrayUtil.reverse(cards);

			
			CardValue value = listenStright(cards);
			
			if(value != null)
			{
				System.out.println(value);
			}
		}
	}
}
