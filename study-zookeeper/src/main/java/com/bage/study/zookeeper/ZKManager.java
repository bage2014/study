package com.bage.study.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.UnsupportedEncodingException;

public interface ZKManager {
    void create(String path, byte[] data) throws Exception;
    Object getZNodeData(String path, boolean watchFlag) throws Exception;
    void update(String path, byte[] data) throws Exception;
}