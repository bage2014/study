package com.bage.study.rca.demo.order.client;

import com.bage.study.rca.demo.order.model.SupplyCheckResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * 供应服务客户端：封装库存校验与慢接口调用。
 */
@Component
public class SupplierClient {

    private final RestClient supplierRestClient;

    public SupplierClient(RestClient supplierRestClient) {
        this.supplierRestClient = supplierRestClient;
    }

    public SupplyCheckResult check(String skuId, int quantity) {
        return supplierRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/supply/check")
                        .queryParam("skuId", skuId)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .body(SupplyCheckResult.class);
    }

    /**
     * 调用供应服务慢接口，用于制造超时。
     */
    public void slow(int millis) {
        supplierRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/supply/slow")
                        .queryParam("millis", millis)
                        .build())
                .retrieve()
                .toBodilessEntity();
    }
}
