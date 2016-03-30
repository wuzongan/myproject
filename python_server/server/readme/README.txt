上传install.sh远程服务器
scp ~/Desktop/server/readme/install.sh root@123.57.80.17:/home/
安装所需软件
cd /home
chmod 777 install.sh
./install.sh

1、代码上传到远程服务器
    1）修改servercode下的fabfile.py的host和password
    2）cd到servercode执行fab go:test,1
    3）等待上传成功！
    4）把server目录下的etc、ftp、redis用ftp上传到远程服务器
 
2、修改/etc/rc.local
    在touch /var/lock/subsys/local这行下面添加：
 
    ulimit -SHn 102400
    #/usr/bin/mysql-proxy --proxy-backend-addresses=10.10.142.159:3306 --proxy-address=:3306 --daemon --keepalive
    /usr/local/bin/redis-server /data/redis/etc/redis_6300.conf
    /usr/local/bin/redis-server /data/redis/etc/redis_6301.conf
    supervisord -c /data/etc/supervisor.d/supervisord.conf
    /usr/sbin/nginx -c /data/etc/nginx/nginx.conf


3、导入数据、启动游戏服务器
    1）进入后台http://ip/master2/admin/index/，开发工具/分服配置/输入服务服名称，启动
    2）注释initial_import.py的两行代码
        #if server == 'master':
        #    continue
        执行两次python /data/server/initial_import.py s2
     3）shutdown -r 0
 
4、查看服务器启动状态
supervisorctl
user
123

5、配置MySQL数据库
    1)安装mysql相关软件
    yum install -y mysql mysql-devel mysql-server mysql-proxy
    2)运行mysql服务
    service mysqld start （第一次运行如果mysql安装在本机需要kill掉进程，如：
        ps -ef | grep mysql 
        kill掉运行的mysql进程
    ）
    添加mysql用户密码
    mysqladmin -u root password 111111
    3)导入数据库，先chmod 777 create_table_paylog.sh，然后python create_table_paylog.sh
    4）开通3306端口和给远程开通权限
    打开ucloud后台，进入防火墙界面，然后添加3306的端口
    进入mysql，执行
GRANT ALL PRIVILEGES ON *.* TO 'root’@‘%' IDENTIFIED BY '111111' WITH GRANT OPTION;
FLUSH PRIVILEGES;
exit;
重启mysql：service mysqld restart
    完毕！
  
同理，redis也需要在ucloud后台添加6300、6301端口，再通过用户密码直接访问

常见问题：
1、访问nginx的网页链接404，http://123.59.51.244/master2/admin/index/
查看nginx进程，ps -ef | grep nginx
如果不是/data/etc/nginx/nginx.conf，先kill掉，在/usr/sbin/nginx -c /data/etc/nginx/nginx.conf
2、运行游戏发现数据表的报错
    再导入一遍数据
    python /data/server/initial_import.py s2
  然后重启服务器
3、如果重启后supervisor的服务器没有启动成功，执行
supervisord -c /data/etc/supervisor.d/supervisord.conf