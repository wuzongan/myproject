/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.game.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DFA敏感关键字过滤 公共类
 * @author zernzhou 
 */
public class ChatCheckUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatCheckUtil.class);
    private final TreeNode rootNode = new TreeNode();
    private final ByteBuffer keyWordBuffer = ByteBuffer.allocate(1024);
    private final String charset = "UTF-8";
    
    /**
     * 创建DFA
     * 主要思想是将一个关键字按照树形结构存储到list中
     * @param keyWords
     * @throws UnsupportedEncodingException 
     */
    public void  createKeyWordTree(List<String> keyWords) throws UnsupportedEncodingException{
        for(String keyWord : keyWords){
            if(keyWord == null)
                continue;
            keyWord = keyWord.trim();
            byte[] bytes = keyWord.getBytes(charset);
            //从根节点开始设置树
            TreeNode tempNode = rootNode;
            //循环字节
            for(int i=0; i<bytes.length; i++){
                int index = bytes[i] & 0xff; //将byte转换为int，防止位数错误，导致结果不对
                //根据索引站到相应的关键字
                TreeNode node = tempNode.getSubNode(index);
                if(node == null){
                    node = new TreeNode();
                    //如果node为空则向该索引增加该node节点
                    tempNode.setSubNode(index, node);
                }
                tempNode = node;
                if(i == bytes.length -1){
                    tempNode.setKeyWordEnd(true);
                    logger.debug("DFA:{}", keyWord);
                }
            }
        }
    }
    
    /**
     * 提供外部接口调用 输入的String对话内容
     * @param content
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String searchKeyWord(String content) throws UnsupportedEncodingException{
        return searchKeyWord(content.getBytes(charset));  
    }
    
    /**
     * 内部方法，真正实现关键字过滤
     * @param bytes
     * @return
     * @throws UnsupportedEncodingException 
     */
    private String searchKeyWord(byte[] bytes) throws UnsupportedEncodingException{
        StringBuilder words = new StringBuilder();
        if(bytes == null || bytes.length == 0)
            return words.toString();
        
        TreeNode tempNode = rootNode;
        int rollback = 0;    //回滚位置
        int position = 0;    //当前位置
        
        while(position < bytes.length){
            int index = bytes[position] & 0xff;
            keyWordBuffer.put(bytes[position]);
            tempNode = tempNode.getSubNode(index);
            
            if(tempNode == null){
                position = position - rollback;
                rollback = 0;
                tempNode = rootNode;
                keyWordBuffer.clear();
            }else if(tempNode.isKeyWordEnd()){
                keyWordBuffer.flip();
                String keyWord = Charset.forName(charset).decode(keyWordBuffer).toString();
                logger.debug("Find keyWord:{}", keyWord);
                keyWordBuffer.limit(keyWordBuffer.capacity());
                if(words.length() == 0){
                    words.append(keyWord);
                }else{
                     words.append(":").append(keyWord);
                }
                rollback = 1;
            }else{
                rollback++;
            }
            position++;
        }
        String result = new String(bytes, "UTF-8");
        return result;
    }
}

/**
 * 关键字树形结构
 * @author kl
 */
class TreeNode{
    private static final int NODE_LEN = 256;

    private boolean end = false;    
    private final TreeNode[] subNodes = new TreeNode[NODE_LEN];

    public TreeNode(){
    }

    public void setSubNode(int index, TreeNode node){
        subNodes[index] = node;
    }

    public TreeNode getSubNode(int index){
        return subNodes[index];
    }

    public boolean isKeyWordEnd(){
        return end;
    }

    public void setKeyWordEnd(boolean end){
        this.end = end;
    }
}

