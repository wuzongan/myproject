package com.kunlun.poker.game.domain;

import com.kunlun.poker.domain.Simplifiable;

/**
 *
 * @author kl
 */
public enum ShowdownState implements Simplifiable<Integer>{ 
    /**
     * 必须亮牌
     */
    REQUIRED {
        @Override
        public String toString()
        {
            return "必须亮牌";
        }

        @Override
        public Integer simplify() {
                return 1; 
        }
    },
    /**
     * 选择性亮牌
     */
    OPTIONAL{
        @Override
        public String toString()
        {
            return "选择亮牌";
        }

        @Override
        public Integer simplify() {
                return 2; 
        }
    },
    /**
     * 不亮牌
     */
    DISCARDED{
        @Override
        public String toString()
        {
            return "不亮牌";
        }

        @Override
        public Integer simplify() {
                return 3; 
        }
    }  
}
