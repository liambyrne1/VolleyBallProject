package com.volleyballlondon.security.controller;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import com.volleyballlondon.persistence.config.DataConfig;
import com.volleyballlondon.security.config.SecurityConfig;
import com.volleyballlondon.security.config.WebMvcConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class, SecurityConfig.class, DataConfig.class })
@WebAppConfiguration(value = "src/main/java")

/**
 * Integration test for LoginController.
 * Tests all services.
 */
public class LoginControllerIntegrationTest {
    private static final String LOGIN_ERROR_MESSAGE =
        "Username or Password is incorrect !!";

    private static final String LOGOUT_MESSAGE =
        "You have been successfully logged out !!";

    @Autowired
    private WebApplicationContext context;

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
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = context.getServletContext();
        
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(context.getBean("loginController"));
    }

    /**
    * @see LoginController#loginPage(String,String,Model)
    * @verifies authenticate given valid user and password
    *
    * user "a" has password "1" therefore should be authenticated.
    *
    * After being login, should be able to logout, returned to the login screen,
    * with the correct logout message.
    */
    @Test
    public void loginPage_shouldAuthenticateGivenValidUserAndPassword()
        throws Exception {
        System.out.println("*** Starting loginPage_shouldAuthenticateGivenValidUserAndPassword ***");

        String logoutUrl = "/login?logout=true";

        mockMvc
            .perform(formLogin().user("a").password("1"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/loginSuccess"))
            .andExpect(authenticated().withUsername("a"));

        mockMvc
            .perform(logout())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(logoutUrl));

        MvcResult mvcResult = mockMvc
            .perform(get(logoutUrl))
            .andExpect(forwardedUrl("/WEB-INF/views/login.jsp"))
            .andExpect(model().attribute("errorMessage", LOGOUT_MESSAGE))
            .andReturn();
    }

    /**
     * @see LoginController#loginPage(String,String,Model)
     * @verifies deny login given invalid password
     *
     * user "a" has password "1" not password "2"
     * therefore should be denied login
     * returned to the login screen
     * with the correct error message.
     */
    @Test
    public void loginPage_shouldDenyLoginGivenInvalidPassword() throws Exception {
        System.out.println("*** Starting loginPage_shouldDenyLoginGivenInvalidPassword ***");
        String loginErrorUrl = "/login?error=true";
        mockMvc
            .perform(formLogin().user("a").password("2"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(loginErrorUrl))
            .andExpect(unauthenticated());

        MvcResult mvcResult = mockMvc
            .perform(get(loginErrorUrl))
            .andExpect(model().attribute("errorMessage", LOGIN_ERROR_MESSAGE))
            .andExpect(forwardedUrl("/WEB-INF/views/login.jsp"))
            .andReturn();
    }

    /**
     * @see LoginController#loginPage(String,String,Model)
     * @verifies authenticate with admin role
     *
     * user "a" has ROLE_ADMIN authority,
     * should enter the adminloginpage after login
     */
    @Test
    public void loginPage_shouldAuthenticateWithAdminRole() throws Exception {
        System.out.println("*** Starting loginPage_shouldAuthenticateWithAdminRole ***");

        String successfulUrl = "/loginSuccess";

        mockMvc
            .perform(formLogin().user("a").password("1"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(successfulUrl))
            .andExpect(authenticated().withUsername("a"));

        mockMvc
            .perform(get(successfulUrl))
            .andExpect(forwardedUrl("/WEB-INF/views/adminloginpage.jsp"))
            .andExpect(authenticated().withUsername("a"));

        mockMvc
            .perform(logout())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login?logout=true"));
    }

    /**
     * @see LoginController#loginPage(String,String,Model)
     * @verifies authenticate with user role
     *
     * user "b" has ROLE_USER authority,
     * should enter the userloginpage after login
     */
    @Test
    public void loginPage_shouldAuthenticateWithUserRole() throws Exception {
        System.out.println("*** Starting loginPage_shouldAuthenticateWithUserRole ***");

        String successfulUrl = "/loginSuccess";

        mockMvc
            .perform(formLogin().user("b").password("1"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(successfulUrl))
            .andExpect(authenticated().withUsername("b"));

        mockMvc
            .perform(get(successfulUrl))
            .andExpect(forwardedUrl("/WEB-INF/views/userloginpage.jsp"))
            .andExpect(authenticated().withUsername("b"));

        mockMvc
            .perform(logout())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login?logout=true"));
    }

    /**
     * @see LoginController#loginPage(String,String,Model)
     * @verifies direct to login given unauthenticated user
     */
    @Test
    public void loginPage_shouldDirectToLoginGivenUnauthenticatedUser() throws Exception {
        System.out.println("*** Starting loginPage_shouldDirectToLoginGivenUnauthenticatedUser ***");

        String successfulUrl = "/loginSuccess";

        mockMvc
            .perform(get(successfulUrl))
            .andExpect(redirectedUrl("http://localhost/login"))
            .andExpect(unauthenticated());

    }

}
