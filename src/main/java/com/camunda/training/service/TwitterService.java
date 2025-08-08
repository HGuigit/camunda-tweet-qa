package com.camunda.training.service;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TwitterService {

    private final Logger LOGGER = Logger.getLogger(TwitterService.class.getName());


    public void postTweet(String tweet) {
        LOGGER.info("Posting tweet: " + tweet);
    }
}
