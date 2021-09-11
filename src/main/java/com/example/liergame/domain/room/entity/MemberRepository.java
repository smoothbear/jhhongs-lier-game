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
    void deleteAllByRoom(Room room);
    @Query("select count(a) as cnt from Vote a where Room = ?1 group by a.member.name order by cnt desc")
    List<Member> findFirst(Room room);
}
