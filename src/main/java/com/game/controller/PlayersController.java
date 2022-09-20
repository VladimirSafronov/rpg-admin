package com.game.controller;

import com.game.dto.PlayerDto;
import com.game.entity.Race;
import com.game.service.PlayerService;
import java.security.spec.ECField;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;
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
      @RequestParam Map<String, String> queryParams,
      @RequestParam(name = "order", required = false) String order
  ) {
    // Создать фильтр
    PlayerFilter filter = createFilter(queryParams);
    // Получить список игроков из нашего сервиса
    List<PlayerDto> players = playerService.getAllPlayers();
    // Отфильтровать список игроков
    List<PlayerDto> filteredPlayers = filterPlayers(filter, players);
    sortPlayers(filteredPlayers, order);
    return ResponseEntity.ok(filteredPlayers);
  }

  private void sortPlayers(List<PlayerDto> playerDtos, String order) {
    if (order == null) {
      playerDtos.sort(sortById());
    } else {
      try {
        PlayerOrder playerOrder = PlayerOrder.valueOf(order);

        switch (playerOrder) {
          case ID: {
            playerDtos.sort(sortById());
            break;
          }
          case NAME: {
            playerDtos.sort(sortByName());
            break;
          }
          case BIRTHDAY: {
            playerDtos.sort(sortByBirthday());
            break;
          }
          default: {
            playerDtos.sort(sortById());
            break;
          }
        }

      } catch (Exception e) {
        playerDtos.sort(sortById());
      }
    }
  }

  private Comparator<PlayerDto> sortById() {
    return (player1, player2) -> {
      return Long.compare(player1.getId(), player2.getId());
    };
  }

  private Comparator<PlayerDto> sortByName() {
    return (player1, player2) -> {
      return player1.getName().compareTo(player2.getName());
    };
  }

  private Comparator<PlayerDto> sortByBirthday() {
    return (player1, player2) -> {
      Long player1Birthday = player1.getBirthday().getTime();
      Long player2Birthday = player2.getBirthday().getTime();
      return Long.compare(player1Birthday, player2Birthday);
    };
  }

  private PlayerFilter createFilter(Map<String, String> queryParams) {
    PlayerFilter filter = new PlayerFilter();
    filter.setName(queryParams.get(PlayerFilter.NAME));
    filter.setTitle(queryParams.get(PlayerFilter.TITLE));
    filter.setBanned(getIsBannedFilter(queryParams.get(PlayerFilter.IS_BANNED)));
    filter.setBirthdayAfter(getLong(queryParams.get(PlayerFilter.BIRTHDAY_AFTER)));
    filter.setBirthdayBefore(getLong(queryParams.get(PlayerFilter.BIRTHDAY_BEFORE)));
    filter.setRace(getRaceFilter(queryParams.get(PlayerFilter.RACE)));
    return filter;
  }

  private Race getRaceFilter(String raceFilter) {
    if (raceFilter == null) {
      return null;
    } else {
      try {
        return Race.valueOf(raceFilter);
      } catch (Exception e) {
        return null;
      }
    }
  }
  private Boolean getIsBannedFilter(String filterParam) {
    if (filterParam == null) {
      return null;
    } else {
      return Boolean.valueOf(filterParam);
    }
  }

  private Long getLong(String value) {
    if (value == null) {
      return null;
    } else {
      return Long.parseLong(value);
    }
  }

  private List<PlayerDto> filterPlayers(PlayerFilter filter, List<PlayerDto> players) {
    return players.stream()
        .filter(byName(filter))
        .filter(byTitle(filter))
        .filter(byBanned(filter))
        .filter(byBirthday(filter))
        .filter(byRace(filter))
        .collect(Collectors.toList());
  }

  private Predicate<PlayerDto> byName(PlayerFilter filter) {
    return playerDto -> {
      if (filter.getName() == null) {
        return true;
      } else {
        return playerDto.getName().toLowerCase().contains(filter.getName().toLowerCase());
      }
    };
  }

  private Predicate<PlayerDto> byTitle(PlayerFilter filter) {
    return playerDto -> {
      if (filter.getTitle() == null) {
        return true;
      } else {
        return playerDto.getTitle().toLowerCase().contains(filter.getTitle().toLowerCase());
      }
    };
  }

  private Predicate<PlayerDto> byRace(PlayerFilter filter) {
    return playerDto -> {
      if (filter.getRace() == null) {
        return true;
      } else {
        return playerDto.getRace().equals(filter.getRace());
      }
    };
  }

  private Predicate<PlayerDto> byBanned(PlayerFilter filter) {
    return playerDto -> {
      if (filter.isBanned() != null) {
        return playerDto.isBanned() == filter.isBanned();
      } else {
        return true;
      }
    };
  }

  private Predicate<PlayerDto> byBirthday(PlayerFilter filter) {
    return playerDto -> {
      return filterByBirthdayAfter(playerDto, filter.getBirthdayAfter()) &&
          filterByBirthdayBefore(playerDto, filter.getBirthdayBefore());
    };
  }

  private boolean filterByBirthdayAfter(PlayerDto playerDto, Long birthdayAfter) {
    if (birthdayAfter == null) {
      return true;
    } else {
      return playerDto.getBirthday().getTime() >= birthdayAfter;
    }
  }

  private boolean filterByBirthdayBefore(PlayerDto playerDto, Long birthdayBefore) {
    if (birthdayBefore == null) {
      return true;
    } else {
      return playerDto.getBirthday().getTime() <= birthdayBefore;
    }
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
