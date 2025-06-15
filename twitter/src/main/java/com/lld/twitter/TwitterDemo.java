package com.lld.twitter;

import com.lld.twitter.model.Tweet;
import com.lld.twitter.model.User;

import java.util.List;

public class TwitterDemo {
    public static void main(String[] args) {
        TwitterSystem twitter = new TwitterSystem();

        try {
            // Create users
            User alice = twitter.createUser("Alice", "alice@email.com");
            User bob = twitter.createUser("Bob", "bob@email.com");
            User charlie = twitter.createUser("Charlie", "charlie@email.com");

            System.out.println("Created users: " + alice.getName() + ", " + bob.getName() + ", " + charlie.getName());

            // Follow relationships
            twitter.followUser(alice.getId(), bob.getId());    // Alice follows Bob
            twitter.followUser(alice.getId(), charlie.getId()); // Alice follows Charlie
            twitter.followUser(bob.getId(), charlie.getId());   // Bob follows Charlie

            System.out.println("Alice follows: " + twitter.getFollowing(alice.getId()).size() + " users");
            System.out.println("Bob has: " + twitter.getFollowers(bob.getId()).size() + " followers");

            // Create tweets
            Tweet tweet1 = twitter.createTweet(bob.getId(), "Hello world! #firsttweet");
            Tweet tweet2 = twitter.createTweet(charlie.getId(), "Learning #java today!");
            Tweet tweet3 = twitter.createTweet(bob.getId(), "Another tweet from Bob #coding");
            Tweet tweet4 = twitter.createTweet(charlie.getId(), "More #java and #coding practice!");

            System.out.println("Created " + 4 + " tweets");

            // Like and comment
            twitter.likeTweet(tweet1.getId(), alice.getId());
            twitter.commentOnTweet(tweet1.getId(), alice.getId(), "Great first tweet!");
            twitter.likeTweet(tweet2.getId(), alice.getId());
            twitter.likeTweet(tweet2.getId(), bob.getId());

            System.out.println("Tweet 1 likes: " + twitter.getTweet(tweet1.getId()).getLikeCount());
            System.out.println("Tweet 2 likes: " + twitter.getTweet(tweet2.getId()).getLikeCount());
            System.out.println("Tweet 1 comments: " + twitter.getTweet(tweet1.getId()).getComments().size());

            // Get Alice's timeline (should see Bob's and Charlie's tweets)
            List<Tweet> aliceTimeline = twitter.getUserTimeline(alice.getId());
            System.out.println("Alice's timeline has " + aliceTimeline.size() + " tweets");

            // Search by hashtag
            List<Tweet> javaTweets = twitter.searchTweetsByHashtag("java");
            System.out.println("Found " + javaTweets.size() + " tweets with #java");

            List<Tweet> codingTweets = twitter.searchTweetsByHashtag("coding");
            System.out.println("Found " + codingTweets.size() + " tweets with #coding");

            List<Tweet> firstTweetTags = twitter.searchTweetsByHashtag("firsttweet");
            System.out.println("Found " + firstTweetTags.size() + " tweets with #firsttweet");

            // Test user's own tweets
            List<Tweet> bobTweets = twitter.getUserTweets(bob.getId());
            System.out.println("Bob has created " + bobTweets.size() + " tweets");

            // Test follow status
            System.out.println("Alice follows Bob: " + twitter.isFollowing(alice.getId(), bob.getId()));
            System.out.println("Bob follows Alice: " + twitter.isFollowing(bob.getId(), alice.getId()));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}