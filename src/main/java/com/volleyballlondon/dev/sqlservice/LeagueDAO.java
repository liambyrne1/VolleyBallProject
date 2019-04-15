package com.volleyballlondon.dev.sqlservice;

import java.util.List;

import javax.sql.DataSource;

import com.volleyballlondon.dataobject.League;

/**
 * DAO interface for League JDBC Template
 */
public interface LeagueDAO {

	void setdataSource(DataSource p_dataSource);

	void create(String p_league);

    List<League> read(String p_league);

    int readLeagueCount();

    League readNewLeague();
}
