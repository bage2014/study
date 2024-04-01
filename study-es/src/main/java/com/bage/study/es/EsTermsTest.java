package com.bage.study.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.bage.study.es.model.Person;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EsTermsTest {
    public static void main(String[] args) throws IOException {

        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9092)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);
        String content ="hello world";
        AnalyzeRequest request = new AnalyzeRequest.Builder()
                .index("persons")
                .text(Collections.singletonList(content))
                .build();
        AnalyzeResponse response =client.indices()
                .analyze(request);//执行
        List<AnalyzeToken> tokens = response.tokens();
        System.out.println(tokens);

    }
    private static void processProduct(Person source) {
        System.out.println(source);
    }
}
