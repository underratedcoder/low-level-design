package com.lld.cachemulti.cache.storage;

import com.lld.cachemulti.cache.exceptions.NotFoundException;
import com.lld.cachemulti.cache.exceptions.StorageFullException;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStorage<Key, Value> implements IStorage<Key, Value> {

    Map<Key, Value> storage;
    private final Integer capacity;

    public InMemoryStorage(Integer capacity) {
        this.capacity = capacity;
        storage = new HashMap<>();
    }

    @Override
    public void add(Key key, Value value) throws StorageFullException {
        if (isStorageFull()) throw new StorageFullException("Capacity Full.....");
        storage.put(key, value);
    }

    @Override
    public void remove(Key key) throws NotFoundException {
        if (!storage.containsKey(key)) throw new NotFoundException(key + "doesn't exist in cache.");
        storage.remove(key);
    }

    @Override
    public Value get(Key key) throws NotFoundException {
        if (!storage.containsKey(key)) throw new NotFoundException(key + "doesn't exist in cache.");
        return storage.get(key);
    }

//    public double getCurrentUsage() {
//        return ((double) storage.size()) / ((double) capacity);
//    }

    private boolean isStorageFull() {
        return storage.size() == capacity;
    }
}
