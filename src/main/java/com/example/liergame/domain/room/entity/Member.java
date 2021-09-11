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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private boolean isLier;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "votedMember")
    private List<Vote> voted;

    public void setLier() {
        isLier = true;
    }

}
