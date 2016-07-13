package io.summerx.sso.auth;

import io.summerx.framework.utils.RandomUtils;

/**
 * Created by xiayg on 7/11/2016.
 */
public class SSOHelper {

    public static String generateTgt(UsernamePasswordAuthorization authorization) {
        return "TGT_" + RandomUtils.generateAlphaNumberSequence(32);
    }

    public static String generateSt(UsernamePasswordAuthorization authorization) {
        return "ST_" + RandomUtils.generateAlphaNumberSequence(32);
    }
}
