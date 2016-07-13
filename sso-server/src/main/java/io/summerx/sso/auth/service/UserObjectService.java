package io.summerx.sso.auth.service;

import io.summerx.sso.auth.UserObject;

/**
 * Created by xiayg on 7/12/2016.
 */
public interface UserObjectService {

    UserObject loadUserByUsername(String username);
}
