package com.volleyballlondon.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.volleyballlondon.persistence.model.Role;

public interface RoleRepository<P> extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
