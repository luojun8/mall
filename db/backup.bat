@echo off
set "Ymd=%date:~,4%%date:~5,2%%date:~8,2%"
C:\MySQL\bin\mysqldump --opt -u root --password=123456 test > D:\db_backup\test_%Ymd%.sql
@echo on


https://jingyan.baidu.com/article/6181c3e0435026152ef153d0.html//参考定时任务的创建