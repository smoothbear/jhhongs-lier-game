package com.example.liergame.domain.topic.service;

import com.example.liergame.domain.topic.entity.Topic;
import com.example.liergame.domain.topic.entity.TopicRepository;
import com.example.liergame.domain.topic.payload.TopicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public TopicResponse getTopics() {
        return new TopicResponse(topicRepository.findAllBy()
                .stream().map(Topic::getName)
                .collect(Collectors.toList()));
    }
}
