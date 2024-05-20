package com.bage.study.es.terms;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.bage.study.es.crud.EsUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EsTermService {


    public void analyze(String index, String content) throws IOException {

        // And create the API client
        ElasticsearchClient client = EsUtils.getClient();

        AnalyzeRequest request = new AnalyzeRequest.Builder()
                .index(index)
                .text(Collections.singletonList(content))
                .build();

        AnalyzeResponse response =client.indices()
                .analyze(request);//执行

        List<AnalyzeToken> tokens = response.tokens();
        System.out.println(tokens);

    }
}
