package io.summerx.sso.auth.service;

import io.summerx.sso.auth.UserObject;

/**
 * Created by xiayg on 7/12/2016.
 */
public class DefaultUserObjectService implements UserObjectService {

    public UserObject loadUserByUsername(String username) {
        return new UserObject() {
            @Override
            public String getPassword() {
                return "admin123";
            }

            @Override
            public String getUsername() {
                return "admin";
            }

            @Override
            public boolean isAccountLocked() {
                return false;
            }

            @Override
            public boolean isPasswordExpired() {
                return false;
            }
        };
    }
}
