package com.lld.cachemulti.cache.storage;

import com.lld.cachemulti.cache.exceptions.NotFoundException;
import com.lld.cachemulti.cache.exceptions.StorageFullException;

public interface IStorage<Key, Value> {
    void add(Key key, Value value) throws StorageFullException;

    void remove(Key key) throws NotFoundException;

    Value get(Key key) throws NotFoundException;

//    double getCurrentUsage();
}
