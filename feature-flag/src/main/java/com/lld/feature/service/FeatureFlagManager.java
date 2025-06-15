package com.lld.feature.service;

import com.lld.feature.model.FeatureFlag;
import com.lld.feature.model.UserContext;
import com.lld.feature.repository.IFlagStore;

public class FeatureFlagManager {

    private final IFlagStore flagStore;

    public FeatureFlagManager(IFlagStore store) {
        this.flagStore = store;
    }

    public boolean isFeatureEnabled(String featureName, String userId) {
        FeatureFlag flag = new FeatureFlag(featureName);
        return flagStore.isFeatureEnabled(flag, new UserContext(userId));
    }

    public void enableGlobally(String featureName) {
        flagStore.enableGlobally(new FeatureFlag(featureName));
    }

    public void disableGlobally(String featureName) {
        flagStore.disableGlobally(new FeatureFlag(featureName));
    }

    public void enableForUser(String featureName, String userId) {
        flagStore.enableForUser(new FeatureFlag(featureName), userId);
    }

    public void disableForUser(String featureName, String userId) {
        flagStore.disableForUser(new FeatureFlag(featureName), userId);
    }
}
