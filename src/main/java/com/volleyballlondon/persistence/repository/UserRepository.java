package com.volleyballlondon.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.volleyballlondon.persistence.model.UserEntity;

public interface UserRepository<P> extends CrudRepository<UserEntity, String> {
    UserEntity findByUserName(String username);
}
