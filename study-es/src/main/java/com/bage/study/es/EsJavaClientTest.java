package com.bage.study.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class EsJavaClientTest {
    public static void main(String[] args) throws IOException {

        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9092)).build();

// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        // prepare

        // CRUD
        long id = System.currentTimeMillis();
        System.out.println("id: " + id);


        insert(client, id);

        query(client, id);

        // update
        update(client, id);
        query(client, id);

        delete(client, id);
        query(client, id);

    }

    private static void update(ElasticsearchClient client, long id) throws IOException {
        Person person = new Person(25, "Janette Doe [new]", new Date());
        UpdateRequest<Person, Object> request = new UpdateRequest.Builder<Person, Object>()
                .index("persons")
                .id("" + id)
                .doc(person)
                .build();
        UpdateResponse<Person> update = client.update(request, Person.class);
        System.out.println(update);
    }

    private static void delete(ElasticsearchClient client, long id) throws IOException {
        DeleteRequest request = new DeleteRequest.Builder()
                .index("persons")
                .id("" + id)
                .build();
        DeleteResponse delete = client.delete(request);
        System.out.println(delete);
    }

    private static void query(ElasticsearchClient client, long id) throws IOException {
        Query query = QueryBuilders.ids().values("" + id).build()._toQuery();
        SearchResponse<Person> search = client.search(s -> s
                        .index("persons")
                        .query(query)
//                        .source(
                , Person.class);
        List<Hit<Person>> hits = search.hits().hits();

        for (Hit<Person> hit : hits) {
            processProduct(hit.source());
        }
    }

    private static void insert(ElasticsearchClient client, long id) throws IOException {
        Person person = new Person(25, "Janette Doe", new Date());
        IndexRequest<Person> request = new IndexRequest.Builder<Person>()
                .document(person)
                .id("" + id)
                .index("persons")
                .build();
        IndexResponse index = client.index(request);
        System.out.println(index);
    }

    private static void processProduct(Person source) {
        System.out.println(source);
    }
}
