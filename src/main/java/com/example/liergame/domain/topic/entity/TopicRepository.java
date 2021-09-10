package com.example.liergame.domain.topic.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends CrudRepository<Topic, Long> {
    Optional<Topic> findByName(String name);
    List<Topic> findAllBy();
}
