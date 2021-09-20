package com.game.controller;

import com.game.entity.Player;
//import com.game.service.PlayerService;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception_handling.NoSuchPlayerException;
import com.game.exception_handling.PlayerIncorrectData;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class MyRESTController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getAllPlayers(@RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "race", required = false) Race race,
                                          @RequestParam(value = "profession", required = false) Profession profession,
                                          @RequestParam(value = "after", required = false) Long after,
                                          @RequestParam(value = "before", required = false) Long before,
                                          @RequestParam(value = "banned", required = false) Boolean banned,
                                          @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                          @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                          @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                          @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                          @RequestParam(required = false, defaultValue = "ID") PlayerOrder order,
                                          @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                          @RequestParam(required = false, defaultValue = "3") Integer pageSize){
/*        Sort sort = Sort.by(Sort.Direction.ASC, order.getFieldName());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);*/
        List<Player> allPlayers = playerService.getAllPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order);
        return playerService.getPage(pageNumber, pageSize, allPlayers);
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable Long id) throws Exception {
        String checkId = Long.toString(id);
        if (!playerService.isIdValid(checkId)){
            throw new Exception("BAD ID");
        }
        Player player = playerService.getPlayerById(id);

        if (player==null){
            throw new NoSuchPlayerException("There is no player with Id = " + id + " in Database");
        }
        return player;
    }

    @PostMapping("/players")
    public Player addNewPlayer(@RequestBody Player player) throws Exception {
        if (!playerService.isPlayerValid(player)){
            throw new Exception("bad attributes for new player");
        }
        playerService.savePlayer(player);
        return player;
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@RequestBody Player player,
                               @PathVariable Long id) throws Exception {
        String checkId = Long.toString(id);
        if (!playerService.isIdValid(checkId)){
            throw new Exception("BAD ID");
        }
        Player oldPlayer = playerService.getPlayerById(id);
        if (oldPlayer == null){
            throw new NoSuchPlayerException("This player does not exist");
        }

        return playerService.updatePlayer(oldPlayer,player);
    }

    @DeleteMapping("/players/{id}")
    public String deleteEmployee(@PathVariable Long id) throws Exception {
        String checkId = Long.toString(id);
        if (!playerService.isIdValid(checkId)){
            throw new Exception("BAD ID");
        }
        Player player = playerService.getPlayerById(id);
        if (player == null){
            throw new NoSuchPlayerException("There is no player with Id = " + id + " in Database");
        }

        playerService.deletePlayerById(id);
        return "Player with ID = " + id + " was deleted";
    }

    @GetMapping("/players/count")
    public int getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "race", required = false) Race race,
                                   @RequestParam(value = "profession", required = false) Profession profession,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel){
        return playerService.countAllFilteredPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }
}
