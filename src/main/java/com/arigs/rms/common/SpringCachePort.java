package com.arigs.rms.common;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Spring Cache implementation of the cache port.
 */
@Component
@RequiredArgsConstructor
public class SpringCachePort implements CachePort {

    private final CacheManager cacheManager;

    @Override
    public <T> Optional<T> get(String cacheName, String key, Class<T> type) {
        var cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(key, type));
    }

    @Override
    public void put(String cacheName, String key, Object value) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    @Override
    public void evict(String cacheName, String key) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }
}
