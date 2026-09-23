package com.bage.study.ai.best.practice.rca.agent.topology;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TopologyServiceTest {

    private final TopologyService topologyService = new TopologyService();

    @Test
    void directDependenciesShouldBeWithinTwoHops() {
        assertThat(topologyService.withinHops("order-service", "order-service-mq", 2)).isTrue();
        assertThat(topologyService.withinHops("order-service", "order-service-db", 2)).isTrue();
        assertThat(topologyService.withinHops("order-service", "supplier-api", 2)).isTrue();
    }

    @Test
    void unrelatedNoiseNodesShouldBePruned() {
        assertThat(topologyService.withinHops("order-service", "other-service-db", 2)).isFalse();
        assertThat(topologyService.prunedNodes("order-service", 2))
                .contains("order-service-db", "order-service-mq")
                .doesNotContain("other-service", "other-service-db");
    }
}
