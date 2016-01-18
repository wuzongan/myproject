package com.kunlun.poker.service;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kunlun.poker.Config;

public class SqlSessionFactoryHolder {
    private static final Logger logger = LoggerFactory
            .getLogger(SqlSessionFactoryHolder.class);


    private static SqlSessionFactory sqlSessionFactory;

    public synchronized static  SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            try (InputStream inputStream = Resources
                    .getResourceAsStream("com/kunlun/poker/data/mybatis-common.xml")) {
                SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
                sqlSessionFactory = ssfb.build(inputStream,
                        Config.getInstance());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return sqlSessionFactory;
    }
    
}
