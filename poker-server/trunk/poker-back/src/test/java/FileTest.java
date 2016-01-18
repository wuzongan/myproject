
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import com.kunlun.poker.back.system.FileUtil;

public class FileTest {
    public static final int LINE_BYTE_LENGTH = 100;
    public static final int READ_LING_COUNT = 1000;
    public static final String PATH = "C:\\Users\\Administrator\\Downloads\\Adobe CS4.iso";
//    public static final int PROCESS_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    public static final ForkJoinPool pool = new ForkJoinPool(2);
    public static final ExecutorService exec = Executors.newFixedThreadPool(2);
    
    public static void normalTest() throws Exception{
        long start = System.currentTimeMillis();
        try(RandomAccessFile raf = new RandomAccessFile(PATH, "r");){
            int length = (int) raf.length();
            FileChannel fileChannel = raf.getChannel();
            MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, length);
            int lineCount = length%LINE_BYTE_LENGTH==0?length/LINE_BYTE_LENGTH:length/LINE_BYTE_LENGTH + 1;         
            int startLine = 1;
            int tempEndLine = READ_LING_COUNT;
            do {
                //System.out.println("start="+startLine + ",end="+ tempEndLine);
                FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                tempEndLine += READ_LING_COUNT;
                if(tempEndLine > lineCount){
                    tempEndLine = lineCount;
                    startLine += READ_LING_COUNT;
                    //System.out.println("start="+startLine + ",end="+ tempEndLine);
                    FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                    break;
                }
                startLine += READ_LING_COUNT;
            } while (tempEndLine < lineCount);
            System.out.println("总行数："+lineCount);
        }
        long end = System.currentTimeMillis();
        System.out.println("normal耗时："+(end -start) +" 毫秒");
    }
    
    static class CallTask implements Callable<Integer>{
        private int startLine;
        private int endLine;
        private MappedByteBuffer mbb;
        public CallTask(int startLine, int endLine, MappedByteBuffer mbb) {
            this.startLine = startLine;
            this.endLine = endLine;
            this.mbb = mbb;
        }
        @Override
        public Integer call() throws Exception {
            try {
                int tempStartLine = startLine;
                int tempEndLine = startLine -1  + READ_LING_COUNT;
                do {
                    FileUtil.obtainStringInRandomAccessFile(mbb, tempStartLine, tempEndLine, LINE_BYTE_LENGTH);
                    tempEndLine += READ_LING_COUNT;
                    if(tempEndLine > endLine){
                        tempEndLine = endLine;
                        tempStartLine += READ_LING_COUNT;
                        FileUtil.obtainStringInRandomAccessFile(mbb, tempStartLine, tempEndLine, LINE_BYTE_LENGTH);
                        break;
                    }
                    tempStartLine += READ_LING_COUNT;
                } while (tempEndLine < endLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return endLine -  startLine;
        }
        
    }
    
   public static void execTest() throws Exception{
     long start = System.currentTimeMillis();
     try(RandomAccessFile raf = new RandomAccessFile(PATH, "r");){
         int length = (int) raf.length();
         FileChannel fileChannel = raf.getChannel();
         MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, length);
         int lineCount = length%LINE_BYTE_LENGTH==0?length/LINE_BYTE_LENGTH:length/LINE_BYTE_LENGTH + 1;
         int startLine = 1;
         int tempEndLine = 1000000;
         do {
             exec.submit(new CallTask(startLine, tempEndLine, mbb)).get();
             tempEndLine += 1000000;
             if(tempEndLine > lineCount){
                 tempEndLine = lineCount;
                 startLine += 1000000;
                 exec.submit(new CallTask(startLine, tempEndLine, mbb)).get();
                 break;
             }
             startLine += 1000000;
         } while (tempEndLine < lineCount);
         System.out.println("总行数："+lineCount);
     }
     long end = System.currentTimeMillis();
     System.out.println("exec并发耗时："+(end -start) +" 毫秒");
   }
    
    static class Task extends RecursiveTask<Integer>{
        private static final long serialVersionUID = 1L;
        private int startLine;
        private int endLine;
        private MappedByteBuffer mbb;
        public Task(int startLine, int endLine, MappedByteBuffer mbb) {
            this.startLine = startLine;
            this.endLine = endLine;
            this.mbb = mbb;
        }
        
        @Override
        protected Integer compute() {
            try {
                int tempStartLine = startLine;
                int tempEndLine = startLine -1  + READ_LING_COUNT;
                do {
                    FileUtil.obtainStringInRandomAccessFile(mbb, tempStartLine, tempEndLine, LINE_BYTE_LENGTH);
                    tempEndLine += READ_LING_COUNT;
                    if(tempEndLine > endLine){
                        tempEndLine = endLine;
                        tempStartLine += READ_LING_COUNT;
                        FileUtil.obtainStringInRandomAccessFile(mbb, tempStartLine, tempEndLine, LINE_BYTE_LENGTH);
                        break;
                    }
                    tempStartLine += READ_LING_COUNT;
                } while (tempEndLine < endLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return endLine - startLine;
        }
        
    }
    
    public static void forkTest() throws Exception{
        long start = System.currentTimeMillis();
        
        try(RandomAccessFile raf = new RandomAccessFile(PATH, "r");){
            int length = (int) raf.length();
            FileChannel fileChannel = raf.getChannel();
            MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, length);
            int lineCount = length%LINE_BYTE_LENGTH==0?length/LINE_BYTE_LENGTH:length/LINE_BYTE_LENGTH + 1;
            int startLine = 1;
            int tempEndLine = 3000000;
            do {
                pool.invoke(new Task(startLine, tempEndLine, mbb));
                tempEndLine += 3000000;
                if(tempEndLine > lineCount){
                    tempEndLine = lineCount;
                    startLine += 3000000;
                    pool.invoke(new Task(startLine, tempEndLine, mbb));
                    break;
                }
                startLine += 3000000;
            } while (tempEndLine < lineCount);
            System.out.println("总行数："+lineCount);
        }
        long end = System.currentTimeMillis();
        System.out.println("fork耗时："+(end -start) +" 毫秒");
    }
    
    public static void main(String[] args){
        try {
            //normalTest();
            forkTest();
            //execTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
}
