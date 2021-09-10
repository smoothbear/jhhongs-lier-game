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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoomController {

    private final RoomService roomService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;

    @PostMapping("/room")
    public String createRoom(@RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @GetMapping("/chatroom/{roomId}")
    public List<String> getMemberList(@PathVariable String roomId) {
        return roomService.members(roomId);
    }

    @MessageMapping("/chatroom/{roomId}")
    public void joinChatRoom(@DestinationVariable String roomId,
                     @Payload String username) throws JsonProcessingException {
        roomService.joinRoom(roomId, username);
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new RoomResponse(Type.JOIN, username)));
    }

    @MessageMapping("/game/{roomId}")
    public void startGame(@DestinationVariable String roomId) throws JsonProcessingException {
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new RoomResponse(Type.START, "start")));
    }

}
