@echo off
:begin

set /p PROJECTDIR=请输入工程所在目录 

::echo qh360,uc,alipay,nd91
::set /P PLUGINS=请输入工程插件,如果多个用逗号分隔
echo PROJECTDIR  =  %PROJECTDIR% 
echo PLUGINS     =  %PLUGINS%
echo 开始部署工程
pushd .
::检测输入是否正确
:inputdir
cd %PROJECTDIR%
if not exist jni echo 工程目录错误请重新输入 & set /p PROJECTDIR=请输入工程所在目录  &goto inputdir
REM :inputplugin
REM for %%i in (%PLUGINS%) do if %%i neq qh360 (echo %%i)else if %%i neq uc (echo %%i)else if %%i neq alipay (echo %%i)else if  %%i neq nd91 ()else  (echo 插件输入错误 &&set /P PLUGINS=请输入工程插件,如果多个用逗号分隔goto inputplugin)
::修改main.cpp

for /d  %%i in (.\jni\*) do for /f "tokens=*" %%j in (%%i\main.cpp) do (
	echo %%j >> %%i\main1.cpp
	if "%%j" == "#include <android/log.h>"              (
		echo #include "PluginJniHelper.h" >>%%i\main1.cpp
	)else if "%%j" == "PluginJniHelper::setJavaVM(vm);" (
		echo PluginJniHelper::setJavaVM^(vm^); >>%%i\main1.cpp
	)
	if "%%j" == '#include "PluginJniHelper.h"' (
			del %%i\main1.cpp
	)
)
pause