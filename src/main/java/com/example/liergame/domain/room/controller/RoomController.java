package com.example.liergame.domain.room.controller;

import com.example.liergame.domain.room.entity.*;
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
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;

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
        members.get(0).setLier();
        memberRepository.save(members.get(0));
        List<MemberResponse> memberResponses = members.stream()
                .map(member -> {
                    String subject = member.isLier() ? "lier" : room.getSubject().getSubject();
                    return new MemberResponse(member.getName(), subject);
                })
                .collect(Collectors.toList());
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new StartResponse(Type.START, memberResponses)));
    }

    @MessageMapping("/vote/{roomId}")
    public void vote(@DestinationVariable String roomId,
                     @Payload VoteRequest request) throws JsonProcessingException {
        Room room = roomRepository.findByCode(roomId)
                .orElseThrow(IllegalArgumentException::new);
        Member sender = memberRepository.findByRoomAndName(room, request.getUsername()).orElseThrow(IllegalArgumentException::new);
        room.getMember().stream()
                .filter(member -> member.getName().equals(request.getUsername()))
                .map(member -> {
                    voteRepository.findByMember(sender)
                            .ifPresentOrElse(vote -> {
                                        vote.setVotedMember(member);
                                        voteRepository.save(vote);
                                    },
                                    () -> voteRepository.save(new Vote(null, sender, member)));
                    return member;
                })
                .collect(Collectors.toList());
        List<UserVoteResponse> userVoteResponseList = room.getMember()
                .stream()
                .map(member -> new UserVoteResponse(voteRepository.countVoteByMember(member).intValue(), member.getName()))
                .collect(Collectors.toList());
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new VoteResponse(Type.VOTE, userVoteResponseList)));
    }

    @MessageMapping("/game/finish/{roomId}")
    public void finishGame(@DestinationVariable String roomId) throws JsonProcessingException {
        Room room = roomRepository.findByCode(roomId)
                .orElseThrow(IllegalArgumentException::new);

        Member dier = memberRepository.findFirstByRoomOrderByVoted(room);
        String message = dier.isLier() ? "시민 승리!" : "라이어 승리!";
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new GameSetMessage(message)));
    }

}
