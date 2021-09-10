package com.example.liergame.domain.room.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
    JOIN("join"),
    START("start");

    private final String type;
}
