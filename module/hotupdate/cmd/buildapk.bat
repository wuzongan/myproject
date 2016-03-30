@echo off
set ANT=E:\workspace\android\eclipse_android\plugins\org.apache.ant_1.8.3.v201301120609\bin
call %ANDROID_SDK_ROOT%\tools\android update project -p .
call %ANT%\ant release