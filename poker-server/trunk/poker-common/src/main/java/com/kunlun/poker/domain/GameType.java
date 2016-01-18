package com.kunlun.poker.domain;

public enum GameType {
	NORMAL {
		@Override
		public int getNumberOfStartingHands() {
			return 2;
		}
	},
	OMAHA {
		@Override
		public int getNumberOfStartingHands() {
			return 4;
		}
	},
	ROYAL  {
		@Override
		public int getNumberOfStartingHands() {
			return 2;
		}
	};

	abstract public int getNumberOfStartingHands();
	
	public static GameType getGameTypeByIndex(int index){
	    switch (index) {
        case 0:
            return NORMAL;
        case 1:
            return OMAHA;
        case 2:
            return ROYAL;
        default:
            break;
        }
	    return null;
	}
}
