package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {


    Page<Player> findAllByNameContains(String name, Pageable pageable);

    Page<Player> findAllByTitleContains(String title, Pageable pageAble);

    Page<Player> findAllByRaceAndProfessionAndBirthdayAfterAndBirthdayBefore(Race race, Profession profession, Date min, Date max, Pageable pageable);

}
