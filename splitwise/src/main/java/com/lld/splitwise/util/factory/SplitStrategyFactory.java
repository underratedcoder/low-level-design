// SplitStrategyFactory.java
package com.lld.splitwise.util.factory;

import com.lld.splitwise.enums.SplitType;
import com.lld.splitwise.util.strategy.EqualSplitStrategy;
import com.lld.splitwise.util.strategy.PercentageSplitStrategy;
import com.lld.splitwise.util.strategy.SplitStrategy;

public class SplitStrategyFactory {
    
    private SplitStrategyFactory() {}

    public SplitStrategy createStrategy(SplitType splitType) {
        switch (splitType) {
            case EQUAL:
                return new EqualSplitStrategy();
            case PERCENTAGE:
                return new PercentageSplitStrategy();
            default:
                throw new IllegalArgumentException("Unsupported split type: " + splitType);
        }
    }

    /**
     * Object Creation
     * */
    private static SplitStrategyFactory instance;

    public static synchronized SplitStrategyFactory getInstance() {
        if (instance == null) {
            instance = new SplitStrategyFactory();
        }
        return instance;
    }
}