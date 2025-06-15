package com.lld.feature;

import com.lld.feature.repository.InMemoryFlagStore;
import com.lld.feature.service.FeatureFlagManager;
import com.lld.feature.service.SearchService;

public class FeatureFlagDemo {
    public static void main(String[] args) {
        FeatureFlagManager manager = new FeatureFlagManager(new InMemoryFlagStore());

        String feature = "NEW_SEARCH_UI";
        String userA = "user123";
        String userB = "user456";

        manager.enableForUser(feature, userA);

        SearchService searchService = new SearchService(manager);

        System.out.println("== User A ==");
        searchService.search(userA, "laptop deals");

        System.out.println("== User B ==");
        searchService.search(userB, "laptop deals");
    }
}
