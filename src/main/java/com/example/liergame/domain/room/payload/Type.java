package com.example.liergame.domain.room.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
    JOIN("join"),
    VOTE("vote"),
    START("start"),
    VOTE_END("vote_result");

    private final String type;
}
