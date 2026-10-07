package com.arigs.rms.common;

import java.util.Optional;

/**
 * Cache abstraction that can be backed by local cache or Redis.
 */
public interface CachePort {

    <T> Optional<T> get(String cacheName, String key, Class<T> type);

    void put(String cacheName, String key, Object value);

    void evict(String cacheName, String key);
}
