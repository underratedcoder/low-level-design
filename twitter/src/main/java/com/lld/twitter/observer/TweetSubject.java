package com.lld.twitter.observer;

import com.lld.twitter.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetSubject {
    private List<TweetObserver> observers = new ArrayList<>();
    
    public void addObserver(TweetObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(TweetObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(String authorId, Tweet tweet) {
        for (TweetObserver observer : observers) {
            observer.onTweetCreated(authorId, tweet);
        }
    }
}