package com.example.liergame.domain.game.service;

import com.example.liergame.domain.room.entity.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private final RoomRepository roomRepository;

    @Override
    public void startGame(Long roomId) {
        roomRepository.findById(roomId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
