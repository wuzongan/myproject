package com.kunlun.poker.back.system;

import java.io.File;
import java.nio.MappedByteBuffer;

public class FileUtil {

    public static boolean isExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static int getMiddleTime(MappedByteBuffer mbb, int lineCount,
            int columnCount, int byteLength) throws Exception {
        mbb.position((lineCount-1) * byteLength);
        byte[] bytes = new byte[byteLength];
        //file.read(bytes, 0, byteLength);
        mbb.get(bytes);
        String str = new String(bytes);
        String[] strArr = str.split(",");
        return Integer.valueOf(strArr[columnCount - 1].trim());
    }

    public static int binarySearch(MappedByteBuffer mbb, int targetTime,
            int totalLineCount, int columnCount, int lineByteLength,
            int direction) throws Exception {
        int tempTotal = totalLineCount;
        int start = 0;
        int tempLine = -1;
        while (start <= totalLineCount) {
            int middle = (start + totalLineCount) / 2;
            int middleParamter;
            if(middle==0){
                middleParamter = getMiddleTime(mbb, 1, columnCount,
                        lineByteLength);
            }else{
            	middleParamter = getMiddleTime(mbb, middle, columnCount, lineByteLength);
            }

            if (targetTime == middleParamter) {
                if(middle == 0){
                    return 1;
                }else if(middle == 1 || middle >= tempTotal){
            		return middle;
            	}else if(middle > 0){
            		if (direction == 8) {
            			while (getMiddleTime(mbb, middle = middle - 1,
            					columnCount, lineByteLength) == targetTime) {
            			    if(middle <= 0){
            			        return middle;
            			    }
            			}
            			return middle+1;
            		} else if (direction == 2) {
            			while (getMiddleTime(mbb, middle = middle + 1,
            					columnCount, lineByteLength) == targetTime) {
            			    if(middle >= tempTotal){
            			        return middle;
            			    }
            			}
            			return middle-1;
            		}
            	}
            } else if (targetTime < middleParamter) {
                totalLineCount = middle - 1;
            } else {
                start = middle + 1;
            }

            tempLine = middle;
        }
        return tempLine==0?tempLine+1:tempLine;
    }
    
    public static String obtainStringInRandomAccessFile(MappedByteBuffer mbb,
            int startIndex, int endIndex, int byteLength) throws Exception {
        int length = (endIndex - startIndex + 1) * byteLength;
//        System.out.println(startIndex);
        mbb.position((startIndex - 1) * byteLength);

        if (length == 0) {
            return null;
        }

        byte[] bytes = new byte[length];
        //file.read(bytes, 0, length);
        mbb.get(bytes);
        String str = new String(bytes);
        return str;
    }

}
