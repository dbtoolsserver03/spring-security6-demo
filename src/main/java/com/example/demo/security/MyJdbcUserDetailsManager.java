package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class MyJdbcUserDetailsManager extends JdbcUserDetailsManager {
    public static final String ROLE_PREFIX = "ROLE_";
    private final String usersByUsernameQuery = "select * from users where id = ?";
    private final String authoritiesByUsernameQuery = "select ua.user_id, auth.auth_name from user_authority ua inner join authorities auth on ua.authority_id = auth.id where id = ?";

    public MyJdbcUserDetailsManager() {
        super.setRolePrefix(ROLE_PREFIX);
        super.setUsersByUsernameQuery(this.usersByUsernameQuery);
        super.setAuthoritiesByUsernameQuery(this.authoritiesByUsernameQuery);
    }

    @Override
    public void createUser(final UserDetails user) {

    }
}
