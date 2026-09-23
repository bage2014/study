package com.bage.study.ai.best.practice.rca.agent.topology;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 服务拓扑（Mock）：提供无向依赖图与 ±N 跳裁剪能力（rca.md 拓扑感知裁剪）。
 * 图中同时构造与告警应用无关的噪声节点，用于验证裁剪/干扰项过滤。
 */
@Service
public class TopologyService {

    /**
     * 构建以 appId 为中心的依赖图（无向边，BFS 上下跳均可达）。
     */
    public Map<String, Set<String>> buildGraph(String appId) {
        Map<String, Set<String>> graph = new HashMap<>();
        String app = appId;
        String db = appId + "-db";
        String redis = appId + "-redis";
        String mq = appId + "-mq";
        String host = appId + "-host";
        String supplier = "supplier-api";

        connect(graph, app, db);
        connect(graph, app, redis);
        connect(graph, app, mq);
        connect(graph, app, supplier);
        connect(graph, app, host);

        // 噪声子图：与告警应用无连接，拓扑裁剪时应被排除
        String otherApp = "other-service";
        connect(graph, otherApp, "other-service-db");
        connect(graph, otherApp, "other-service-mq");
        return graph;
    }

    /** 目标节点是否在告警应用 maxHops 跳范围内 */
    public boolean withinHops(String appId, String target, int maxHops) {
        Map<String, Set<String>> graph = buildGraph(appId);
        Set<String> visited = new HashSet<>();
        Set<String> frontier = new LinkedHashSet<>();
        frontier.add(appId);
        visited.add(appId);
        for (int hop = 0; hop < maxHops; hop++) {
            Set<String> next = new LinkedHashSet<>();
            for (String node : frontier) {
                for (String neighbor : graph.getOrDefault(node, Set.of())) {
                    if (visited.add(neighbor)) {
                        next.add(neighbor);
                    }
                }
            }
            frontier = next;
        }
        return visited.contains(target);
    }

    /** 告警应用 ±maxHops 跳内的节点（拓扑裁剪结果） */
    public List<String> prunedNodes(String appId, int maxHops) {
        Map<String, Set<String>> graph = buildGraph(appId);
        Set<String> visited = new LinkedHashSet<>();
        Set<String> frontier = new LinkedHashSet<>();
        frontier.add(appId);
        visited.add(appId);
        for (int hop = 0; hop < maxHops; hop++) {
            Set<String> next = new LinkedHashSet<>();
            for (String node : frontier) {
                for (String neighbor : graph.getOrDefault(node, Set.of())) {
                    if (visited.add(neighbor)) {
                        next.add(neighbor);
                    }
                }
            }
            frontier = next;
        }
        return new ArrayList<>(visited);
    }

    private static void connect(Map<String, Set<String>> graph, String a, String b) {
        graph.computeIfAbsent(a, k -> new LinkedHashSet<>()).add(b);
        graph.computeIfAbsent(b, k -> new LinkedHashSet<>()).add(a);
    }
}
