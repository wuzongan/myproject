#!/bin/bash

echo "CREATE DATABASE game2;" >> ./create_database_game.sql \
&& \
mysql -u"root" -p"111111" < ./create_database_game.sql \
&& \
rm ./create_database_game.sql \
&& \
echo 'done'

#rm ./create_table_users.sql
for i in {0..15};do echo "CREATE TABLE paylog_$i (
        order_id varchar(255) NOT NULL, 
        admin varchar(32), 
        gift_coin mediumint, 
        level smallint, 
        old_coin mediumint, 
        order_coin mediumint, 
        order_money smallint, 
        order_time varchar(255), 
        platform varchar(32), 
        product_id mediumint, 
        raw_data blob, 
        reason blob, 
        scheme_id varchar(255), 
        user_id varchar(32),
        uin varchar(32),
        PRIMARY KEY(order_id), 
        index(order_id),
        index(order_time),
        index(user_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;" >> ./create_table_paylog.sql;done \
    && \
    mysql -u"root" -p"111111" game2 < ./create_table_paylog.sql \
    && \
    rm ./create_table_paylog.sql \
    && \
    echo 'done'

#for i in {0..15}; do echo "alter table paylog_$i add index user_id (user_id);";done;


#rm ./create_table_spendlog.sql
for i in {0..15};do echo "CREATE TABLE spendlog_$i (
        order_id int(50) NOT NULL auto_increment,
        uid varchar(32), 
        level int(4), 
        subtime varchar(32), 
        coin_num int(10),
        coin_1st int(10),
        coin_2nd int(10),
        goods_type varchar(50),
        goods_subtype varchar(50),
        goods_name varchar(100),
        goods_num int(10),
        goods_price int(10),
        goods_cnname varchar(100),
        args varchar(500),
        PRIMARY KEY(order_id), 
        index(subtime),
        index(uid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;" >> ./create_table_spendlog.sql;done \
    && \
    mysql -u"root" -p"111111" game2 < ./create_table_spendlog.sql \
    && \
    rm ./create_table_spendlog.sql \
    && \
    echo 'done'

#for i in {0..15}; do echo "alter table spendlog_$i add index uid (uid);";done;

#rm ./create_table_users.sql
for i in {0..15};do echo "CREATE TABLE paylog_ios_$i (
        order_id varchar(255) NOT NULL, 
        admin varchar(32), 
        gift_coin mediumint, 
        level smallint, 
        old_coin mediumint, 
        order_coin mediumint, 
        order_money smallint, 
        order_time varchar(255), 
        platform varchar(32), 
        product_id mediumint, 
        raw_data blob, 
        reason blob, 
        scheme_id varchar(255), 
        user_id varchar(32),
        uin varchar(32),
        PRIMARY KEY(order_id), 
        index(order_id),
        index(order_time),
        index(user_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;" >> ./create_table_paylog.sql;done \
    && \
    mysql -u"root" -p"111111" game2 < ./create_table_paylog.sql \
    && \
    rm ./create_table_paylog.sql \
    && \
    echo 'done'

#rm ./create_table_spendlog.sql
for i in {0..15};do echo "CREATE TABLE spendlog_ios_$i (
        order_id int(50) NOT NULL auto_increment,
        uid varchar(32), 
        level int(4), 
        subtime varchar(32), 
        coin_num int(10),
        coin_1st int(10),
        coin_2nd int(10),
        goods_type varchar(50),
        goods_subtype varchar(50),
        goods_name varchar(100),
        goods_num int(10),
        goods_price int(10),
        goods_cnname varchar(100),
        args varchar(500),
        PRIMARY KEY(order_id), 
        index(subtime),
        index(uid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;" >> ./create_table_spendlog.sql;done \
    && \
    mysql -u"root" -p"111111" game2 < ./create_table_spendlog.sql \
    && \
    rm ./create_table_spendlog.sql \
    && \
    echo 'done'
