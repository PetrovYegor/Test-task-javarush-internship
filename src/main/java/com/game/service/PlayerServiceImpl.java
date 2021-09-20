package com.game.service;


import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<Player> getAllPlayers(
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
            PlayerOrder order) {

        Date afterDate = null;
        Date beforeDate = null;
        if (after != null){afterDate = new Date(after);}
        if (before != null){beforeDate = new Date(before);}

        List<Player> allPlayers = playerRepository.findAll();

        List<Player> allPlayersAfterFilter = new ArrayList<>();

        for (Player player : allPlayers){
            if (name != null && !player.getName().contains(name)){continue;}
            if (title != null && !player.getTitle().contains(title)){continue;}
            if (race != null && player.getRace() != race){continue;}
            if (profession != null && player.getProfession() != profession){continue;}
            if (after != null && !player.getBirthday().after(afterDate)){continue;}
            if (before != null && !player.getBirthday().before(beforeDate)){continue;}
            if (banned != null && player.getBanned() != banned){continue;}
            if (minExperience != null && player.getExperience().compareTo(minExperience) < 0){continue;}
            if (maxExperience != null && player.getExperience().compareTo(maxExperience) > 0){continue;}
            if (minLevel != null && player.getLevel().compareTo(minLevel) < 0){continue;}
            if (maxLevel != null && player.getLevel().compareTo(maxLevel) > 0){continue;}
            allPlayersAfterFilter.add(player);
        }
        if (order != null) {
            allPlayersAfterFilter.sort((player1, player2) -> {
                switch(order) {
                    case ID: return player1.getId().compareTo(player2.getId());
                    case NAME: return player1.getName().compareTo(player2.getName());
                    case LEVEL: return player1.getLevel().compareTo(player2.getLevel());
                    case BIRTHDAY: return player1.getBirthday().compareTo(player2.getBirthday());
                    case EXPERIENCE: return player1.getExperience().compareTo(player2.getExperience());
                    default: return 0;
                }
            });
        }
        return allPlayersAfterFilter;
    }

    @Override
    public int countAllFilteredPlayers(String name,
                                       String title,
                                       Race race,
                                       Profession profession,
                                       Long birthdayMin,
                                       Long birthdayMax,
                                       Boolean banned,
                                       Integer minExperience,
                                       Integer maxExperience,
                                       Integer minLevel,
                                       Integer maxLevel) {
        Date afterDate = null;
        Date beforeDate = null;
        if (birthdayMin != null){afterDate = new Date(birthdayMin);}
        if (birthdayMax != null){beforeDate = new Date(birthdayMax);}

        List<Player> allPlayers = playerRepository.findAll();
        List<Player> allFilteredPlayers = new ArrayList<>();

        for (Player player : allPlayers){
            if (name != null && !player.getName().contains(name)){continue;}
            if (title != null && !player.getTitle().contains(title)){continue;}
            if (race != null && player.getRace() != race){continue;}
            if (profession != null && player.getProfession() != profession){continue;}
            if (birthdayMin != null && !player.getBirthday().after(afterDate)){continue;}
            if (birthdayMax != null && !player.getBirthday().before(beforeDate)){continue;}
            if (banned != null && player.getBanned() != banned){continue;}
            if (minExperience != null && player.getExperience().compareTo(minExperience) < 0){continue;}
            if (maxExperience != null && player.getExperience().compareTo(maxExperience) > 0){continue;}
            if (minLevel != null && player.getLevel().compareTo(minLevel) < 0){continue;}
            if (maxLevel != null && player.getLevel().compareTo(maxLevel) > 0){continue;}
            allFilteredPlayers.add(player);
        }

        return allFilteredPlayers.size();
    }

    public List<Player> getPage(Integer pageNumber, Integer pageSize, List<Player> players) {
        Integer page = pageNumber == null ? 0 : pageNumber;
        Integer size = pageSize == null ? 3 : pageSize;
        int from = page * size;
        int to = from + size;
        if (to > players.size()) to = players.size();
        return players.subList(from, to);
    }

    @Override
    public Player getPlayerById(long id) {
        Player player = null;
        Optional<Player> optional = playerRepository.findById(id);
        if (optional.isPresent()){
            player = optional.get();
        }
        return player;

    }

    @Override
    public Player savePlayer(Player player) {
        Integer exp = player.getExperience();
        Integer level = calculateLevel(exp);
        player.setLevel(level);
        player.setUntilNextLevel(calculateUntilNextLevel(exp, level));
        playerRepository.save(player);
        return player;
    }

    @Override
    public Player updatePlayer(Player oldPlayer, Player newPlayer){
        String name = newPlayer.getName();
        if (name != null) {
            if (!isNameValid(name))
                throw new IllegalArgumentException();
            else
                oldPlayer.setName(name);
        }
        String title = newPlayer.getTitle();
        if (title != null) {
            if (!isTitleValid(title))
                throw new IllegalArgumentException();
            else
                oldPlayer.setTitle(title);
        }
        Race race = newPlayer.getRace();
        if (race != null)
            oldPlayer.setRace(race);
        Profession profession = newPlayer.getProfession();
        if (profession != null)
            oldPlayer.setProfession(profession);
        Integer experience = newPlayer.getExperience();
        if (experience != null) {
            if (!isExperienceValid(experience))
                throw new IllegalArgumentException();
            else {
                oldPlayer.setExperience(experience);
                Integer level = calculateLevel(experience);
                oldPlayer.setLevel(calculateLevel(experience));
                oldPlayer.setUntilNextLevel(calculateUntilNextLevel(experience, level));
            }
        }
        Date birthday = newPlayer.getBirthday();
        if (birthday != null) {
            if (!isBirthdayValid(birthday))
                throw new IllegalArgumentException();
            else
                oldPlayer.setBirthday(birthday);
        }
        Boolean banned = newPlayer.getBanned();
        if (banned != null)
            oldPlayer.setBanned(banned);
        return playerRepository.save(oldPlayer);

    }

    @Override
    public void deletePlayerById(Long id) {
            playerRepository.deleteById(id);
    }

    private boolean isNameValid(String name){
        return  name != null && name.trim().length() != 0 && !name.isEmpty() && name.trim().length() <=12;
    }

    private boolean isTitleValid(String title){
        return  title!= null && title.trim().length() != 0 && !title.isEmpty() && title.trim().length() <=30;
    }

    private boolean isExperienceValid(Integer experience){
        return experience != null && experience >= 0 && experience <= 10000000;
    }

    private boolean isBirthdayValid(Date birthday){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.YEAR, 3000);
        Date endDate = calendar.getTime();

        return birthday != null && birthday.after(startDate) && birthday.before(endDate);

    }

    public Integer calculateLevel(Integer experience){
        return (int) Math.floor((Math.sqrt(2500+200*experience) - 50) / 100);
    }

    public Integer calculateUntilNextLevel(Integer experience, Integer level){
        return 50 * (level + 1) * (level + 2) - experience;
    }

    public boolean isPlayerValid(Player player){
        return player != null && isNameValid(player.getName()) && isTitleValid(player.getTitle()) && isExperienceValid(player.getExperience()) && isBirthdayValid(player.getBirthday());
    }

    public boolean isIdValid(String id){
        try{
            long checkId = Long.parseLong(id);
            if (checkId >= 1 && (checkId % 2 == 0 || checkId % 2 == 1)){
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

}
