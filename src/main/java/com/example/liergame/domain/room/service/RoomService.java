package com.example.liergame.domain.room.service;

import com.example.liergame.domain.room.payload.CreateRoomRequest;

public interface RoomService {
    String createRoom(CreateRoomRequest request);
    void joinRoom(Long roomId, String username);
    void deleteRoom(Long id);
}
