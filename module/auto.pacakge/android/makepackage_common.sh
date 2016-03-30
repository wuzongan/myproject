#!/bin/sh

apk_platform=$1
if [[ ! -n $apk_platform ]]; then
echo "please define apk_platform third param"
exit 1
fi

apk_version=$2
if [[ ! -n $apk_version ]]; then
echo "please define apk_version first param"
exit 1
fi

apk_code=$3
if [[ ! -n $apk_code ]]; then
echo "please define apk_code first param"
exit 1
fi

echo "apk_version"=$apk_version " apk_code"=$apk_code " apk_platform"=$apk_platform

if [ $apk_platform = "debug" ]; then
	android_path="../../android.platform/proj.android"
else
	android_path="../../android.platform/proj_$apk_platform.android"
fi
build_file="$android_path/build_native.sh"
echo 'build file name:'$build_file
sh $build_file


if [ $apk_platform = "downjoy" ] || [ $apk_platform = "cmgame" ]; then
	echo 'ant build_$apk_platform ... '
	ant -buildfile build_"$apk_platform".xml -Dapk-version="$apk_version" -Dapk-code="$apk_code" -Dplatform="$apk_platform"
elif [ $apk_platform = "debug" ]; then
	echo 'ant debug ... '
	ant -buildfile build.xml -Dapk-version="$apk_version" -Dapk-code="$apk_code" -Dapk-env="$apk_env"
else
	echo 'ant build ... '
	if $4; then
		ant -buildfile build_common_lib.xml -Dapk-version="$apk_version" -Dapk-code="$apk_code" -Dplatform="$apk_platform"
	else
		ant -buildfile build_common.xml -Dapk-version="$apk_version" -Dapk-code="$apk_code" -Dplatform="$apk_platform"
	fi
fi
echo 'package over'

