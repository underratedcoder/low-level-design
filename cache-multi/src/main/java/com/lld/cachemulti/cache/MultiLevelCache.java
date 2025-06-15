package com.lld.cachemulti.cache;

import com.lld.cachemulti.cache.model.ReadResponse;
import com.lld.cachemulti.cache.model.WriteResponse;
import com.lld.cachemulti.cache.provider.LevelCache;
import com.lld.cachemulti.cache.provider.ILevelCache;
import lombok.NonNull;

public class MultiLevelCache<Key, Value> {
    private final ILevelCache<Key, Value> levelCache;
//    private final List<Double> lastReadTimes;
//    private final List<Double> lastWriteTimes;
//    private final int lastCount;

    public MultiLevelCache(@NonNull final LevelCache<Key, Value> levelCache, final int lastCount) {
        this.levelCache = levelCache;
  //      this.lastCount = lastCount;
//        this.lastReadTimes = new ArrayList<>(lastCount);
//        this.lastWriteTimes = new ArrayList<>(lastCount);
    }

    public WriteResponse set(@NonNull final Key key, @NonNull final Value value) {
        final WriteResponse writeResponse = levelCache.set(key, value);
//        addTimes(lastWriteTimes, writeResponse.getTimeTaken());
        return writeResponse;
    }

    public ReadResponse<Value> get(@NonNull final Key key) {
        final ReadResponse<Value> readResponse = levelCache.get(key);
//        addTimes(lastReadTimes, readResponse.getTotalTime());
        return readResponse;
    }

//    private Double getAvgReadTime() {
//        return getSum(lastReadTimes)/lastReadTimes.size();
//    }
//
//    private Double getAvgWriteTime() {
//        return getSum(lastWriteTimes)/lastWriteTimes.size();
//    }

//    private void addTimes(List<Double> times, Double time) {
//        if (times.size() == this.lastCount) {
//            times.remove(0);
//        }
//
//        times.add(time);
//    }

//    private Double getSum(List<Double> times) {
//        Double sum = 0.0;
//        for (Double time: lastReadTimes) {
//            sum += time;
//        }
//        return sum;
//    }
}
