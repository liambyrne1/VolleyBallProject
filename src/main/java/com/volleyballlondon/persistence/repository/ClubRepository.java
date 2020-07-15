package com.volleyballlondon.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volleyballlondon.persistence.model.Club;

public interface ClubRepository<P> extends CrudRepository<Club, Long> {
    List<Club> findByName(String name);

    List<Club> findByNameIgnoreCase(String name);

    List<Club> findByOrderByName();
    
    Club findFirstByOrderByIdDesc();

    @Modifying
    @Query("update Club c set c.name = :clubName where c.id = :clubId")
    int updateClubName(@Param("clubId") Long clubId,
        @Param("clubName") String clubName);
}
