package com.lld.twitter.service;

import com.lld.twitter.model.Timeline;
import com.lld.twitter.model.Tweet;
import com.lld.twitter.observer.TweetObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimelineService implements TweetObserver {
    private Map<String, Timeline> timelines = new HashMap<>();
    private TweetService tweetService;
    private FollowService followService;

    public TimelineService(TweetService tweetService, FollowService followService) {
        this.tweetService = tweetService;
        this.followService = followService;
    }

    @Override
    public void onTweetCreated(String authorId, Tweet tweet) {
        // Get all followers of the tweet author
        List<String> followers = followService.getFollowers(authorId);

        // Add tweet to each follower's timeline
        for (String followerId : followers) {
            Timeline timeline = timelines.computeIfAbsent(followerId, k -> Timeline.builder()
                    .userId(k)
                    .tweetIds(new ArrayList<>())
                    .lastUpdatedAt(LocalDateTime.now())
                    .build());

            timeline.getTweetIds().add(0, tweet.getId()); // Add to front for chronological order
            timeline.setLastUpdatedAt(LocalDateTime.now());

            // Keep timeline size manageable (last 100 tweets)
            if (timeline.getTweetIds().size() > 100) {
                timeline.getTweetIds().remove(timeline.getTweetIds().size() - 1);
            }
        }
    }

    public List<Tweet> fetchTimeline(String userId) {
        List<String> following = followService.getFollowing(userId);
        List<Tweet> timelineTweets = new ArrayList<>();

        // Get tweets from all people user follows
        for (String followeeId : following) {
            List<Tweet> userTweets = tweetService.fetchByUser(followeeId);
            timelineTweets.addAll(userTweets);
        }

        // Sort by creation time (most recent first)
        timelineTweets.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));

        // Return top 50 tweets
        return timelineTweets.stream().limit(50).collect(Collectors.toList());
    }

    public Timeline getTimelineMetadata(String userId) {
        return timelines.get(userId);
    }
}