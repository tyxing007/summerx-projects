package io.summerx.sso.authentication.service;

import io.summerx.sso.authentication.userdetails.DefaultUserDetails;
import io.summerx.sso.authentication.userdetails.UserDetails;

/**
 * Created by xiayg on 7/12/2016.
 */
public class MockUserService implements UserService {

    public UserDetails loadUserByUsername(String username) {
        return new DefaultUserDetails("admin", "admin123");
    }
}
