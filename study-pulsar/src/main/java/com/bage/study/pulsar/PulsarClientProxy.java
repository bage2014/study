package com.bage.study.pulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class PulsarClientProxy {

    public static PulsarClient getInstance(){
        PulsarClient client = null;
        try {
            client = PulsarClient.builder()
                    .serviceUrl("pulsar://localhost:6650")
                    .build();
        } catch (PulsarClientException e) {
            throw new RuntimeException(e);
        }

        return client;
    }
}
