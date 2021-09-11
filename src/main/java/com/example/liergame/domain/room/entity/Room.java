package com.example.liergame.domain.room.entity;

import com.example.liergame.domain.topic.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Member> member;

    private boolean isStarted;
}
