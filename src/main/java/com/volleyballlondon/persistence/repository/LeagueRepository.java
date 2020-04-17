package com.volleyballlondon.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volleyballlondon.persistence.model.League;

public interface LeagueRepository<P> extends CrudRepository<League, Long> {
    List<League> findByName(String name);

    List<League> findByNameIgnoreCase(String name);

    List<League> findByOrderByName();
    
    League findFirstByOrderByIdDesc();

    @Modifying
    @Query("update League l set l.name = :leagueName where l.id = :leagueId")
    int updateLeagueName(@Param("leagueId") Long leagueId,
        @Param("leagueName") String leagueName);
}
