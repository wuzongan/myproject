#!/bin/sh

# if [ $# != 2 ]
# then
#     echo "USAGE: ./build_android.sh PLATFORM_NAME VERSION_NUMBER"
#     exit 0
# fi
echo "=========开始打包============"
path=`pwd`/android
echo $path

platform_list=(
      "default" "all" "360" "xiaomi" "lenovo" 
      "mogoo" "uc" "youku" "sj49you" "baidu" 
      "sogou" "downjoy" "anzhi" "coolpad" "huawei" 
      "oppo" "vivo" "meizu" "wdj" "baofeng" 
      "amigo" "gfan" "mumayi" "pps" "htc" 
      "sy07073" "mopo" "pptv" "tencent" "sj49you_ysx"
      "sj49you_tyh" "letv" "sj49you_fx" "jiudu" "shandou"
      "tfgame" "koudai" "cmgame")
platform_libs=(
      "0" "0" "0" "0" "1" 
      "1" "0" "1" "1" "1" 
      "1" "1" "1" "1" "0" 
      "1" "0" "0" "1" "0" 
      "0" "1" "0" "1" "1" 
      "1" "1" "0" "1" "1"
      "1" "1" "1" "1" "0"
      "1" "1" "0")
echo "=========平台列表============"

for i in "${!platform_list[@]}"; do 
    printf "%d\t%s\n" "$i" "${platform_list[$i]}"
done

read -p "请选择平台数字(如有多个，用2,5,7,8,11或者2-10):  " m_num 
read -p "请输入版本号:  " m_version 
read -p "请输入版本code：" m_code
#修改游戏的版本号
sed -i "" "/CLIENT_VERSION/ s/\(.*=\).*/\1\"$m_version\"/" $path/../../Resources/lua/game_version_config.lua

b_lib=false
flag=0
subIndex=`awk 'BEGIN{print match("'"$m_num"'","','")}'`
if [ $subIndex -eq 0 ]; then
  subIndex=`awk 'BEGIN{print match("'"$m_num"'","'-'")}'`
  if [ $subIndex -ne 0 ]; then
    flag=2
    m_num=(${m_num//-/ })
    sub_v=$((m_num[1]-m_num[0]))  
    for (( i=0; i<=$sub_v; i++)); do
        m_num[$i]=$[m_num[0]+i]
    done
  fi
else
  flag=1
  m_num=(${m_num//,/ })
fi    
  
len=0

for element in ${m_num[@]}   
do  
    len=$[len+1]
    p_name=$p_name","${platform_list[$element]}
done  
echo "======platform_list====="
echo "$p_name" 
echo "========================"
func(){
    cd $path
    sh "makepackage_common.sh" $1 $m_version $m_code $2
}


if [ $len -gt 1 ]; then
    for file in ${m_num[@]}; do
        b_lib=false
        if [ ${platform_libs[$file]} = "1" ]; then
            b_lib=true
        fi
        file=${platform_list[$file]}
        if  [[ "$file" != "all" ]]; then
          func $file $b_lib
        fi
    done  
else
    m_platform=${platform_list[$m_num]}
    if [ $m_platform = "all" ]; then
        for i in "${!platform_list[@]}"; do 
            single_file=${platform_list[$i]}
            if  [[  "$single_file" != "default"  && "$single_file" != "all"   ]]; then
                b_lib=false
                if [ ${platform_libs[$i]} = "1" ]; then
                    b_lib=true
                fi
                func $single_file $b_lib
            fi
        done
    elif [ $m_platform = "default" ]; then
        # echo "build file name: makepackage.sh
        # cd $path
        # sh "makepackage.sh" $m_version 18 pub 
        func "debug" $b_lib
    else
        if [ ${platform_libs[$m_num]} = "1" ]; then
            b_lib=true
        fi
        func $m_platform $b_lib
    fi    
fi

echo 'success over'