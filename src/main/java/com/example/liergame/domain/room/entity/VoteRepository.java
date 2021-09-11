package com.example.liergame.domain.room.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Long countVoteByMember(Member member);
    Optional<Vote> findByMember(Member member);
}
