#!/bin/bash

#pngquant使用教程

#到https://pngquant.org安装pngquant的Command-line，如（Binary for Mac OS X ）
#设置环境
    # vi ~/.bash_profile 添加
    # export PNGQUANT_ROOT=~/Desktop/bin/pngquant(你的pngquant路径)
    # export PATH=$PNGQUANT_ROOT:$PATH
    # 保存退出，执行source ~/.bash_profile

function compress {
    local DIR=$1
    declare -a filelist
    for item in `ls $DIR` ;do
        if [ -f $DIR/$item ]  ;then
            if [ "${item:0,-4}" == ".png" ] ;then
                filelist[${#filelist[@]}]=$DIR/$item
            fi
        elif [ -d $DIR/$item ]; then
            echo $DIR/$item
            compress $DIR/$item
        fi
    done 
   # echo ${filelist[*]}
   if [ ${#filelist[*]} -gt 0 ];then
        pngquant 256 $num --ext .png ${filelist[*]} --force --skip-if-larger 
   fi
}  

echo 正在压缩 ...
compress `pwd`
echo 压缩完毕！

