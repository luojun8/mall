要求mysql、jdk1.8

item下需要建立一个logs文件夹,接收日志

application.yml修改为自己的数据库配置

启动前需要建立库：create database test;

建立表(db文件夹下)：
先建立t_user表，执行t_user文件；
再建立t_order表，执行t_order文件；
再建立t_every_order表和t_order_his表，执行t_every_order、t_order_his文件；
最后建立t_item表，执行t_item文件。

运行Application.java的main方法

访问链接http://127.0.0.1:8082/item/page/index.html