package com.example.liergame.domain.topic.service;

import com.example.liergame.domain.room.entity.Room;
import com.example.liergame.domain.room.entity.RoomRepository;
import com.example.liergame.domain.topic.entity.Subject;
import com.example.liergame.domain.topic.entity.Topic;
import com.example.liergame.domain.topic.entity.TopicRepository;
import com.example.liergame.domain.topic.payload.TopicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final RoomRepository roomRepository;

    @Override
    public TopicResponse getTopics() {
        return new TopicResponse(topicRepository.findAllBy()
                .stream().map(Topic::getName)
                .collect(Collectors.toList()));
    }

    @Override
    public String getSubjects(String roomId) {
        Room room = roomRepository.findByCode(roomId)
                .orElseThrow(IllegalAccessError::new);
        return room.getSubject().getSubject();
    }
}
