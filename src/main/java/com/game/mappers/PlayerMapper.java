package com.game.mappers;

import com.game.dto.PlayerDto;
import com.game.entity.PlayerEntity;
import java.sql.Date;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    public PlayerDto mapEntityToDto(PlayerEntity entity) {

        PlayerDto playerDto = new PlayerDto();

        playerDto.setId(entity.getId());
        playerDto.setName(entity.getName());
        playerDto.setRace(entity.getRace());
        playerDto.setProfession(entity.getProfession());
        playerDto.setBirthday(entity.getBirthday().getTime());
        playerDto.setBanned(entity.isBanned());
        playerDto.setExperience(entity.getExperience());
        playerDto.setLevel(entity.getLevel());
        playerDto.setUntilNextLevel(entity.getUntilNextLevel());
        playerDto.setTitle(entity.getTitle());

        return playerDto;
    }

    public List<PlayerDto> mapEntityListToDtoList(List<PlayerEntity> entities) {
        List<PlayerDto> players = new ArrayList<>();
        for (PlayerEntity entity : entities) {
            players.add(mapEntityToDto(entity));
        }
        return players;
    }

    public PlayerEntity mapDtoToNewEntity(PlayerDto playerDto) {
        PlayerEntity playerEntity = new PlayerEntity();
        mapDtoToEntity(playerDto, playerEntity);
        return playerEntity;
    }

    public void mapDtoToSourceEntity(PlayerDto playerDto, PlayerEntity sourceEntity) {

        if (playerDto.getExperience() != null) {
            sourceEntity.setExperience(playerDto.getExperience());
        }

        if (playerDto.getProfession() != null) {
            sourceEntity.setProfession(playerDto.getProfession());
        }

        if (playerDto.getRace() != null) {
            sourceEntity.setRace(playerDto.getRace());
        }

        if (playerDto.getName() != null) {
            sourceEntity.setName(playerDto.getName());
        }

        if (playerDto.getTitle() != null) {
            sourceEntity.setTitle(playerDto.getTitle());
        }

        if (playerDto.isBanned() != null) {
            sourceEntity.setBanned(playerDto.isBanned());
        }

        if (playerDto.getBirthday() != null) {
            sourceEntity.setBirthday(new Date(playerDto.getBirthday()));
        }
    }

    private void mapDtoToEntity(PlayerDto playerDto, PlayerEntity sourceEntity) {
        sourceEntity.setName(playerDto.getName());
        sourceEntity.setTitle(playerDto.getTitle());
        sourceEntity.setRace(playerDto.getRace());
        sourceEntity.setProfession(playerDto.getProfession());
        sourceEntity.setExperience(playerDto.getExperience());
        sourceEntity.setBanned(playerDto.isBanned());
        sourceEntity.setBirthday(new Date(playerDto.getBirthday()));
    }
}
