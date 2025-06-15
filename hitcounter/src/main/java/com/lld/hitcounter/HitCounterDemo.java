package com.lld.hitcounter;

public class HitCounterDemo {
    public static void main(String[] args) throws InterruptedException {
        HitCounter counter = new HitCounter(10); // 10-second window

        counter.count("api1", 100);
        counter.count("api1", 101);
        counter.count("api2", 102);
        counter.count("api1", 109);

        System.out.println(counter.getCount("api1", 110)); // Should return 2 (hits at 101, 109)
        System.out.println(counter.getCount("api2", 110)); // Should return 1
        System.out.println(counter.getAllCount(110));           // Should return 3
    }
}
