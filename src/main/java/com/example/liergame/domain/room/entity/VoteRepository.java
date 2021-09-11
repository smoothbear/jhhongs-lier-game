package com.example.liergame.domain.room.entity;

import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Long countVoteByMember(Member member);
}
