package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface PlayerService {

    List<Player> getAllPlayers(
            String name,
            String title,
            Race race,
            Profession profession,
            Long after,
            Long before,
            Boolean banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel,
            PlayerOrder order
    );

    Player getPlayerById(long id);

    List<Player> getPage(Integer pageNumber, Integer pageSize, List<Player> players);

    int countAllFilteredPlayers(String name,
                                String title,
                                Race race,
                                Profession profesion,
                                Long birthdayMin,
                                Long birthdayMax,
                                Boolean banned,
                                Integer minExperience,
                                Integer maxExperience,
                                Integer minLevel,
                                Integer maxLevel);

    Player savePlayer(Player player);

    void deletePlayerById(Long id);

    boolean isPlayerValid(Player player);

    boolean isIdValid(String id);

    Player updatePlayer(Player oldPlayer, Player newPlayer);

}
