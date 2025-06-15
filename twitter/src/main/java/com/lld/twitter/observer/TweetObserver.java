package com.lld.twitter.observer;

import com.lld.twitter.model.Tweet;

public interface TweetObserver {
    void onTweetCreated(String authorId, Tweet tweet);
}