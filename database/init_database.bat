@echo off
echo ============================================
echo 外卖系统数据库初始化脚本
echo ============================================
echo.

REM 设置MySQL连接信息
set MYSQL_HOST=localhost
set MYSQL_PORT=3306
set MYSQL_USER=root
set MYSQL_PASS=123456
set DB_NAME=takeout_system

echo 正在连接MySQL服务器...
echo.

REM 执行建表脚本
echo [1/2] 正在创建数据库和表...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -h%MYSQL_HOST% -P%MYSQL_PORT% -u%MYSQL_USER% -p%MYSQL_PASS% < "%~dp01_create_tables.sql"
if %errorlevel% neq 0 (
    echo 错误：建表脚本执行失败！
    pause
    exit /b 1
)
echo 建表脚本执行成功！
echo.

REM 执行数据插入脚本
echo [2/2] 正在插入初始数据...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -h%MYSQL_HOST% -P%MYSQL_PORT% -u%MYSQL_USER% -p%MYSQL_PASS% < "%~dp02_insert_data.sql"
if %errorlevel% neq 0 (
    echo 错误：数据插入脚本执行失败！
    pause
    exit /b 1
)
echo 数据插入脚本执行成功！
echo.

echo ============================================
echo 数据库初始化完成！
echo ============================================
echo.
echo 数据库名称：%DB_NAME%
echo.
echo 测试账号信息：
echo - 前端用户：test1/123456, test2/123456, test3/123456
echo - 商家账号：shop_admin1/123(肯德基), shop_admin2/123(麦当劳), shop_admin3/123(星巴克), shop_admin4/123(必胜客), shop_admin5/123(喜茶)
echo - 平台管理员：admin/123456
echo.
pause
