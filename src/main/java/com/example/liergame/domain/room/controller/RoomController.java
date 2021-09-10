package com.example.liergame.domain.room.controller;

import com.example.liergame.domain.room.entity.Member;
import com.example.liergame.domain.room.entity.Room;
import com.example.liergame.domain.room.entity.RoomRepository;
import com.example.liergame.domain.room.payload.*;
import com.example.liergame.domain.room.service.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoomController {

    private final RoomService roomService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;
    private final RoomRepository roomRepository;

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
        Room room = roomRepository.findByCode(roomId)
                .orElseThrow(IllegalArgumentException::new);

        List<Member> members = room.getMember();
        Collections.shuffle(members);
        List<MemberResponse> memberResponses = members.stream()
                .map(member -> new MemberResponse(member.getName(), room.getSubject().getSubject()))
                .collect(Collectors.toList());
        memberResponses.get(0).setSubject("lier");
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new StartResponse(Type.START, memberResponses)));
    }

}
