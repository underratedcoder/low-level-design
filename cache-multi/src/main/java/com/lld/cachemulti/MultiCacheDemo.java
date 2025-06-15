package com.lld.cachemulti;

import com.lld.cachemulti.cache.MultiLevelCache;
import com.lld.cachemulti.cache.model.LevelCacheData;
import com.lld.cachemulti.cache.model.ReadResponse;
import com.lld.cachemulti.cache.model.WriteResponse;
import com.lld.cachemulti.cache.policies.LRUEvictionPolicy;
import com.lld.cachemulti.cache.provider.Cache;
import com.lld.cachemulti.cache.provider.LevelCache;
import com.lld.cachemulti.cache.storage.InMemoryStorage;

public class MultiCacheDemo {

    public static void main(String[] args) {
        Cache<String, String> c1 = createCache(10);
        Cache<String, String> c2 = createCache(20);

        LevelCacheData cl1 = new LevelCacheData(1, 3);
        LevelCacheData cl2 = new LevelCacheData(2, 4);

        LevelCache<String, String> l2Cache = new LevelCache<>(cl2, c2, null);
        LevelCache<String, String> l1Cache = new LevelCache<>(cl1, c1, l2Cache);

        MultiLevelCache<String, String> cacheService = new MultiLevelCache<>(l1Cache, 5);

        WriteResponse w1 = cacheService.set("k1", "v1");
        WriteResponse w2 = cacheService.set("k2", "v2");

        ReadResponse<String> r1 = cacheService.get("k1");

        ReadResponse<String> r2 = cacheService.get("k2");

        // Explicitly remove key from l1 for testing.
        c1.set("k1", null);

        ReadResponse<String> r1AfterRemovalFromL1 = cacheService.get("k1");

        ReadResponse<String> reRead = cacheService.get("k1");

        WriteResponse reWritingValue = cacheService.set("k1", "v1");
    }

    private static Cache<String, String> createCache(int capacity) {
        return new Cache<>(
                new LRUEvictionPolicy<>(),
                new InMemoryStorage<>(capacity));
    }
}
