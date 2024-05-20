package com.bage.study.es.crud;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;

import java.io.Reader;
import java.io.StringReader;

public class AdvancedEsService {

    private String index = "persons";

    public String withJson(String json) {
        try {
            Reader input = new StringReader(json);

            IndexRequest<JsonData> request = IndexRequest.of(i -> i
                    .index("logs")
                    .withJson(input)
            );

            IndexResponse response = EsUtils.getClient().index(request);

            System.out.println("Indexed with version " + response.version());

            return response.id();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
