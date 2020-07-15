package com.volleyballlondon.persistence.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.volleyballlondon.persistence.config.DataConfig;
import com.volleyballlondon.persistence.model.Club;
import com.volleyballlondon.persistence.repository.ClubRepository;

@Configuration("clubDbBean")
@Import(DataConfig.class)
public class ClubDbService {

	@Autowired
	ClubRepository<Club> clubRepository;

    /**
     * Finds the Club count
     */
    public long count() {
        return clubRepository.count();
    }

    /**
     * Returns all the columns of each club on the database
     * ordered by the database default.
     */
	public List<Club> getAllClubs() {
		return (List<Club>) clubRepository.findAll();
	}

    /**
     * Returns all the columns of each club on the database
     * ordered alphabetically by name.
     */
	public List<Club> findByOrderByName() {
		return (List<Club>) clubRepository.findByOrderByName();
	}

    /**
     * Finds the Club that was inserted last
     */
    public Club findFirstByIdOrderByIdDesc() {
        return clubRepository.findFirstByOrderByIdDesc();
    }

    /**
     * Finds a Club by the given name
     */
	public List<Club> findByName(String name) {
		return clubRepository.findByName(name);
	}

    /**
     * Finds a Club by the given name but ignoring the case
     */
	public List<Club> findByNameIgnoreCase(String name) {
		return clubRepository.findByNameIgnoreCase(name);
	}

    /**
     * Finds a Club by the given id
     */
	public Optional<Club> getById(Long id) {
		return clubRepository.findById(id);
	}

    /**
     * Updates the club name for a given id.
     */
     @Transactional
     public int updateClubName(Long clubId, String clubName) {
        return clubRepository.updateClubName(clubId, clubName);
     }

    /**
     * Deletes the Club with the given id
     */
	@Transactional
	public void deleteClub(Long clubId) {
		clubRepository.deleteById(clubId);
	}

    /**
     * Adds a new Club with the given name
     */
	@Transactional
	public boolean addClub(String clubName) {
        Club club = new Club();
        club.setName(clubName);
		return clubRepository.save(club) != null;
	}
}
