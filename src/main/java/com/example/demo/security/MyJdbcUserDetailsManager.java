package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class MyJdbcUserDetailsManager extends JdbcUserDetailsManager {
    private String usersByUsernameQuery = "select user_id, user_account_id, user_password, user_ from demo_user where user_account_id = ?";

    public MyJdbcUserDetailsManager() {
        super.setUsersByUsernameQuery(this.usersByUsernameQuery);
    }

    @Override
    public void createUser(final UserDetails user) {

    }
}
