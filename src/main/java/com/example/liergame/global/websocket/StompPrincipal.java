package com.example.liergame.global.websocket;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
class StompPrincipal implements Principal {
    private final String name;

    @Override
    public String getName() { return name;}
}
