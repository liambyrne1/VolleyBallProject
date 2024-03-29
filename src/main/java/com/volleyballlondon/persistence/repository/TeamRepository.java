package com.volleyballlondon.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volleyballlondon.persistence.model.Team;

public interface TeamRepository<M> extends CrudRepository<Team, Long> {
    List<M> findByName(String name);

    List<M> findByClubIdOrderByName(Long clubId);

    List<M> findByLeagueIdOrderByName(Long leagueId);

    List<M> findByNameIgnoreCase(String name);

    List<M> findByOrderByName();
    
    M findFirstByOrderByIdDesc();

    @Modifying
    @Query("update Team t set t.name = :teamName where t.id = :teamId")
    int updateTeamName(@Param("teamId") Long teamId,
        @Param("teamName") String teamName);

    /**
     * Updates the leagueId field in the team table.
     *
     * @param teamId Identifies the team table.
     * @param teamLeagueId New value for the leagueId field.
     */
    @Modifying
    @Query("update Team t set t.leagueId = :teamLeagueId where t.id = :teamId")
    int updateTeamLeagueId(@Param("teamId") Long teamId,
        @Param("teamLeagueId") Long teamLeagueId);

}
