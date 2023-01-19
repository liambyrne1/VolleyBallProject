package com.volleyballlondon.security.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.volleyballlondon.exceptions.UserAlreadyExistsException;
import com.volleyballlondon.persistence.model.Role;
import com.volleyballlondon.persistence.services.UserDbService;
import com.volleyballlondon.security.service.SecurityService;
import com.volleyballlondon.web.dto.UserDto;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    UserDbService userDbService;

    @Autowired
    private SecurityService securityService;

    /**
     * When the controller receives the request “/registration”,
     * creates the new UserDto object that will back the registration form.
     * 
     * @should return registration form
     */
    @GetMapping
    public ModelAndView showRegistrationForm() {
        LOGGER.debug("Rendering registration page.");
        return new ModelAndView("registrationForm", "userDTO", new UserDto());
    }

    /**
     * Creates a new user registration on the database.
     * 
     * @param userDTO
     *            - DTO that holds user input. User name and password.
     * @return returns the following screens:
     *              - userloginpage if successful registration,
     *              - registrationForm if user already exists.
     *              - errorpage if system error.
     *
     * @should create new user registration
     * @should fail if user already exists
     */
    @PostMapping
    public ModelAndView registerUser(@Valid @ModelAttribute("userDTO")
        UserDto userDto, BindingResult br) {
        LOGGER.debug("Registering user account with information: {}", userDto);

        ModelAndView modelAndView = null;

        try {
            if (br.hasErrors()) {
                modelAndView = new ModelAndView("registrationForm");
            }
            else {
                modelAndView = processUser(userDto);
            }
        } catch (final RuntimeException ex) {
            LOGGER.warn("Unable to register user", ex);
            modelAndView = new ModelAndView("errorpage");
            throw ex;
        }
        return modelAndView;
    }

    /**
     * Creates the user on the database, if the user does not already exist.
     * Assigns user ROLE_USER authority.
     * Authenticates the user to use the system.
     */
    private ModelAndView processUser(UserDto userDto) {
        // Always register a new user with "USER" role.
        ModelAndView modelAndView;

        try {
            userDbService.registerNewUserAccount(userDto, Role.USER);

            securityService.autoLogin(userDto.getEmail(), userDto.getPassword());

            System.out.println("Successfully authenticated. Security context contains: " 
                + SecurityContextHolder.getContext().getAuthentication());

            modelAndView = new ModelAndView("userloginpage");

        } catch (final UserAlreadyExistsException uaeEx) {
            modelAndView = new ModelAndView("registrationForm");
            modelAndView.addObject("errorMessage", uaeEx.getMessage());
        }
        return modelAndView;
    }

}
