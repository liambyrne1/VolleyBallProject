package com.volleyballlondon.dev.dbservice;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.volleyballlondon.dataobject.League;
import com.volleyballlondon.dev.sqlservice.LeagueJDBCTemplate;

/**
 * Encapsulates the League Template
 */
public class LeagueDbService {
    LeagueJDBCTemplate i_leagueJDBCTemplate;

    public LeagueDbService() {
        ApplicationContext l_context = new ClassPathXmlApplicationContext("Beans.xml");
        i_leagueJDBCTemplate = (LeagueJDBCTemplate) l_context.getBean("leagueJDBCTemplate");
    }

	public void create(String p_league) {
		i_leagueJDBCTemplate.create(p_league);
	}

    public List<League> read(String p_league) {
        return i_leagueJDBCTemplate.read(p_league);
    }

    public int readLeagueCount() {
        return i_leagueJDBCTemplate.readLeagueCount();
    }

    public League readNewLeague() {
        return i_leagueJDBCTemplate.readNewLeague();
    }
}
