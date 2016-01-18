package com.kunlun.poker.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.kunlun.poker.domain.ServerNode;

public class ServerNodeService {
    private static final ServerNodeService instance = new ServerNodeService();

    public static ServerNodeService getInstance() {
        return instance;
    }

    private ServerNodeService() {

    }

    private List<ServerNode> serverNodes;

    public synchronized List<ServerNode> getAll() {

        SqlSessionFactory ssf = SqlSessionFactoryHolder.getSqlSessionFactory();
        if (ssf != null) {
            try (SqlSession ss = ssf.openSession()) {
                serverNodes = ss.selectList("selectServers");
            }
        }

        return serverNodes;
    }

    public synchronized ServerNode getOne(int id) {
        SqlSessionFactory ssf = SqlSessionFactoryHolder.getSqlSessionFactory();
        if (ssf != null) {
            try (SqlSession ss = ssf.openSession()) {
                return ss.selectOne("selectServer", id);
            }
        }

        return null;
    }
}
