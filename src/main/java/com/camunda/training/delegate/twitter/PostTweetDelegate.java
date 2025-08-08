package com.camunda.training.delegate.twitter;


import com.camunda.training.service.TwitterService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("postTweetDelegate")
public class PostTweetDelegate implements JavaDelegate {

    @Autowired
    private final TwitterService twitterService;

    public PostTweetDelegate(TwitterService twitterService) {
        this.twitterService = twitterService;

    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String tweet = (String) delegateExecution.getVariable("tweetText");

        if (tweet == null || tweet.isEmpty()) {
            throw new IllegalArgumentException("Tweet text cannot be null or empty");
        }
        twitterService.postTweet(tweet);

    }
}
