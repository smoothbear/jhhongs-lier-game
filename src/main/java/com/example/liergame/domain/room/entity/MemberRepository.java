package com.example.liergame.domain.room.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
    Optional<Member> findByRoomAndName(Room room, String name);
    Member findTopByRoomOrderByVoted(Room room);

    @Query(value = "delete from Member where Member.room = ?1")
    void deleteAllByRoom(Room room);
}
