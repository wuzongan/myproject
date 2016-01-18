/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.domain;


/**
 * 牌面花色的枚举类
 * @author Administrator
 */
public enum ColorSuit implements Simplifiable<Integer>{
    /**
     * 红桃
     */
    HEART {
        @Override
        public String toString()
        {
            return "红桃";
        }

		@Override
		public Integer simplify() {
			return 1; 
		}
    },
    /**
     * 方片
     */
    DIAMOND {
        @Override
        public String toString()
        {
            return "方片";
        }

		@Override
		public Integer simplify() {
			return 2; 
		}
    },
    /**
     * 梅花
     */
    CLUB {
        @Override
        public String toString()
        {
            return "梅花";
        }

		@Override
		public Integer simplify() {
			return 3; 
		}
    },
    /**
     * 黑桃
     */
    SPADE {
        @Override
        public String toString()
        {
            return "黑桃";
        }

		@Override
		public Integer simplify() {
			return 4; 
		}
    };
    
    public static ColorSuit getColorSuitById(int id){
        switch (id) {
        case 1:
            return HEART;
        case 2:
            return DIAMOND;
        case 3:
            return CLUB;
        case 4:
            return SPADE;
        default:
            break;
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        System.out.println(ColorSuit.HEART.ordinal());
    }
}
