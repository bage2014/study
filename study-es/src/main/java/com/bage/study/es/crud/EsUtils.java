package com.bage.study.es.crud;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.transport.TransportUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;

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


    public static ElasticsearchClient getAdvanceClient() throws IOException {
    // Create the low-level client
        String host = "localhost";
        int port = 9200;
        String login = "elastic";
        String password = "changeme";
        String apiKey = "VnVhQ2ZHY0JDZGJrU...";
        File certFile = new File("/path/to/http_ca.crt");

        SSLContext sslContext = TransportUtils
                .sslContextFromHttpCaCrt(certFile);

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(login, password)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(host, port, "https"))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();


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
