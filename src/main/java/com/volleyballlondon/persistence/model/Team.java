package com.volleyballlondon.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "teams")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "club_id")
	private Long clubId;

	public Team() {
	}

	public Team(String teamName, Long teamClubId) {
        name = teamName;
        clubId = teamClubId;
	}

    @JsonProperty("id")
	public Long getId() {
		return id;
	}

    @JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

    @JsonProperty("name")
	public String getName() {
		return name;
	}

    @JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

    @JsonProperty("club_id")
	public Long getClubId() {
		return clubId;
	}

    @JsonProperty("club_id")
	public void setClubId(Long clubId) {
		this.clubId = clubId;
    }

	@Override
	public String toString() {
		return "Team{" + "id=" + id + ", teamName=" + name +
            ", clubId = " + clubId + "}";
	}
}
