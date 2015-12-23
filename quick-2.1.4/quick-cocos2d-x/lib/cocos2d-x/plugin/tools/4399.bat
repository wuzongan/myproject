set DIR=%~dp0
set PLUGIN_TOOT=%DIR%..\
set PUBLISH_DIR=%PLUGIN_TOOT%publish
set ANT="E:\kaifa\eclipse\plugins\org.apache.ant_1.8.3.v201301120609\bin\ant.bat"

pushd .
echo %1% isbuilding
set name=4399
cd %PLUGIN_TOOT%plugins\%name%\proj.android
echo sdk.dir = %ANDROID_SDK_ROOT:\=/%>local.properties
echo plugin.dir=../../..>>local.properties
call %ANT% 
popd

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
	