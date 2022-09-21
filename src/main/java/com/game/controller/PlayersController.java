package com.game.controller;

import com.game.dto.PlayerDto;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/rest/players")
public class PlayersController {

  private final PlayerService playerService;
  private final Paginator paginator;

  @Autowired
  public PlayersController(
      PlayerService playerService,
      Paginator paginator) {
    this.playerService = playerService;
    this.paginator = paginator;
  }

  /**
   * REST API - GET /rest/players
   *
   * @return list of players
   */
  @GetMapping
  public ResponseEntity<List<PlayerDto>> getPlayers(
      @RequestParam Map<String, String> queryParams,
      @RequestParam(name = "order", required = false) String order,
      @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
      @RequestParam(name = "pageSize", required = false) Integer pageSize
  ) {
    // Создать фильтр
    PlayerFilter filter = createFilter(queryParams);
    // Получить список игроков из нашего сервиса
    List<PlayerDto> players = playerService.getAllPlayers();
    // Отфильтровать список игроков
    List<PlayerDto> filteredPlayers = filterPlayers(filter, players);
    sortPlayers(filteredPlayers, order);
    List<PlayerDto> filteredSortedPaginatedPlayers = paginator.getPageData(filteredPlayers, pageNumber, pageSize);
    return ResponseEntity.ok(filteredSortedPaginatedPlayers);
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
          case EXPERIENCE: {
            playerDtos.sort(sortByExperience());
            break;
          }
          case LEVEL: {
            playerDtos.sort(sortByLevel());
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
      Long player1Birthday = player1.getBirthday();
      Long player2Birthday = player2.getBirthday();
      return Long.compare(player1Birthday, player2Birthday);
    };
  }

  private Comparator<PlayerDto> sortByExperience() {
    return (player1, player2) -> {
      return Integer.compare(player1.getExperience(), player2.getExperience());
    };
  }

  private Comparator<PlayerDto> sortByLevel() {
    return (player1, player2) -> {
      return Integer.compare(player1.getLevel(), player2.getLevel());
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
    filter.setProfession(getProfessionFilter(queryParams.get(PlayerFilter.PROFESSION)));
    filter.setExperienceAfter(getInteger(queryParams.get(PlayerFilter.EXPERIENCE_AFTER)));
    filter.setExperienceBefore(getInteger(queryParams.get(PlayerFilter.EXPERIENCE_BEFORE)));
    filter.setLevelAfter(getInteger(queryParams.get(PlayerFilter.LEVEL_AFTER)));
    filter.setLevelBefore(getInteger(queryParams.get(PlayerFilter.LEVEL_BEFORE)));
    return filter;
  }

  private Profession getProfessionFilter(String professionFilter) {
    if (professionFilter == null) {
      return null;
    } else {
      try {
        return Profession.valueOf(professionFilter);
      } catch (Exception e) {
        return null;
      }
    }
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

  private Integer getInteger(String value) {
    if (value == null) {
      return null;
    } else {
      return Integer.parseInt(value);
    }
  }

  private List<PlayerDto> filterPlayers(PlayerFilter filter, List<PlayerDto> players) {
    return players.stream()
        .filter(byName(filter))
        .filter(byTitle(filter))
        .filter(byBanned(filter))
        .filter(byBirthday(filter))
        .filter(byRace(filter))
        .filter(byProfession(filter))
        .filter(byExperience(filter))
        .filter(byLevel(filter))
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

  private Predicate<PlayerDto> byProfession(PlayerFilter filter) {
    return playerDto -> {
      if (filter.getProfession() == null) {
        return true;
      } else {
        return playerDto.getProfession().equals(filter.getProfession());
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

  private Predicate<PlayerDto> byExperience(PlayerFilter filter) {
    return playerDto -> {
      return filterByExperienceAfter(playerDto, filter.getExperienceAfter()) &&
          filterByExperienceBefore(playerDto, filter.getExperienceBefore());
    };
  }

  private Predicate<PlayerDto> byLevel(PlayerFilter filter) {
    return playerDto -> {
      return filterByLevelAfter(playerDto, filter.getLevelAfter()) &&
          filterByLevelBefore(playerDto, filter.getLevelBefore());
    };
  }

  private boolean filterByLevelAfter(PlayerDto playerDto, Integer levelAfter) {
    if (levelAfter == null) {
      return true;
    } else {
      return playerDto.getLevel() >= levelAfter;
    }
  }

  private boolean filterByLevelBefore(PlayerDto playerDto, Integer levelBefore) {
    if (levelBefore == null) {
      return true;
    } else {
      return playerDto.getLevel() <= levelBefore;
    }
  }

  private boolean filterByExperienceAfter(PlayerDto playerDto, Integer experienceAfter) {
    if (experienceAfter == null) {
      return true;
    } else {
      return playerDto.getExperience() >= experienceAfter;
    }
  }

  private boolean filterByExperienceBefore(PlayerDto playerDto, Integer experienceBefore) {
    if (experienceBefore == null) {
      return true;
    } else {
      return playerDto.getExperience() <= experienceBefore;
    }
  }

  private boolean filterByBirthdayAfter(PlayerDto playerDto, Long birthdayAfter) {
    if (birthdayAfter == null) {
      return true;
    } else {
      return playerDto.getBirthday() >= birthdayAfter;
    }
  }

  private boolean filterByBirthdayBefore(PlayerDto playerDto, Long birthdayBefore) {
    if (birthdayBefore == null) {
      return true;
    } else {
      return playerDto.getBirthday() <= birthdayBefore;
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
    validateDto(player);
    PlayerDto result = playerService.createPlayer(player);
    return ResponseEntity.ok(result);
  }

  private void validateDto(PlayerDto player) {
    if (player.getBirthday() <= 0) {
      throw new IllegalArgumentException("Birthday can't be less zero");
    }
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
  public ResponseEntity<Integer> getPlayersCount(
      @RequestParam Map<String, String> queryParams
  ) {

    // Создать фильтр
    PlayerFilter filter = createFilter(queryParams);
    // Получить список игроков из нашего сервиса
    List<PlayerDto> players = playerService.getAllPlayers();
    // Отфильтровать список игроков
    List<PlayerDto> filteredPlayers = filterPlayers(filter, players);

    return ResponseEntity.ok(filteredPlayers.size());

  }
}
