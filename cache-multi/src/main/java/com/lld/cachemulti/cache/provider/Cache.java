package com.lld.cachemulti.cache.provider;

import com.lld.cachemulti.cache.exceptions.StorageFullException;
import com.lld.cachemulti.cache.policies.IEvictionPolicy;
import com.lld.cachemulti.cache.storage.IStorage;

public class Cache<Key, Value> {

    private final IEvictionPolicy<Key> IEvictionPolicy;
    private final IStorage<Key, Value> storage;

    public Cache(IEvictionPolicy<Key> IEvictionPolicy, IStorage<Key, Value> storage) {
        this.IEvictionPolicy = IEvictionPolicy;
        this.storage = storage;
    }

    public void set(Key key, Value value) {
        try {
            this.storage.add(key, value);
            this.IEvictionPolicy.keyAccessed(key);
        } catch (StorageFullException exception) {
            final Key keyToRemove = IEvictionPolicy.evictKey();
            if (keyToRemove == null) {
                throw new RuntimeException("Unexpected State.");
            }

            this.storage.remove(keyToRemove);
            set(key, value);
        }
    }

    public Value get(Key key) {
        final Value value = this.storage.get(key);
        this.IEvictionPolicy.keyAccessed(key);
        return value;
    }

//    public double getCurrentUsage() {
//        return this.storage.getCurrentUsage();
//    }
}