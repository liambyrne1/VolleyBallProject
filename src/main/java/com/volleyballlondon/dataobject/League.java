package com.volleyballlondon.dataobject;

/**
 * Represents a League
 */
public class League {
    Integer i_id;
    String i_leagueName;

    public Integer getId() {
        return i_id;
    }
    public void setId(Integer p_id) {
        i_id = p_id;
    }
    public String getLeagueName() {
        return i_leagueName;
    }
    public void setLeagueName(String p_leagueName) {
        i_leagueName = p_leagueName;
    }
}
