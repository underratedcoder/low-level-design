package com.lld.feature.repository;

import com.lld.feature.model.FeatureFlag;
import com.lld.feature.model.UserContext;

public interface IFlagStore {
    boolean isFeatureEnabled(FeatureFlag flag, UserContext user);
    void enableGlobally(FeatureFlag flag);
    void disableGlobally(FeatureFlag flag);
    void enableForUser(FeatureFlag flag, String userId);
    void disableForUser(FeatureFlag flag, String userId);
}
