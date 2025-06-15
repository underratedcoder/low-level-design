package com.lld.cache.cache.storage;

import com.lld.cache.cache.exceptions.NotFoundException;
import com.lld.cache.cache.exceptions.StorageFullException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HashMapBasedIStorage<Key, Value> implements IStorage<Key, Value> {

    Map<Key, Value> storage; // You can use ConcurrentHashMap and avoid lock ?
    private final Integer capacity;
    private final ScheduledExecutorService executor;

    public HashMapBasedIStorage(Integer capacity) {
        this.capacity = capacity;
        storage = new HashMap<>();

        this.executor = Executors.newScheduledThreadPool(1);

        schedulePeriodicCleanup();
    }

    private void schedulePeriodicCleanup() {
        Runnable cleanupTask = () -> {
            // Scan map and remove expired keys
        };

        executor.scheduleAtFixedRate(cleanupTask, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void add(Key key, Value value) throws StorageFullException {
        if (isStorageFull())
            throw new StorageFullException("Capacity Full.....");
        storage.put(key, value);
    }

    @Override
    public void remove(Key key) throws NotFoundException {
        if (!storage.containsKey(key))
            throw new NotFoundException(key + "doesn't exist in cache.");
        storage.remove(key);
    }

    @Override
    public Value get(Key key) throws NotFoundException {
        if (!storage.containsKey(key))
            throw new NotFoundException(key + "doesn't exist in cache.");
        return storage.get(key);
    }

    private boolean isStorageFull() {
        return storage.size() == capacity;
    }
}
