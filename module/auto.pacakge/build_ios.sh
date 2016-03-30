#!/bin/sh

path=`pwd`/ios
echo $path
echo "=========开始打包============\n"

platform_list=("default" "all" "pp" "ky" "xy" "tongbu" 
    "91" "haima" "i4" "itools" "iiapple"
    "sj49ios" "sj49ios_fx" "appstore")
echo "=========平台列表============"

for i in "${!platform_list[@]}"; do 
    printf "%d\t%s\n" "$i" "${platform_list[$i]}"
done

read -p "请选择平台数字(如有多个，用,分开。如:2,5,7,8):  " m_num 
m_platform=${platform_list[$m_num]}
read -p "请输入版本号:  " m_version 
#修改游戏的版本号
sed -i "" "/CLIENT_VERSION/ s/\(.*=\).*/\1\"$m_version\"/" $path/../../Resources/lua/game_version_config.lua

m_num=${m_num//,/ }    
len=0
echo "============================"
for element in $m_num   
do  
    len=$[len+1]
    p_name=${platform_list[$element]}
    echo $p_name
done  
echo "============================"

func(){
    echo "=============build $1 $m_version ==============="
	cd $path
	sh build_common.sh $m_version $1 
}
if [ $len -gt 1 ]; then
    for file in ${m_num[@]}; do
        file=${platform_list[$file]}
        if  [[  "$file" != "default"  && "$file" != "all"   ]]; then
          func $file
        fi
    done  
else
    if [ $m_platform = "all" ]; then
        for file in ${platform_list[@]}; do
            if  [[  "$file" != "default"  && "$file" != "all"   ]]; then
              func $file
            fi
        done
    else
        func $m_platform
    fi    
fi



echo 'success over'