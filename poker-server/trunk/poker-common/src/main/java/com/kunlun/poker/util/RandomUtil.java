package com.kunlun.poker.util;

import java.util.Random;

public class RandomUtil {
    
    private static final Random random = new Random();
    
    public static int nextInt(int num){
        return random.nextInt(num);
    }

    public static int random(int low, int hi) {
        return (int) (low + (hi - low + 0.9) * Math.random());
    }
  
    public static float random(float low, float hi) {
        return (float) (low + (hi - low) * Math.random());
     }
  
    public static boolean shake(float shakeNum) {
        if (shakeNum >= 1) {
            return true;
        }
        if (shakeNum <= 0) {
           return false;
        }
        double a = Math.random();
        return a < shakeNum;
    }
  
}
