package com.kunlun.poker.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    
    public static boolean isSameDay(long timeInMills1, long timeInMills2){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timeInMills1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(timeInMills2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendar objects are on the same day ignoring time.</p>
     *
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     * 
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    
    public static String formatDaybySeconde(String tablePrefixName,int second){
    	long secondLong = second;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
    	return tablePrefixName +"_"+sdf.format(secondLong*1000);
    }
    
    public static String formatDay(String tablePrefixName){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
    	return tablePrefixName +"_"+sdf.format(new java.util.Date());
    }
    
}
