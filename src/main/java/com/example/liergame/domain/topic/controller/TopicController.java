package com.example.liergame.domain.topic.controller;

import com.example.liergame.domain.topic.payload.TopicResponse;
import com.example.liergame.domain.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/topic")
    public TopicResponse getTopics() {
        return topicService.getTopics();
    }
}
