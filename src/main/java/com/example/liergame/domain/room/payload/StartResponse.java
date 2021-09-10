package com.example.liergame.domain.room.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartResponse {

    private Type type;

    private List<MemberResponse> memberResponses;
}
