package com.bage.study.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.bage.study.es.crud.EsService;
import com.bage.study.es.model.Person;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class EsCrudJavaClientTest {

    private static EsService esService = new EsService();
    public static void main(String[] args) throws IOException {
        // CRUD
        long id = System.currentTimeMillis();
        System.out.println("id: " + id);

        insert(id);
        query(id);

        // update
        update(id);
        query(id);

//        delete(id);
//        query(id);

    }

    private static void update(long id) throws IOException {
        Person person = new Person(id, 25, "Janette Doe [new]", new Date());
        int update = esService.update(person);
        System.out.println(update);
    }

    private static void delete(long id) throws IOException {
        int delete = esService.delete(id);
        System.out.println(delete);
    }

    private static void query(long id) throws IOException {
        List<Person> query = esService.query(id);
        System.out.println(query);
    }

    private static void insert(long id) throws IOException {
        Person person = new Person(id, 25, "Janette Doe", new Date());
        int insert = esService.insert(person);
        System.out.println(insert);
    }

}
