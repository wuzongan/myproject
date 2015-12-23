@echo off
set DIR=%~dp0
set OUTPUT_DIR=%DIR%
set MAKE_LUABINDING="%QUICK_COCOS2DX_ROOT%\bin\compile_luabinding.bat"
pushd
cd /d "%DIR%"
call %MAKE_LUABINDING%   -d %OUTPUT_DIR% Plugin_luabinding.tolua