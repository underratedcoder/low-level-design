package com.lld.feature.service;

public class SearchService {

    private final FeatureFlagManager flagManager;
    private final String featureName = "NEW_SEARCH_UI";

    public SearchService(FeatureFlagManager flagManager) {
        this.flagManager = flagManager;
    }

    public void search(String userId, String query) {
        if (flagManager.isFeatureEnabled(featureName, userId)) {
            runNewSearch(query);
        } else {
            runLegacySearch(query);
        }
    }

    private void runNewSearch(String query) {
        System.out.println("🔍 [NEW] Performing vector-based search for: " + query);
    }

    private void runLegacySearch(String query) {
        System.out.println("🔍 [OLD] Performing keyword-based search for: " + query);
    }
}
