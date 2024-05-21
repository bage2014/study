package com.bage.study.es.crud;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.bage.study.es.model.Person;
import com.bage.study.es.model.Product;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedEsService {

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


    public void bulkMultiDocuments() {
        try {

            List<Product> products = fetchProducts();

            BulkRequest.Builder br = new BulkRequest.Builder();

            for (Product product : products) {
                br.operations(op -> op
                        .index(idx -> idx
                                .index("products")
                                .id(product.getSku())
                                .document(product)
                        )
                );
            }

            BulkResponse result = EsUtils.getClient().bulk(br.build());

            // Log errors, if any
            if (result.errors()) {
                System.out.println("Bulk had errors");
                for (BulkResponseItem item : result.items()) {
                    if (item.error() != null) {
                        System.out.println(item.error().reason());
                    }
                }
            } else {
                System.out.println("---- OK -----");
                try {
                    ElasticsearchClient client = EsUtils.getClient();
                    Query query = QueryBuilders.ids().values("bk-1","bk-2","bk-3")
                            .build()._toQuery();
                    SearchResponse<Product> search = client.search(s -> s
                                    .index("products")
                                    .query(query)
                            , Product.class);
                    List<Hit<Product>> hits = search.hits().hits();

                    for (Hit<Product> hit : hits) {
                        System.out.println(hit.source());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Product> fetchProducts() {
        return Arrays.asList(
                new Product("bk-1", "City bike", 123.0),
                new Product("bk-2", "City bike2", 1234.0),
                new Product("bk-3", "City bike3", 1253.0)
        );

    }

}
