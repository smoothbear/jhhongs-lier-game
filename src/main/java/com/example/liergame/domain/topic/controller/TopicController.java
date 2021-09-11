package com.example.liergame.domain.topic.controller;

import com.example.liergame.domain.topic.payload.TopicResponse;
import com.example.liergame.domain.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/topic")
    public TopicResponse getTopics() {
        return topicService.getTopics();
    }

    @GetMapping("/subject/{roomId}")
    public List<String> getSubjects(@PathVariable String roomId) {
        return topicService.getSubjects(roomId);
    }

}
