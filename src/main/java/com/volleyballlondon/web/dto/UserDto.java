package com.volleyballlondon.web.dto;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.volleyballlondon.web.validation.annotation.PasswordMatches;
import com.volleyballlondon.web.validation.annotation.ValidEmail;

@PasswordMatches
public class UserDto {
    @Size(min = 1, message = "First Name is Required.")
    @Size(max = 30, message = "First Name can only be 30 characters or less.")
    @Pattern(regexp="^[a-zA-Z]+",
        message = "First Name can only be letters.")  
    private String firstName;

    @Size(min = 1, message = "Last Name is Required.")
    @Size(max = 30, message = "Last Name can only be 30 characters or less.")
    @Pattern(regexp="^[a-zA-Z]+",
        message = "Last Name can only be letters.")  
    private String lastName;

    @Size(min = 1, message = "Email is Required.")
    @ValidEmail
    private String email;

    @Size(min = 1, message = "Password is Required.")
    @Size(min = 8, message = "Password has to be between 8 - 25 characters.")
    @Size(max = 25, message = "Password has to be between 8 - 25 characters.")
    private String password;

    @Size(min = 1, message = "Confirmation is Required.")
    private String matchingPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [email=")
                .append(email)
                .append(", password=")
                .append(password)
                .append("]");
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UserDto)) {
            return false;
        }
        UserDto other = (UserDto) obj;
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        return true;
    }

}
