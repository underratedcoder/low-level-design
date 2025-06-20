package com.lld.cachemulti.cache.provider;

import com.lld.cachemulti.cache.model.ReadResponse;
import com.lld.cachemulti.cache.model.WriteResponse;
import lombok.NonNull;

import java.util.List;

public interface ILevelCache<Key, Value> {

    @NonNull
    WriteResponse set(Key key, Value value);

    @NonNull
    ReadResponse<Value> get(Key key);

//    @NonNull
//    List<Double> getUsages();
}