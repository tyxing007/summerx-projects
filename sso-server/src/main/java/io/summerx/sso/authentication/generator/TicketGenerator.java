package io.summerx.sso.authentication.generator;

import io.summerx.sso.authentication.UserAuthorization;

/**
 * 凭证生成器.
 *
 * @author summerx
 * @Date 2016-07-15 10:18 AM
 */
public interface TicketGenerator {

    /**
     * 生成TGT凭证
     * @param authorization
     * @return
     */
    String generateTgt(UserAuthorization authorization);

    /**
     * 生成ST凭证
     * @param authorization
     * @return
     */
    String generateProvisionalCredentials(UserAuthorization authorization, String app);

    /**
     * 生成PT凭证
     * @param authorization
     * @return
     */
    String generatePt(UserAuthorization authorization);
}
