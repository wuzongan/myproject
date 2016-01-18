package com.kunlun.poker.domain;

public class ServerNode {
    public static final int ID_CENTER = 101;
    public static final int ID_LOGIN = 102;
    public static final int ID_LOG = 103;

    private int id;
    private String name;
    private String host;
    private int port;
    private int connectFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getConnectFlag() {
        return connectFlag;
    }

    public void setConnectFlag(int connectFlag) {
        this.connectFlag = connectFlag;
    }

    @Override
    public String toString() {
        return "ServerNode [id=" + id + ", name=" + name + ", host=" + host
                + ", port=" + port + ", connectFlag=" + connectFlag + "]";
    }
}
