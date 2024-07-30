package com.bage.study.pulsar;

import org.apache.pulsar.client.api.*;

public class MyConsumer {

    private PulsarClient client = null;
    private Consumer consumer = null;

    public void close() {
        try {
            consumer.close();
            client.close();
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void listener() {
        if (consumer != null) {
            return;
        }

        try {
            client = MyPulsarClient.getInstance();
            consumer = client.newConsumer()
                    .topic("my-topic")
                    .subscriptionName("my-subscription")
                    .messageListener((MessageListener<byte[]>) (consumer1, msg) -> {
                        try {
                            System.out.println("Message received: " + new String(msg.getData()));
                            consumer1.acknowledge(msg);
                        } catch (Exception e) {
                            consumer1.negativeAcknowledge(msg);
                        }
                    })
                    .subscribe();
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }
    }

}
