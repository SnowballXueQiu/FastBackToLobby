@echo off
echo Building FastBackToLobby plugin...
echo 正在构建 FastBackToLobby 插件...

if not exist "target" mkdir target

echo Cleaning previous build...
echo 清理之前的构建...
call mvn clean

if %errorlevel% neq 0 (
    echo Build failed during clean phase!
    echo 清理阶段构建失败！
    pause
    exit /b 1
)

echo Compiling and packaging...
echo 编译和打包中...
call mvn package

if %errorlevel% neq 0 (
    echo Build failed!
    echo 构建失败！
    pause
    exit /b 1
)

echo Build successful!
echo 构建成功！
echo The plugin jar file is located in the target directory.
echo 插件 jar 文件位于 target 目录中。

if exist "target\FastBackToLobby-1.0.0.jar" (
    echo File: target\FastBackToLobby-1.0.0.jar
    echo 文件: target\FastBackToLobby-1.0.0.jar
)

pause