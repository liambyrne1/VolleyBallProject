package com.volleyballlondon.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "leagues")
public class League {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	public League() {
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

	@Override
	public String toString() {
		return "League{" + "id=" + id + ", leagueName=" + name + "}";
	}
}
