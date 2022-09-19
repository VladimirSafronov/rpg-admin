package com.game.controller;

import com.game.dto.PlayerDto;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/rest/players")
public class PlayersController {

    private final PlayerService playerService;

    @Autowired
    public PlayersController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * REST API - GET /rest/players
     *
     * @return list of players
     */
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getPlayers(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "title", required = false, defaultValue = "") String title,
            @RequestParam(name = "banned", required = false) Boolean banned
//            @RequestParam(name = "birthday", required = false)Date birthday
            ) {
        // Создать фильтр
        PlayerFilter filter = new PlayerFilter();
        filter.setName(name);
        filter.setTitle(title);
        filter.setBanned(banned);
//        filter.setBirthday(birthday);
        // Получить список игроков из нашего сервиса
        List<PlayerDto> players = playerService.getAllPlayers();
        // Отфильтровать список игроков
        List<PlayerDto> filteredPlayers = filterPlayers(filter, players);
        return ResponseEntity.ok(filteredPlayers);
    }

    private List<PlayerDto> filterPlayers(PlayerFilter filter, List<PlayerDto> players) {
        return players.stream()
                .filter(player -> player.getName().toLowerCase().contains(filter.getName().toLowerCase()))
                .filter(player -> player.getTitle().toLowerCase().contains(filter.getTitle().toLowerCase()))
                .filter(player -> {
                    if(filter.isBanned() != null) {
                        return player.isBanned() == filter.isBanned();
                    }
                    else {
                        return true;
                    }
                })
//                .filter(player -> {
//                    if(filter.getBirthday() != null) {
//                        return player.getBirthday().equals(filter.getBirthday());
//                    }
//                    else {
//                        return true;
//                    }
//                })
                .collect(Collectors.toList());
    }

    /**
     * REST API - GET /rest/players/{playerId}
     *
     * @return Player
     */
    @GetMapping(value = "/{playerId}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable long playerId) {
        System.out.println(">> getPlayer(): playerId = " + playerId);
        PlayerDto player = playerService.getPlayer(playerId);
        return ResponseEntity.ok(player);
    }

    /**
     * REST API - POST /rest/players/{playerId}
     *
     * @return Updated player
     */
    @PostMapping(value = "/{playerId}")
    public ResponseEntity<PlayerDto> changePlayer(
            @PathVariable long playerId, @RequestBody PlayerDto player) {
        player.setId(playerId);
        PlayerDto updatedPlayer = playerService.changePlayer(player);
        return ResponseEntity.ok(updatedPlayer);
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(
            @RequestBody PlayerDto player
    ) {
        PlayerDto result = playerService.createPlayer(player);
        return ResponseEntity.ok(result);
    }

    /**
     * REST API - DELETE /rest/players/{playerId}
     *
     * @param playerId ID of player
     * @return HTTP code 200 and message
     */
    @DeleteMapping(value = "{playerId}")
    public ResponseEntity<String> deletePlayer(@PathVariable long playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.ok(String.format("Player with id %d deleted!", playerId));
    }

    /**
     * REST API - GET /rest/players/count
     *
     * @return list of players
     */
    @GetMapping(value = "/count")
    public ResponseEntity<Integer> getPlayersCount() {
        // Debug
        System.out.println(">> getPlayersCount()");

        int count = playerService.getAllPlayersCount();
        return ResponseEntity.ok(count);
    }
}
