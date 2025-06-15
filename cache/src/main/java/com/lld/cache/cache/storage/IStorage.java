package com.lld.cache.cache.storage;

import com.lld.cache.cache.exceptions.NotFoundException;
import com.lld.cache.cache.exceptions.StorageFullException;

public interface IStorage<Key, Value> {
    void add(Key key, Value value) throws StorageFullException;

    void remove(Key key) throws NotFoundException;

    Value get(Key key) throws NotFoundException;
}
