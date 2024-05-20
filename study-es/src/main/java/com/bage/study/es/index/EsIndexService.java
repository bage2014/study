package com.bage.study.es.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.bage.study.es.crud.EsUtils;
import com.bage.study.es.model.Product;

import java.io.IOException;

public class EsIndexService {

    private ElasticsearchClient esClient = EsUtils.getClient();

    public int createIndex(){
        try {
            esClient.indices().create(c -> c
                    .index("products")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void index(){
        Product product = new Product("bk-1", "City bike", 123.0);

        IndexResponse response = null;
        try {
            response = esClient.index(i -> i
                    .index("products")
                    .id(product.getSku())
                    .document(product)
            );
            System.out.println("Indexed with version " + response.version());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
