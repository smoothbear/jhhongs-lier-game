package com.example.liergame.domain.topic.service;

import com.example.liergame.domain.topic.payload.TopicResponse;

import java.util.List;

public interface TopicService {
    TopicResponse getTopics();
    List<String> getSubjects(String roomId);
}
