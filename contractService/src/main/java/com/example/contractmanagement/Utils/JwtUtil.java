package com.example.contractmanagement.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    public static String genToken(String claims){
        return JWT.create()
                .withClaim("userInfo",claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *12))
                .sign(Algorithm.HMAC256("H.I.D.E.404"));
    }

    public static String parseToken(String token){
        return JWT.require(Algorithm.HMAC256("H.I.D.E.404"))
                .build()
                .verify(token)
                .getClaim("userInfo")
                .asString();
    }
}
