#!/bin/sh

TargetPath=`PWD`   #脚本目录
#日期
Date=$(date +%m%d)
#版本号 默认1.2.1
#
# version=$1
# platformname=$2		#平台
# xcodeprojFile=$3  #工程名
# infoPlistFile=$4  #info.plist
#
version=$1
if [[ ! -n $version ]]; then
echo "version"=$version
version=1.1.2
# error( "version not define" )
fi
platformname=appstore	#平台
xcodeprojFile=chuangshiji.xcodeproj  #工程名
infoPlistFile=../proj.ios/Info.plist  #info.plist

echo "version"=$version
echo "platformname"=$platformname
echo "xcodeprojFile"=$xcodeprojFile
echo "infoPlistFile"=$infoPlistFile

if [[ ! -n $version ]]; then
echo "version"=$version
exit
# error( "version not define" )
fi

if [[ ! -n $platformname ]]; then
echo "platformname"=$platformname
exit
fi

if [[ ! -n $xcodeprojFile ]]; then
echo "xcodeprojFile"=$xcodeprojFile
exit
# error("xcodeprojFile not define")
fi

if [[ ! -n $infoPlistFile ]]; then
echo "infoPlistFile"=$infoPlistFile
exit
# error("infoPlistFile not define")
fi

Project_Path="$TargetPath/../../proj.ios/$xcodeprojFile"
Info_Path="$TargetPath/../../ios.platform/$infoPlistFile"
compiled_path="$TargetPath/../../package.apk.ipa/ipa/builded/${platformname}"
ipa_path="$TargetPath/../../package.apk.ipa/ipa/${version}/${platformname}"


echo "Project_Path"=$Project_Path
echo "Info_Path"=$Info_Path
echo "compiled_path"=$compiled_path
echo "ipa_path"=$ipa_path

if [[ -d $compiled_path ]]; then
rm -rf $compiled_path
fi
mkdir -p $compiled_path
mkdir -p $ipa_path

# exit

# #改 version
linebefore=`cat -n $Info_Path | grep "<key>CFBundleVersion</key>" | awk '{print $1}'`
line=$[linebefore+1]
ext=${line}"s/>.*</>$version</g"
sed -i "" $ext $Info_Path
# #改 version
linebefore=`cat -n $Info_Path | grep "<key>CFBundleShortVersionString</key>" | awk '{print $1}'`
line=$[linebefore+1]
ext=${line}"s/>.*</>$version</g"
sed -i "" $ext $Info_Path


xcodebuild -project $Project_Path -configuration Release -target chuangshiji clean

xcodebuild -project $Project_Path -configuration Release -sdk iphoneos -target chuangshiji "CONFIGURATION_BUILD_DIR="${compiled_path}

xcrun --sdk iphoneos PackageApplication -v ${compiled_path}"/chuangshiji.app" -o ${compiled_path}"/chuangshiji.ipa"

cp ${compiled_path}"/chuangshiji.ipa" $ipa_path/xbcq_${platformname}_${version}_${Date}.ipa

