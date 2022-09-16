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
        System.out.println("Запрос на изменение игрока. Данные из запроса: " + playerDtoWithNewData);
        // Получаем старые данные по игроку
        PlayerEntity sourcePlayerEntity = playerRepository.getOne(playerDtoWithNewData.getId());
        // Готовим новую сущность по игроку
        PlayerEntity playerEntityWithNewData = playerMapper.mapDtoToEntity(playerDtoWithNewData);
        playerEntityWithNewData.setLevel(sourcePlayerEntity.getLevel());
        playerEntityWithNewData.setUntilNextLevel(sourcePlayerEntity.getUntilNextLevel());
        System.out.println("Обновленная сущность игрока: " + playerEntityWithNewData);
        // Сохраняем и возвращаем
        PlayerEntity updatedPlayerEntity = playerRepository.save(playerEntityWithNewData);
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
