package com.example.liergame.domain.room.controller;

import com.example.liergame.domain.room.payload.CreateRoomRequest;
import com.example.liergame.domain.room.payload.RoomResponse;
import com.example.liergame.domain.room.payload.Type;
import com.example.liergame.domain.room.service.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoomController {

    private final RoomService roomService;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @PostMapping("/room")
    public String createRoom(@RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @MessageMapping("/chatroom/{roomId}")
    public void joinChatRoom(@DestinationVariable Long roomId,
                     @Payload String username) throws JsonProcessingException {
        simpMessageSendingOperations.convertAndSend("/pub/game/" + roomId.toString(), objectMapper.writeValueAsString(new RoomResponse(Type.JOIN, username)));
    }

    @MessageMapping("/game/{roomId}")
    public void startGame(@DestinationVariable Long roomId) throws JsonProcessingException {
        simpMessageSendingOperations.convertAndSend("/pub/game/" + roomId.toString(), objectMapper.writeValueAsString(new RoomResponse(Type.START, "start")));
    }

}
