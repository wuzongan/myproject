@echo off
set DIR=%~dp0
set PLUGIN_TOOT=%DIR%..\
set PUBLISH_DIR=%PLUGIN_TOOT%publish
set ANT="E:\workspace\android\eclipse_android\plugins\org.apache.ant_1.8.3.v201301120609\bin\ant.bat"
echo - config:
echo   ANDROID_NDK_ROOT    = %ANDROID_NDK_ROOT%
echo   ANDROID_SDK_ROOT    = %ANDROID_SDK_ROOT%
echo   QUICK_COCOS2DX_ROOT = %QUICK_COCOS2DX_ROOT%
echo   COCOS2DX_ROOT       = %COCOS2DX_ROOT%
echo   PLUGIN_TOOT         =%PLUGIN_TOOT%
echo   PUBLISH_DIR         =%PUBLISH_DIR%
echo   ANT                 =%ANT%
rem qh360,uc,alipay,nd91,
set plugins=4399
::编译 libPluginProtocolStatic
textcolor yellow  
echo Now build libPluginProtocolStatic
textcolor
rmdir /s /q %PLUGIN_TOOT%protocols\proj.android\obj
call "%ANDROID_NDK_ROOT%\ndk-build" -C %PLUGIN_TOOT%protocols\proj.android\jni  NDK_PROJECT_PATH=%PLUGIN_TOOT%protocols\proj.android 
if errorlevel 0 (
	textcolor purple
	echo libPluginProtocolStatic build successfull
	textcolor 
)else textcolor red &echo libPluginProtocolStatic build  failed

:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if exist "%PUBLISH_DIR%\protocols\android\lib\armeabi" rmdir /s /q "%PUBLISH_DIR%\protocols\android\lib\armeabi"
mkdir "%PUBLISH_DIR%\protocols\android\lib\armeabi"

copy  %PLUGIN_TOOT%protocols\proj.android\obj\local\armeabi\libPluginProtocolStatic.a  %PUBLISH_DIR%\protocols\android\lib\armeabi

:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if exist "%PUBLISH_DIR%\protocols\android\lib\armeabi-v7a" rmdir /s /q "%PUBLISH_DIR%\protocols\android\lib\armeabi-v7a"
mkdir "%PUBLISH_DIR%\protocols\android\lib\armeabi-v7a"

copy  %PLUGIN_TOOT%protocols\proj.android\obj\local\armeabi-v7a\libPluginProtocolStatic.a  %PUBLISH_DIR%\protocols\android\lib\armeabi-v7a
xcopy /Q /S /Y %PLUGIN_TOOT%protocols\include\*.*   %PUBLISH_DIR%\protocols\include

textcolor green
echo -------------------------------------------------
textcolor

::发布 protocols protocols
textcolor yellow
echo Now publish protocols
textcolor
pushd .
cd %PLUGIN_TOOT%protocols\proj.android
echo sdk.dir = %ANDROID_SDK_ROOT:\=/%>local.properties
echo plugin.dir=../../>>local.properties
call %ANT% >nul
if errorlevel 0 (
	popd
	textcolor purple
	echo  prtocols build sucessfull
	textcolor 
)else popd &textcolor red &echo  prtocols build   failed
copy %PLUGIN_TOOT%protocols\proj.android\bin\*.jar %PUBLISH_DIR%\protocols\android

textcolor green
echo -------------------------------------------------
textcolor

::发布平台sdk
for  %%i in (%plugins%) do (
	textcolor yellow
	echo Now build %%i
	textcolor
	
	call :buildsdklib %%i
	textcolor green
	echo -------------------------------------------------
	textcolor
)
pause
exit

::编译插件工程  参数为工程名称
:buildsdklib
pushd .
echo %1% isbuilding
set name=%1%
cd %PLUGIN_TOOT%plugins\%name%\proj.android
echo sdk.dir = %ANDROID_SDK_ROOT:\=/%>local.properties
echo plugin.dir=../../..>>local.properties
call %ANT% >nul
popd
if errorlevel 0 (
		textcolor purple 
		echo %i% build successfull 
	)else textcolor red &echo %i% build failed
textcolor
::创建publish里插件目录
if not exist %PUBLISH_DIR%\plugins mkdir %PUBLISH_DIR%\plugins
if not exist %PUBLISH_DIR%\plugins\%name%\android  mkdir %PUBLISH_DIR%\plugins\%name%\android

::复制文件到publish目录
pushd .
cd %PLUGIN_TOOT%plugins\%name%\proj.android
if exist .\DependProject mkdir %PUBLISH_DIR%\plugins\%name%\android\DependProject 2>nul &xcopy /Q /S /Y  DependProject  %PUBLISH_DIR%\plugins\%name%\android\DependProject >nul
if exist .\CLibs mkdir %PUBLISH_DIR%\plugins\%name%\android\CLibs   2>nul         &xcopy  /Q /S /Y CLibs %PUBLISH_DIR%\plugins\%name%\android\CLibs >nul
if exist .\ForAssets mkdir %PUBLISH_DIR%\plugins\%name%\android\ForAssets 2>nul    &xcopy /Q /S /Y  ForAssets %PUBLISH_DIR%\plugins\%name%\android\ForAssets >nul
if exist .\ForRes mkdir %PUBLISH_DIR%\plugins\%name%\android\ForRes     2>nul         &xcopy /Q /S /Y  ForRes %PUBLISH_DIR%\plugins\%name%\android\ForRes >nul

copy .\bin\*.jar     %PUBLISH_DIR%\plugins\%name%\android
copy ForManifest.xml %PUBLISH_DIR%\plugins\%name%\android
copy .\sdk\*.jar     %PUBLISH_DIR%\plugins\%name%\android
popd
	

