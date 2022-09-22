package com.game.service;

import com.game.dto.PlayerDto;
import com.game.entity.PlayerEntity;
import com.game.mappers.PlayerMapper;
import com.game.repository.PlayerRepository;
import com.game.service.exceptions.PlayerNotFoundException;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
        try {
            PlayerEntity playerEntity = playerRepository.getOne(playerId);
            return playerMapper.mapEntityToDto(playerEntity);
        } catch (EntityNotFoundException e) {
            throw new PlayerNotFoundException(String.valueOf(playerId));
        }
    }

    @Transactional
    public List<PlayerDto> getAllPlayers() {
        List<PlayerEntity> playerEntities = playerRepository.findAll();
        return playerMapper.mapEntityListToDtoList(playerEntities);
    }

    @Transactional
    public PlayerDto changePlayer(PlayerDto playerDtoWithNewData) {
        // Получаем игрока из хранилища
        long playerId = playerDtoWithNewData.getId();
        Optional<PlayerEntity> sourcePlayerEntityOptional = playerRepository.findById(playerId);
        PlayerEntity sourcePlayerEntity = sourcePlayerEntityOptional.orElseThrow(() -> new PlayerNotFoundException(String.valueOf(playerId)));
        // Обновляем данные по игроку
        playerMapper.mapDtoToSourceEntity(playerDtoWithNewData, sourcePlayerEntity);
        setupPlayerLevelToEntity(playerDtoWithNewData, sourcePlayerEntity);
        // Сохраняем и возвращаем
        PlayerEntity updatedPlayerEntity = playerRepository.save(sourcePlayerEntity);
        return playerMapper.mapEntityToDto(updatedPlayerEntity);
    }

    @Transactional
    public void deletePlayer(long playerId) {
        try {
            playerRepository.deleteById(playerId);
        } catch (EmptyResultDataAccessException e) {
            throw new PlayerNotFoundException(String.valueOf(playerId));
        }
    }

    @Transactional
    public PlayerDto createPlayer(PlayerDto newPlayerDto) {
        // Создание новой сущности для хранилища
        PlayerEntity newPlayerEntity = playerMapper.mapDtoToNewEntity(newPlayerDto);
        setupPlayerLevelToEntity(newPlayerDto, newPlayerEntity);
        // Сохранение сущности в хранилище
        PlayerEntity resultEntity = playerRepository.save(newPlayerEntity);
        // Возврат результата
        return playerMapper.mapEntityToDto(resultEntity);
    }

    private void setupPlayerLevelToEntity(final PlayerDto playerDto, PlayerEntity playerEntity) {

        int experience;
        if (playerDto.getExperience() != null) {
            experience = playerDto.getExperience();
        } else {
            experience = playerEntity.getExperience();
        }

        int level = calculateLevel(experience);
        int untilNextLevel = calculateUntilNextLevel(level, experience);
        playerEntity.setLevel(level);
        playerEntity.setUntilNextLevel(untilNextLevel);
    }

    public int getAllPlayersCount() {
        return getAllPlayers().size();
    }

    private int calculateLevel(int experience) {
        return (((int) Math.sqrt(2500 + 200 * experience)) - 50) / 100;
    }

    private int calculateUntilNextLevel(int level, int experience) {
        return 50 * (level + 1) * (level + 2) - experience;
    }
}
