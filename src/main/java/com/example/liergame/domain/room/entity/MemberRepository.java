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
    @Query(value = "select count(name) cnt from vote inner join member on vote.voted_name = member.id where room_id=?1 group by name order by cnt desc limit 1", nativeQuery = true)

    void deleteAllByRoom(Room room);
}
