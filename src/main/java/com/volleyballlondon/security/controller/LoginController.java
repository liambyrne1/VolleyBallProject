package com.volleyballlondon.security.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.volleyballlondon.persistence.model.Role;
 
@Controller
public class LoginController
{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String USER_LOGIN_PAGE = "userloginpage";
    private static final String ADMIN_LOGIN_PAGE = "adminloginpage";

    /**
     * Returns the login screen to initiate a Spring login.
     * If "error" is set to true, displays an error message on screen.
     * If "logout" is set to true, displays an successful logout message
     * on screen.
     * 
     * @should authenticate given valid user and password
     * @should deny login given invalid password
     * @should direct to login given unauthenticated user
     * @should authenticate with user role
     * @should authenticate with admin role
     */
    @GetMapping("/login")
    public String loginPage(
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model model) {

        String errorMessage = null;
        if(error != null) {
            errorMessage = "Username or Password is incorrect !!";
        }
        if(logout != null) {
            errorMessage = "You have been successfully logged out !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
  
    /**
     * Logs out the current user.
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){   
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    /**
     * Called after a successful league.
     * Returns the screen depending on the user's role.
     */
	@GetMapping("/loginSuccess")
    public String loginSuccess(){
        String returnView = getViewByRole();

        System.out.println("Successfully authenticated. Security context contains: " +
            SecurityContextHolder.getContext().getAuthentication());

        return returnView;
    }

    /**
     * Returns the screen depending on the user's role.
     * If the user's role is "user" returns userloginpage.
     * If the user's role is "admin" returns adminloginpage.
     */
    private String getViewByRole(){
        String returnView;
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication.getAuthorities().contains(Role.ADMIN_AUTHORITY)) {
                returnView = ADMIN_LOGIN_PAGE;
            } else if (authentication.getAuthorities().contains(Role.USER_AUTHORITY)) {
                returnView = USER_LOGIN_PAGE;
            } else {
                returnView = "errorpage";
            }
        }
        else {
            returnView = "errorpage";
        }
        return returnView;
    }
}
