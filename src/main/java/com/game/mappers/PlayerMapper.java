package com.game.mappers;

import com.game.dto.PlayerDto;
import com.game.entity.PlayerEntity;
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
        playerDto.setBirthday(entity.getBirthday());
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

    public PlayerEntity mapDtoToEntity(PlayerDto playerDto) {
        PlayerEntity playerEntity = new PlayerEntity();

        playerEntity.setId(playerDto.getId());
        playerEntity.setName(playerDto.getName());
        playerEntity.setRace(playerDto.getRace());
        playerEntity.setProfession(playerDto.getProfession());
        playerEntity.setBirthday(playerDto.getBirthday());
        playerEntity.setBanned(playerDto.isBanned());
        playerEntity.setExperience(playerDto.getExperience());
        playerEntity.setTitle(playerEntity.getTitle());

        return playerEntity;
    }
}
