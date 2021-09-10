package com.example.liergame.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(401, "Invalid Token");

    private final int status;
    private final String message;
}
