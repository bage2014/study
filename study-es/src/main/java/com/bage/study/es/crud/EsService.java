package com.bage.study.es.crud;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.bage.study.es.model.Person;

import java.io.IOException;
import java.util.Date;

public class EsService {

    public int insert(Person person) {
        try {
            ElasticsearchClient client = EsUtils.getClient();
            IndexRequest<Person> request = new IndexRequest.Builder<Person>()
                    .document(person)
                    .id("" + person.getFullName())
                    .index("persons")
                    .build();
            IndexResponse index = client.index(request);
            System.out.println(index.id());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
