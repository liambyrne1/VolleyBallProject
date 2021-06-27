package com.volleyballlondon.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles")
public class Role {
    public static final String ADMIN = "ADMIN";

    public static final String USER = "USER";

    public static final String AUTHORITY_PREFIX = "ROLE_";

    public static final SimpleGrantedAuthority ADMIN_AUTHORITY =
        new SimpleGrantedAuthority("ROLE_ADMIN");

    public static final SimpleGrantedAuthority USER_AUTHORITY =
        new SimpleGrantedAuthority("ROLE_USER");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;
	
    @Column(unique = true)
	private String name;

    @ManyToMany(mappedBy = "roles")
	private Set<UserEntity> users = new HashSet<>(); 

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}	
}
