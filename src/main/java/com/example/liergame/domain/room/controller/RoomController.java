package com.example.liergame.domain.room.controller;

import com.example.liergame.domain.room.entity.*;
import com.example.liergame.domain.room.payload.*;
import com.example.liergame.domain.room.service.RoomService;
import com.example.liergame.domain.topic.entity.Subject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoomController {

    private final RoomService roomService;
    private final Random RANDOM = new Random();
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
        List<Member> updatedMembers = members.stream().map(member -> member.setLier(false))
                .collect(Collectors.toList());
        memberRepository.saveAll(updatedMembers);
        Collections.shuffle(members);
        members.get(0).setLier(true);
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
                .filter(member -> member.getName().equals(request.getSuspendName()))
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
                .map(member -> new UserVoteResponse(voteRepository.countVoteByVotedMember(member).intValue(), member.getName()))
                .collect(Collectors.toList());
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new VoteResponse(Type.VOTE, userVoteResponseList)));
    }

    @Transactional
    @MessageMapping("/game/finish/{roomId}")
    public void finishGame(@DestinationVariable String roomId) throws JsonProcessingException {
        Room room = roomRepository.findByCode(roomId)
                .orElseThrow(IllegalArgumentException::new);

        List<Member> members = room.getMember();

        int mostVote = members.stream()
                .map(member -> member.getVoted().size())
                .sorted()
                .collect(Collectors.toList()).get(members.size() - 1);
        System.out.println("MostSize: " + mostVote);
        Member dier = members.stream().filter(member -> member.getVoted().size() == mostVote)
                .map(member -> {
                    System.out.println(member.getVoted().size());
                    return member;
                })
                .findFirst().orElseThrow(IllegalArgumentException::new);

        System.out.println(dier.getName() + " : " + dier.isLier());
        String message = dier.isLier() ? "user" : "lier";
        room.getMember().forEach(voteRepository::deleteAllByMember);
        List<Subject> subjects = room.getSubject().getTopic().getSubject();
        Subject subject = subjects.get(RANDOM.nextInt(subjects.size()));
        room.setSubject(subject);
        roomRepository.save(room);
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.writeValueAsString(new VoteEndResponse(Type.VOTE_END, message)));
    }

    @MessageMapping("/game/lier/{roomId}")
    public void lierMatch(@DestinationVariable String roomId,
                          String subject) {
        Room room = roomRepository.findByCode(roomId)
                .orElseThrow(IllegalArgumentException::new);
        template.convertAndSend("/sub/chatroom/" + roomId, objectMapper.createObjectNode().put("isMatch", subject.equals(room.getSubject().getSubject())));
    }

}
