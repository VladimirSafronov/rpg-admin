package com.game.service;

import com.game.dto.PlayerDto;
import com.game.entity.PlayerEntity;
import com.game.mappers.PlayerMapper;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(
            PlayerMapper playerMapper,
            PlayerRepository playerRepository) {
        this.playerMapper = playerMapper;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public PlayerDto getPlayer(long playerId) {
        PlayerEntity playerEntity = playerRepository.getOne(playerId);
        return playerMapper.mapEntityToDto(playerEntity);
    }

    @Transactional
    public List<PlayerDto> getAllPlayers() {
        List<PlayerEntity> playerEntities = playerRepository.findAll();
        return playerMapper.mapEntityListToDtoList(playerEntities);
    }

    @Transactional
    public PlayerDto changePlayer(PlayerDto playerDtoWithNewData) {
        // Получаем игрока из хранилища
        PlayerEntity sourcePlayerEntity = playerRepository.getOne(playerDtoWithNewData.getId());
        // Обновляем данные по игроку
        playerMapper.mapDtoToSourceEntity(playerDtoWithNewData, sourcePlayerEntity);
        // Сохраняем и возвращаем
        PlayerEntity updatedPlayerEntity = playerRepository.save(sourcePlayerEntity);
        return playerMapper.mapEntityToDto(updatedPlayerEntity);
    }

    @Transactional
    public void deletePlayer(long playerId) {
        playerRepository.deleteById(playerId);
    }

    public int getAllPlayersCount() {
        return getAllPlayers().size();
    }
}
