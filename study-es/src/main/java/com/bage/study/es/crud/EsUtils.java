package com.bage.study.es.crud;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class EsUtils {


    public static ElasticsearchClient getClient() {

        // Create the low-level client
        HttpHost localhost = new HttpHost("localhost", 9092);
        RestClient restClient = RestClient.builder(localhost).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        return new ElasticsearchClient(transport);
    }

    public static ElasticsearchAsyncClient getAsyncClient() {

        // Create the low-level client
        HttpHost localhost = new HttpHost("localhost", 9092);
        RestClient restClient = RestClient.builder(localhost).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        // Asynchronous non-blocking client
        return new ElasticsearchAsyncClient(transport);
    }

}
