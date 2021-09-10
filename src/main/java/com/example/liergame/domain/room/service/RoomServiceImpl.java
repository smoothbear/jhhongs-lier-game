package com.example.liergame.domain.room.service;

import com.example.liergame.domain.room.entity.Member;
import com.example.liergame.domain.room.entity.MemberRepository;
import com.example.liergame.domain.room.entity.Room;
import com.example.liergame.domain.room.entity.RoomRepository;
import com.example.liergame.domain.room.payload.CreateRoomRequest;
import com.example.liergame.domain.topic.entity.Subject;
import com.example.liergame.domain.topic.entity.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private static final Random RANDOM = new Random();
    private final TopicRepository topicRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Override
    public String createRoom(CreateRoomRequest request) {
        List<Subject> subjects = topicRepository.findByName(request.getName())
                .orElseThrow(IllegalArgumentException::new).getSubject();
        Subject subject = subjects.get(RANDOM.nextInt(subjects.size()));
        String code = UUID.randomUUID().toString();
        roomRepository.save(Room.builder()
                .code(code)
                .name(request.getName())
                .subject(subject)
                .build());
        return code;
    }

    @Override
    public void joinRoom(Long roomId, String username) {
        memberRepository.save(Member.builder()
                .name(username)
                .room(roomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new))
                .subject(null)
                .build());
    }

    @Override
    public void deleteRoom(Long id) {

    }
}
