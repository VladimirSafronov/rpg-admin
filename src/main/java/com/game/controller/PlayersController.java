package com.game.controller;

import com.game.dto.PlayerDto;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return list of players
     */
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getPlayers() {
        // Debug
        System.out.println(">> getPlayers()");
        List<PlayerDto> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    /**
     * REST API - GET /rest/players/{playerId}
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
     * @return Updated player
     */
    @PostMapping(value = "/{playerId}")
    public ResponseEntity<PlayerDto> changePlayer(
            @PathVariable long playerId, @RequestBody PlayerDto player) {
        player.setId(playerId);
        PlayerDto updatedPlayer = playerService.changePlayer(player);
        return ResponseEntity.ok(updatedPlayer);
    }

    /**
     * REST API - DELETE /rest/players/{playerId}
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
