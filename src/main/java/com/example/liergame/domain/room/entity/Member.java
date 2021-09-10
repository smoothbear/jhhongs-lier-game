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
public class Member implements Comparable<Member> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private int count;

    private boolean isLier;

    public void setLier() {
        isLier = true;
    }
    public Member addCount() {
        this.count += 1;
        return this;
    }

    @Override
    public int compareTo(Member o) {
        return o.count - count;
    }
}
