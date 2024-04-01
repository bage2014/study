package com.bage.study.es.crud;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.bage.study.es.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EsService {

    private String index = "persons";

    public int insert(Person person) {
        try {
            ElasticsearchClient client = EsUtils.getClient();
            IndexRequest<Person> request = new IndexRequest.Builder<Person>()
                    .document(person)
                    .id("" + person.getId())
                    .index(index)
                    .build();
            IndexResponse index = client.index(request);
            System.out.println(index.id());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int update(Person person) {
        try {
            ElasticsearchClient client = EsUtils.getClient();
            UpdateRequest<Person, Object> request = new UpdateRequest.Builder<Person, Object>()
                    .index(index)
                    .id("" + person.getId())
                    .doc(person)
                    .build();
            UpdateResponse<Person> update = client.update(request, Person.class);
            System.out.println(update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int delete(Long id) {
        try {
            ElasticsearchClient client = EsUtils.getClient();
            DeleteRequest request = new DeleteRequest.Builder()
                    .index(index)
                    .id("" + id)
                    .build();
            DeleteResponse delete = client.delete(request);
            System.out.println(delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public List<Person> query(Long id) {
        List<Person> list = new ArrayList<>();
        try {
            ElasticsearchClient client = EsUtils.getClient();
            Query query = QueryBuilders.ids().values("" + id).build()._toQuery();
            SearchResponse<Person> search = client.search(s -> s
                            .index(index)
                            .query(query)
                    , Person.class);
            List<Hit<Person>> hits = search.hits().hits();

            for (Hit<Person> hit : hits) {
                list.add(hit.source());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
