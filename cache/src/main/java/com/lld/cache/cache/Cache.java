package com.lld.cache.cache;

import com.lld.cache.cache.exceptions.NotFoundException;
import com.lld.cache.cache.exceptions.StorageFullException;
import com.lld.cache.cache.policies.IEvictionPolicy;
import com.lld.cache.cache.storage.IStorage;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache<Key, Value> {
    private final IEvictionPolicy<Key> IEvictionPolicy;
    private final IStorage<Key, Value> IStorage;
    private final ReentrantReadWriteLock lock;
    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    public Cache(IEvictionPolicy<Key> IEvictionPolicy, IStorage<Key, Value> IStorage) {
        this.IEvictionPolicy = IEvictionPolicy;
        this.IStorage = IStorage;
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public void put(Key key, Value value) {
        writeLock.lock();
        try {
            this.IStorage.add(key, value);
            this.IEvictionPolicy.keyAccessed(key);
        } catch (StorageFullException exception) {
            System.out.println("Got storage full. Will try to evict.");
            Key keyToRemove = IEvictionPolicy.evictKey();
            if (keyToRemove == null) {
                throw new RuntimeException("Unexpected State. Storage full and no key to evict.");
            }
            this.IStorage.remove(keyToRemove);
            System.out.println("Creating space by evicting item..." + keyToRemove);
            put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public Value get(Key key) {
        readLock.lock();
        try {
            Value value = this.IStorage.get(key);
            this.IEvictionPolicy.keyAccessed(key);
            return value;
        } catch (NotFoundException notFoundException) {
            System.out.println("Tried to access non-existing key.");
            return null;
        } finally {
            readLock.unlock();
        }
    }


}
