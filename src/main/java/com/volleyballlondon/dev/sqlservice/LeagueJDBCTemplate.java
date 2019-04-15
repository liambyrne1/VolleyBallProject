package com.volleyballlondon.dev.sqlservice;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.volleyballlondon.dataobject.League;

/**
 * JdbcTemplate for the League table
 */
public class LeagueJDBCTemplate implements LeagueDAO {

    /** Data source is injected */
    private DataSource i_dataSource;
	private JdbcTemplate i_jdbcTemplateObject;

	@Override
	public void setdataSource(DataSource p_dataSource) {
		i_dataSource = p_dataSource;
        i_jdbcTemplateObject = new JdbcTemplate(p_dataSource);
	}

    /**
     * Creates a new League with the given league name
     */
    @Override
	public void create(String p_league) {
			String l_sqlString = "INSERT INTO leagues (LeagueName) VALUES (?)";

			i_jdbcTemplateObject.update(l_sqlString, new Object[]{p_league});
	}

    /**
     * Reads a League with the given league name.
     * The league name may not exist.
     */
    @Override
    public List<League> read(String p_league) {
        String l_sqlString = "SELECT LeagueName "
                           + "FROM leagues "
                           + "WHERE LeagueName = ?";

        List<League> l_leagues = i_jdbcTemplateObject.query(l_sqlString,
            new Object[]{p_league}, new LeagueNameMapper());

        return l_leagues;
    }

    /**
     * Reads the League count
     */
    @Override
    public int readLeagueCount() {
        String l_sqlString = "SELECT COUNT(*) "
                           + "FROM leagues";

        int l_leagueCount = i_jdbcTemplateObject.queryForObject(l_sqlString, Integer.class);
        return l_leagueCount;
    }

    /**
     * Reads the newest League, the last league created
     */
    @Override
    public League readNewLeague() {
        String l_sqlString = "SELECT LeagueName "
                           + "FROM leagues "
                           + "ORDER BY LeagueID DESC "
                           + "LIMIT 1";

        return i_jdbcTemplateObject.queryForObject(l_sqlString, new LeagueNameMapper());
    }
}
