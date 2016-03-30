#!/bin/sh

TargetPath=`PWD`   #脚本目录
#日期
Date=$(date +%Y-%m-%d)
#版本号 默认1.2.1
#
# version=$1
# platformname=$2		#平台
# xcodeprojFile=$3  #工程名
# infoPlistFile=$4  #info.plist
#
version=$1
platformname=$2		#平台
xcodeprojFile=xbcq_$platformname.xcodeproj  #工程名
infoPlistFile=proj.$platformname/Info_ios.plist  #info.plist

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
compiled_path="$TargetPath/../../package.apk.ipa/ipa/${platformname}"
ipa_path="$TargetPath/../../package.apk.ipa/ipa/${Date}"


echo "Project_Path"=$Project_Path
echo "Info_Path"=$Info_Path
echo "compiled_path"=$compiled_path
echo "ipa_path"=$ipa_path

if [[ -d $compiled_path ]]; then
rm -rf $compiled_path
fi
mkdir -p $compiled_path

if [[ ! -d $ipa_path ]]; then
mkdir -p $ipa_path
fi

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


xcodebuild -project $Project_Path -configuration Release -target xbcq clean

xcodebuild -project $Project_Path -configuration Release -sdk iphoneos -target xbcq "CONFIGURATION_BUILD_DIR="${compiled_path}

xcrun --sdk iphoneos PackageApplication -v ${compiled_path}"/xbcq.app" -o ${compiled_path}"/xbcq.ipa"

cp ${compiled_path}"/xbcq.ipa" $ipa_path/xbcq_${platformname}_${version}.ipa

if [[ -d $compiled_path ]]; then
rm -rf $compiled_path
fi

