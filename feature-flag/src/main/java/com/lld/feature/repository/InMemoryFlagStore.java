package com.lld.feature.repository;

import com.lld.feature.model.FeatureFlag;
import com.lld.feature.model.UserContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlagStore implements IFlagStore {

    private final Set<FeatureFlag> globalEnabledFlags = ConcurrentHashMap.newKeySet();
    private final Map<FeatureFlag, Set<String>> perUserFlags = new ConcurrentHashMap<>();

    @Override
    public boolean isFeatureEnabled(FeatureFlag flag, UserContext user) {
        if (globalEnabledFlags.contains(flag)) {
            return true;
        }
        return perUserFlags.getOrDefault(flag, Collections.emptySet())
                           .contains(user.getUserId());
    }

    @Override
    public void enableGlobally(FeatureFlag flag) {
        globalEnabledFlags.add(flag);
    }

    @Override
    public void disableGlobally(FeatureFlag flag) {
        globalEnabledFlags.remove(flag);
    }

    @Override
    public void enableForUser(FeatureFlag flag, String userId) {
        perUserFlags
            .computeIfAbsent(flag, k -> ConcurrentHashMap.newKeySet())
            .add(userId);
    }

    @Override
    public void disableForUser(FeatureFlag flag, String userId) {
        Set<String> users = perUserFlags.get(flag);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                perUserFlags.remove(flag);
            }
        }
    }
}
