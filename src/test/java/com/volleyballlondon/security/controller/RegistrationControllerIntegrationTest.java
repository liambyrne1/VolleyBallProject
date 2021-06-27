package com.volleyballlondon.security.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.volleyballlondon.persistence.config.DataConfig;
import com.volleyballlondon.persistence.model.Role;
import com.volleyballlondon.persistence.model.UserEntity;
import com.volleyballlondon.persistence.services.UserDbService;
import com.volleyballlondon.security.config.WebMvcConfig;
import com.volleyballlondon.web.dto.UserDto;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class, DataConfig.class })
@WebAppConfiguration(value = "src/main/java")

/**
 * Integration test for RegistrationController.
 * Tests all services.
 * Must reload database before running these tests.
 */
public class RegistrationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PasswordEncoder passwordEncoder;
 
    @Autowired
    UserDbService userDbService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
      mockMvc = MockMvcBuilders
              .webAppContextSetup(context)
              .apply(sharedHttpSession())
              .apply(springSecurity())
              .alwaysDo(print())
              .build();
    }

    /**
     * Verify the WebApplicationContext object (webApplicationContext)
     * has been loaded properly.
     * Also check that the right servletContext is being attached.
     */
    @Test
    public void givenWac_whenServletContext_thenItProvidesRegistrationController() {
        ServletContext servletContext = context.getServletContext();
        
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(context.getBean("registrationController"));
    }

    /**
    * @see RegistrationController#registerUser(UserDto,BindingResult,Model)
    * @verifies create new user registration
    *
    * user "e" is a new user.
    * should save username and password on database
    * should assign user with user role
    * should enter "userloginpage" after registration
    */
    @Test
    public void registerUser_shouldCreateNewUserRegistration() throws Exception {

        System.out.println();
        System.out.println("*** Starting test registerUser_shouldCreateNewUserRegistration ***");
        System.out.println();

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "e");
        param.add("password", "2");

        MvcResult mvcResult = this.mockMvc
            .perform(
                post("/registration")
                    .with(csrf())
                    .params(param)
            )
            .andExpect(status().isOk())
            .andExpect(authenticated().withUsername("e"))
            .andExpect(view().name("userloginpage"))
            .andReturn();

        assertDbUsers("e", "2");
    }

    /**
    * @see RegistrationController#registerUser(UserDto,BindingResult,Model)
    * @verifies fail if user already exists
    *
    * user "a" already exists
    * should return user to registration screen and display error message
    */
    @Test
    public void registerUser_shouldFailIfUserAlreadyExists() throws Exception {

        System.out.println();
        System.out.println("*** Starting test registerUser_shouldFailIfUserAlreadyExists ***");
        System.out.println();

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "a");
        param.add("password", "2");

        MvcResult mvcResult = this.mockMvc
            .perform(
                post("/registration")
                    .with(csrf())
                    .params(param)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("registrationForm"))
            .andExpect(model().attribute("errorMessage",
                "User  a  already exists."))
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
        System.out.println();
        System.out.println("*** Starting test showRegistrationForm_shouldReturnRegistrationForm ***");
        System.out.println();
        MvcResult mvcResult = this.mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationForm"))
                .andExpect(model().attribute("userDTO", new UserDto()))
                .andReturn();
    }

    /**
     * I CANNOT GET THIS TEST WORKING. PLEASE COULD YOU HAVE A LOOK AT IT.
     *
     * I am trying to verify Spring validation.
     * In src\\main\java\com\volleyballlondon\web\dto\UserDto.Java
     * the username must be alphanumeric,
     * therefore username = "!" should cause a binding/validation error
     * with the error message "User Name can only be letters, numbers or spaces".
     * i.e. br.hasErrors() in the RegistrationController method registerUser
     * should be true.
     * But when a run the unit test it is false.
     *
     * When I run it through the application, it works.
     * 
     * I know you have to load Hibernate Validator to get Spring validation working,
     * but from the test output, Hibernate Validator seems to be loading.
     *
     * The test also creates a record on the database.
     */
    @Test
    public void registerUser_shouldHaveErrors() throws Exception {

        System.out.println();
        System.out.println("*** Starting test registerUser_shouldHaveErrors ***");
        System.out.println();

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "!");
        param.add("password", "2");

        MvcResult mvcResult = this.mockMvc
            .perform(
                post("/registration")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .params(param)
            )
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors("username"))
            .andReturn();
    }

    /**
     * Asserts that the given username is created on the database with the current
     * password, and has only ROLE_USER authority.
     */
    private void assertDbUsers(String expectedUsername, String expectedPassword){
        Optional<UserEntity> userEntityOptional = userDbService.getById(expectedUsername);
        UserEntity userEntity = null;

        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
        } else {
            fail("User username " + expectedUsername + ", " +
                "password " + expectedPassword + " not created");
        }

        assertTrue("Incorrect password for user: " + expectedUsername +
            ", password: " + expectedPassword,
            passwordEncoder.matches(expectedPassword, userEntity.getPassword()));

        Collection<? extends GrantedAuthority> actualAuthorities =
            userEntity.getAuthorities();

        assertTrue("User username " + expectedUsername +
            " should only have one authority",
            actualAuthorities.size() == 1);

        assertTrue("User username " + expectedUsername +
            " should only have ROLE_USER authority",
            actualAuthorities.contains(Role.USER_AUTHORITY));
    }
}
