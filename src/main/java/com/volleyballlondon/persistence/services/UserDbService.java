package com.volleyballlondon.persistence.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.volleyballlondon.exceptions.UserAlreadyExistsException;
import com.volleyballlondon.persistence.model.Role;
import com.volleyballlondon.persistence.model.UserEntity;
import com.volleyballlondon.persistence.repository.RoleRepository;
import com.volleyballlondon.persistence.repository.UserRepository;
import com.volleyballlondon.web.dto.UserDto;

public class UserDbService {

	@Autowired
	UserRepository<UserEntity> userRepository;

	@Autowired
	RoleRepository<Role> roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
 
    /**
     * Finds the User count
     */
    public long count() {
        return userRepository.count();
    }

    /**
     * Finds a User by the given user name
     */
	public UserEntity findByUsername(String name) {
		return userRepository.findByUserName(name);
	}

    /**
     * Finds a User by the given id
     */
	public Optional<UserEntity> getById(String username) {
		return userRepository.findById(username);
	}

    /**
     * Creates a new user on the database.
     * Encodes the user's password.
     * Assigns the given role to the user by using the join table "users_roles".
     *
     * @param userDto DTO from controller containing user details.
     * @param roleString String version of user role.
     */
	@Transactional
	public boolean registerNewUserAccount(UserDto userDto, String roleString)
        throws UserAlreadyExistsException {
        
        if (userNameExists(userDto.getUsername())) {
            throw new UserAlreadyExistsException(userDto.getUsername());
        }

        UserEntity userEntity = new UserEntity();    
        userEntity.setUserName(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName(roleString);
        userEntity.addRole(role);

        return userRepository.save(userEntity) != null;
    }

    /**
     * Returns true if user exists on the database.
     */
    private boolean userNameExists(String username) {
        return getById(username).isPresent();
    }
}
