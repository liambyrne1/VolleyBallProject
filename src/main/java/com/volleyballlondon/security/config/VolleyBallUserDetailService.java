package com.volleyballlondon.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.volleyballlondon.persistence.model.Role;
import com.volleyballlondon.persistence.model.UserEntity;
import com.volleyballlondon.persistence.services.UserDbService;

@Service
public class VolleyBallUserDetailService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
 
    @Autowired
    UserDbService userDbService;

    /**
     * Identifies an user.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userDbService.getById(username);
        UserEntity userEntity;

        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
        } else {
            throw new UsernameNotFoundException(username);
        }

        UserDetails user = new User(
            userEntity.getUserName(),
            userEntity.getPassword(),
            userEntity.getAuthorities());

        return user;
    }

}
