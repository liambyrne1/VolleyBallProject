package com.volleyballlondon.dev.sqlservice;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.volleyballlondon.dataobject.League;

/**
 * Mapper for the League
 */
public class LeagueNameMapper implements RowMapper<League> {

    @Override
    public League mapRow(ResultSet p_rs, int p_rowNum) throws SQLException {
        League l_league = new League();
        l_league.setLeagueName(p_rs.getString("LeagueName"));

        return l_league;
    }

}
