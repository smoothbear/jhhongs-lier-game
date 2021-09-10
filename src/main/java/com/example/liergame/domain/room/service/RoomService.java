package com.example.liergame.domain.room.service;

import com.example.liergame.domain.room.payload.CreateRoomRequest;

import java.util.List;

public interface RoomService {
    String createRoom(CreateRoomRequest request);
    List<String> members(String roomId);
    void joinRoom(String roomId, String username);
    void deleteRoom(Long id);
}
