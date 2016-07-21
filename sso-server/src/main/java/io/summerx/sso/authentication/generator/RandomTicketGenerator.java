package io.summerx.sso.authentication.generator;

import io.summerx.framework.utils.RandomUtils;
import io.summerx.sso.authentication.UserAuthorization;

/**
 * 一个完全随机的凭证生成器
 *
 * @author summerx
 * @Date 2016-07-15 10:18 AM
 */
public class RandomTicketGenerator implements TicketGenerator {

    public String generateTgt(UserAuthorization authorization) {
        return "TGT_" + RandomUtils.generateAlphaNumberSequence(32);
    }

    public String generateProvisionalCredentials(UserAuthorization authorization, String app) {
        return "ST_" + RandomUtils.generateAlphaNumberSequence(32);
    }

    public String generatePt(UserAuthorization authorization) {
        return "PT_" + RandomUtils.generateAlphaNumberSequence(32);
    }
}
