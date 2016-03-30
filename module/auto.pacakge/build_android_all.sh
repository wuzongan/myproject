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

func(){
  apk_platform=$1
  if [[ ! -n $apk_platform ]]; then
  echo "please define apk_platform third param"
  exit 1
  fi

  echo "====================apk_platform="$apk_platform

  android_path="../android.platform/proj_$apk_platform.android"
  build_file="$android_path/build_native.sh"
  echo 'build file name:'$build_file
  sh $build_file
}

for i in "${!platform_list[@]}"; do 
    single_file=${platform_list[$i]}
    if  [[  "$single_file" != "default"  && "$single_file" != "all"   ]]; then
        func $single_file 
    fi
done




echo 'success over'