package com.kunlun.poker.back.system;

import java.text.SimpleDateFormat;

public interface Constants {

    //每次读的行数
    static final int READ_LINE_COUNT = 1000;
    
    static final String LINE_SEPARATOR =  System.getProperty("line.separator").toString();

    static final int LINE_SEPARATOR_LENGTH = LINE_SEPARATOR.getBytes().length;
    
    static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat(
            "yyyy_MM_dd");
}
