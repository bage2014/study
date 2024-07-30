package com.bage.study.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class MyProducer {

    public void send(String message) {
        PulsarClient client = PulsarClientProxy.getInstance();
        Producer<byte[]> producer = null;
        try {
            producer = client.newProducer()
                    .topic("my-topic")
                    .create();
            // You can then send messages to the broker and topic you specified:
            producer.send(message.getBytes());

        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                producer.close();
                client.close();
            } catch (PulsarClientException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
