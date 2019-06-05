package com.volleyballlondon.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.volleyballlondon.persistence.model.League;

public interface LeagueRepository<P> extends CrudRepository<League, Long> {
    List<League> findByName(String name);

    List<League> findByNameIgnoreCase(String name);

    League findFirstByOrderByIdDesc();
}
