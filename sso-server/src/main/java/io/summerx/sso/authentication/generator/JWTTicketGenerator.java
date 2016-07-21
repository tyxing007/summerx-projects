package io.summerx.sso.authentication.generator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.summerx.sso.authentication.UserAuthorization;

import java.util.Date;

/**
 * 基于JWT的凭证生成器
 *   使用JWT时，你可以将用户身份信息加密到JWT Token中，无需通过缓存等手段来实现保存用户身份信息。
 *   此时你应该采用不同的密钥来加密TGT，ST和PT。
 *   其中PT的密钥需要告知应用端（应用端根据密钥解密PT获得用户身份信息），另外不同应用的PT请采取不同的密钥加密。
 *
 *   完整的流程应该是应用端申请接入SSO服务器，SSO服务器分配一个secureKey给应用端，针对该应用的请求通过分配的secureKey加密PT凭证
 *   应用端通过分配的secureKey解密PT凭证获取用户身份信息。
 *
 *   这里只是一个简单的实现。
 *
 * @author summerx
 * @Date 2016-07-15 10:18 AM
 */
public class JWTTicketGenerator implements TicketGenerator {

    /**
     * 默认的密钥
     */
    public static final String DEFAULT_JWT_KEY = "superjwtkey";

    /**
     * 默认的过期时间
     */
    public static final Long DEFAULT_EXP_MILLS = 60 * 60 * 1000l;

    // 密钥
    private String jwtKey = DEFAULT_JWT_KEY;

    // TGT过期时间（默认1小时）
    private Long tgtExpires = 60 * 60 * 1000l;

    // ST过期时间（默认15秒）
    private Long stExpires = 15 * 1000l;

    // PT过期时间（默认30分钟）
    private Long ptExpires = 30 * 60 * 1000l;

    public String generateTgt(UserAuthorization authorization) {
        Date exp = new Date(System.currentTimeMillis() + tgtExpires);
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtKey)
                .setIssuer(authorization.getUsername())
                .setExpiration(exp)
                .claim("username", authorization.getUsername())
                .claim("prefix", "TGT")
                .compact();

        return token;
    }

    public String generateProvisionalCredentials(UserAuthorization authorization, String app) {
        Date exp = new Date(System.currentTimeMillis() + stExpires);
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtKey)
                .setIssuer(authorization.getUsername())
                .setExpiration(exp)
                .claim("username", authorization.getUsername())
                .claim("prefix", "ST")
                .compact();

        return token;
    }

    public String generatePt(UserAuthorization authorization) {
        Date exp = new Date(System.currentTimeMillis() + ptExpires);
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtKey)
                .setIssuer(authorization.getUsername())
                .setExpiration(exp)
                .claim("username", authorization.getUsername())
                .claim("prefix", "PT")
                .compact();

        return token;
    }

    public void setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public void setTgtExpires(Long tgtExpires) {
        this.tgtExpires = tgtExpires;
    }

    public void setStExpires(Long stExpires) {
        this.stExpires = stExpires;
    }

    public void setPtExpires(Long ptExpires) {
        this.ptExpires = ptExpires;
    }
}
