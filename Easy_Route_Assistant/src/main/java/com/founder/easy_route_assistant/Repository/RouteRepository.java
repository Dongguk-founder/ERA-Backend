package com.founder.easy_route_assistant.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@CacheConfig(cacheNames="RouteDTO")
public class RouteRepository {
    private final Map<Long, String> items = new LinkedHashMap<>();

    @CachePut(key="'all'")
    public List<String> findAll() {
        List<String> results = items.values().stream().toList();
        log.info("Cache findAll {}", results);

        return results;
    }

    @CachePut(key="#id")
    public String findById(Long id) {

        return items.get(id);
    }

    @CachePut(key="#id")
    @CacheEvict(key = "'all'")
    public void save(Long id, String jsonRouteDTO) {
        log.info("Cahce save {}", jsonRouteDTO);
        items.put(id, jsonRouteDTO);
    }
}
