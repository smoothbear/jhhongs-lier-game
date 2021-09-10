package com.example.liergame.domain.room.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Type type = Type.START;

    private String name;

    @Setter
    private String subject;

}
