package com.volleyballlondon.persistence.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.volleyballlondon.persistence.config.DataConfig;
import com.volleyballlondon.persistence.model.Team;
import com.volleyballlondon.persistence.repository.TeamRepository;

@Configuration("teamDbBean")
@Import(DataConfig.class)
public class TeamDbService {

	@Autowired
	TeamRepository<Team> teamRepository;

    /**
     * Finds the Team count
     */
    public long count() {
        return teamRepository.count();
    }

    /**
     * Returns all the columns of each team on the database
     * ordered by the database default.
     */
	public List<Team> getAllTeams() {
		return (List<Team>) teamRepository.findAll();
	}

    /**
     * Finds a Team by the given id
     */
	public Optional<Team> getById(Long id) {
		return teamRepository.findById(id);
	}

    /**
     * Finds the Team that was inserted last
     */
    public Team findFirstByIdOrderByIdDesc() {
        return teamRepository.findFirstByOrderByIdDesc();
    }

    /**
     * Finds a Team by the given name
     */
	public List<Team> findByName(String name) {
		return teamRepository.findByName(name);
	}

    /**
     * Finds a Team by the given name but ignoring the case
     */
	public List<Team> findByNameIgnoreCase(String name) {
		return teamRepository.findByNameIgnoreCase(name);
	}

    /**
     * Returns all the columns of each team on the database
     * ordered alphabetically by name.
     */
	public List<Team> findByOrderByName() {
		return (List<Team>) teamRepository.findByOrderByName();
	}

    /**
     * Finds Teams by the given club id.
     */
	public List<Team> findByClubIdOrderByName(Long clubId) {
		return teamRepository.findByClubIdOrderByName(clubId);
	}

    /**
     * Finds Teams by the given league id.
     */
	public List<Team> findByLeagueIdOrderByName(Long leagueId) {
		return teamRepository.findByLeagueIdOrderByName(leagueId);
	}

    /**
     * Updates the team name for a given id.
     */
     @Transactional
     public int updateTeamName(Long teamId, String teamName) {
        return teamRepository.updateTeamName(teamId, teamName);
     }

    /**
     * Deletes the Team with the given id
     */
	@Transactional
	public void deleteTeam(Long teamId) {
		teamRepository.deleteById(teamId);
	}

    /**
     * Removes a team from a league by setting the leagueId field in the team
     * table on the database to null.
     * 
     * @param teamId Id of the team that is being removed.
     *
     */
     @Transactional
     public int removeTeamFromLeague(Long teamId) {
        return teamRepository.updateTeamLeagueId(teamId, null);
     }

    /**
     * Adds a team to a league by setting the leagueId field in the team
     * table to the Id of the league.
     * 
     * @param teamId Id of the team that is being updated.
     * @param leagueId Id of league, team is being associated to.
     *
     */
     @Transactional
     public int addTeamToLeague(Long teamId, Long leagueId) {
        return teamRepository.updateTeamLeagueId(teamId, leagueId);
     }

    /**
     * Adds a new Team with the given name
     */
	@Transactional
	public boolean addTeam(String teamName, Long teamClubId) {
        Team team = new Team(teamName, teamClubId);
		return teamRepository.save(team) != null;
	}
}
