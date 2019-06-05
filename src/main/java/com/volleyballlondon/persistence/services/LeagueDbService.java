package com.volleyballlondon.persistence.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.volleyballlondon.persistence.config.DataConfig;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.repository.LeagueRepository;

@Configuration("mainBean")
@Import(DataConfig.class)
public class LeagueDbService {

	@Autowired
	LeagueRepository<League> leagueRepository;

    /**
     * Finds the League count
     */
    public long count() {
        return leagueRepository.count();
    }

    /**
     * Finds the League that was inserted last
     */
    public League findFirstByIdOrderByIdDesc() {
        return leagueRepository.findFirstByOrderByIdDesc();
    }

    /**
     * Finds all leagues
     */
	public List<League> getAllLeagues() {
		return (List<League>) leagueRepository.findAll();
	}

    /**
     * Finds a League by the given name
     */
	public List<League> findByName(String name) {
		return leagueRepository.findByName(name);
	}

    /**
     * Finds a League by the given name but ignoring the case
     */
	public List<League> findByNameIgnoreCase(String name) {
		return leagueRepository.findByNameIgnoreCase(name);
	}

    /**
     * Finds a League by the given id
     */
	public Optional<League> getById(Long id) {
		return leagueRepository.findById(id);
	}

    /**
     * Deletes the League with the given id
     */
	@Transactional
	public void deleteLeague(Long leagueId) {
		leagueRepository.deleteById(leagueId);
	}

    /**
     * Adds a new League with the given name
     */
	@Transactional
	public boolean addLeague(String leagueName) {
        League league = new League();
        league.setName(leagueName);
		return leagueRepository.save(league) != null;
	}
}
