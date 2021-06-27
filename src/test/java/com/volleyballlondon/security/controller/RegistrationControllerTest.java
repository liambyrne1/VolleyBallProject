package com.volleyballlondon.security.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.volleyballlondon.exceptions.UserAlreadyExistsException;
import com.volleyballlondon.persistence.services.UserDbService;
import com.volleyballlondon.security.service.SecurityService;
import com.volleyballlondon.web.dto.UserDto;

@WebAppConfiguration(value = "file:WebContent")
@RunWith(MockitoJUnitRunner.class)

/**
 * Unit test for RegistrationController.
 * All services have been mocked.
 */
public class RegistrationControllerTest {

    @Mock
    UserDbService userDbService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    RegistrationController registrationController;
    
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
            .alwaysDo(print())
            .build();
    }

    /**
    * @see RegistrationController#registerUser(UserDto,BindingResult,Model)
    * @verifies create new user registration
    *
    * user "z" is a new user.
    * should enter "userloginpage" after registration
    */
    @Test
    public void registerUser_shouldCreateNewUserRegistration() throws Exception {

        System.out.println();
        System.out.println("*** Starting test registerUser_shouldCreateNewUserRegistration ***");
        System.out.println();

        Mockito
            .when(userDbService.registerNewUserAccount(any(UserDto.class),
                Mockito.anyString()))
            .thenReturn(true);

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "z");
        param.add("password", "2");

        MvcResult mvcResult = this.mockMvc.perform(post("/registration").params(param))
            .andExpect(status().isOk())
            .andExpect(view().name("userloginpage"))
            .andReturn();
    }

    /**
    * @see RegistrationController#registerUser(UserDto,BindingResult,Model)
    * @verifies fail if user already exists
    *
    * Mocked userDbService.registerNewUserAccount to throw an exception
    * to replicated an 'user already exists' scenario.
    * should return user to registration screen and display error message
    */
    @Test
    public void registerUser_shouldFailIfUserAlreadyExists() throws Exception {

        System.out.println();
        System.out.println("*** Starting test registerUser_shouldFailIfUserAlreadyExists ***");
        System.out.println();

        Mockito
            .when(userDbService.registerNewUserAccount(any(UserDto.class),
                Mockito.anyString()))
            .thenThrow(new UserAlreadyExistsException("z"));

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "z");
        param.add("password", "2");

        MvcResult mvcResult = this.mockMvc.perform(post("/registration").params(param))
            .andExpect(status().isOk())
            .andExpect(view().name("registrationForm"))
            .andExpect(model().attribute("errorMessage",
                "User  z  already exists."))
            .andReturn();
    }

    /**
     * @see RegistrationController#showRegistrationForm()
     * @verifies return registration form
     *
     * should return registration form
     * should return DTO object
     */
    @Test
    public void showRegistrationForm_shouldReturnRegistrationForm() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/registration"))
                .andExpect(view().name("registrationForm"))
                .andExpect(model().attribute("userDTO", new UserDto()))
                .andExpect(status().isOk())
                .andReturn();
    }
}
