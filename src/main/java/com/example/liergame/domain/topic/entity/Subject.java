package com.example.liergame.domain.topic.entity;

import com.example.liergame.domain.room.entity.Member;
import com.example.liergame.domain.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "subject")
    private List<Room> rooms;

    @OneToMany(mappedBy = "subject")
    private List<Member> members;

}
