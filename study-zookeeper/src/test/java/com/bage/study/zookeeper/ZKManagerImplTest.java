package com.bage.study.zookeeper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class ZKManagerImplTest {

    @Test
    public void setAndGet() {
        ZKManagerImpl zkManager = new ZKManagerImpl();
        try {
            String node = "/hello-zookeeper";
            zkManager.create(node, "hello-bage".getBytes(StandardCharsets.UTF_8));

            Object nodeData = zkManager.getZNodeData(node, false);
            System.out.println(nodeData);

            zkManager.update(node, "hello-bage-new".getBytes(StandardCharsets.UTF_8));

            Object nodeDataNew = zkManager.getZNodeData(node, false);
            System.out.println(nodeDataNew);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zkManager.closeConnection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
