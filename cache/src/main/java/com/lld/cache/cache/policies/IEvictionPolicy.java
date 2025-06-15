package com.lld.cache.cache.policies;

/**
 * Interface for defining eviction policies.
 *
 * @param <Key> Type of key.
 */
public interface IEvictionPolicy<Key> {

    void keyAccessed(Key key);

    /**
     * Evict key from eviction policy and return it.
     */
    Key evictKey();
}
