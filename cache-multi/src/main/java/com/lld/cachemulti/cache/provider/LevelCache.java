package com.lld.cachemulti.cache.provider;

import com.lld.cachemulti.cache.model.LevelCacheData;
import com.lld.cachemulti.cache.model.ReadResponse;
import com.lld.cachemulti.cache.model.WriteResponse;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

//CacheService -> L1 -> L2 -> L3...Ln -> null
//                C1    C2    ...
//@AllArgsConstructor
public class LevelCache<Key, Value> implements ILevelCache<Key, Value> {
    private final LevelCacheData levelCacheData;
    private final Cache<Key, Value> cache;
    private final ILevelCache<Key, Value> next;

    public LevelCache(
            LevelCacheData levelCacheData,
            Cache<Key, Value> cache,
            ILevelCache<Key, Value> next
    ) {
            this.levelCacheData = levelCacheData;
            this.cache = cache;
            this.next = next;
    }

    @NonNull
    public WriteResponse set(Key key, Value value) {
        Double curTime = 0.0;
        Value curLevelValue = cache.get(key);
        curTime += levelCacheData.getReadTime();
        if (!value.equals(curLevelValue)) {
            cache.set(key, value);
            curTime += levelCacheData.getWriteTime();
        }

        if (next != null) {
            curTime += next.set(key, value).getTimeTaken();
        }

        return new WriteResponse(curTime);
    }

    @NonNull
    public ReadResponse<Value> get(Key key) {
        Double curTime = 0.0;
        Value curLevelValue = cache.get(key);
        curTime += levelCacheData.getReadTime();

        // L1 -> L2 -> L3 -> L4
        if (curLevelValue == null && next != null) {
            ReadResponse<Value> nextResponse = next.get(key);
            curTime += nextResponse.getTotalTime();
            curLevelValue = nextResponse.getValue();
            if (curLevelValue != null) {
                cache.set(key, curLevelValue);
                curTime += levelCacheData.getWriteTime();
            }
        }

        return new ReadResponse<>(curLevelValue, curTime);
    }

//    @NonNull
//    public List<Double> getUsages() {
//        final List<Double> usages;
//        if (next == null) {
//            usages = Collections.emptyList();
//        } else {
//            usages = next.getUsages();
//        }
//
//        usages.add(0, cache.getCurrentUsage());
//        return usages;
//    }
}