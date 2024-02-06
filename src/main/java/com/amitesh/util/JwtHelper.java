package com.amitesh.util;

import com.amitesh.configuration.JWTConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

public class JwtHelper {

  private JwtHelper() {
  }

  /*
      eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.I12a3j-7rr4DtsUFAGJaeUSVxQYdqAqInK-onmrCDOA
      base64URLEncode( header.getBytes("UTF-8") ).base64URLEncode( claims.getBytes("UTF-8") ).base64URLEncode( signature )
  */
  public static String encode(final String payload) {
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    JwtBuilder jwtBuilder = Jwts.builder().setSubject(payload).setIssuedAt(now);
    System.out.println("Set payload: " + payload);

    //if TTL has been specified, let's add the expiration time
    long ttl = JWTConfiguration.getTTL();
    if (ttl >= 0) {
      System.out.println("Get TTL in millisecond: " + ttl);
      long expMillis = nowMillis + ttl;
      Date exp = new Date(expMillis);
      jwtBuilder.setExpiration(exp);
      System.out.println("Current Time: " + nowMillis);
      System.out.println("Set expiration: " + exp.getTime());
    }
    final Key secretKey = JWTConfiguration.getSecretKey();
    System.out.println(
        "Get Primary Encoded format of Key: " + Arrays.toString(secretKey.getEncoded()));
    System.out.println("Get Key Algorithm: " + secretKey.getAlgorithm());
    System.out.println("Get Key Format: " + secretKey.getFormat());

    return jwtBuilder.signWith(secretKey).compact();
  }

  public static String decode(final String encodedPayload) {
    return decodeClaims(encodedPayload).getBody().getSubject();
  }

  public static Jws<Claims> decodeClaims(final String encodedPayload) {
    try {
      return Jwts.parser()
          // .setAllowedClockSkewSeconds(JWTConfiguration.getClockSkewSec())
          .setSigningKey(JWTConfiguration.getSecretKey())
          .parseClaimsJws(encodedPayload);
    } catch (JwtException e) {
      //don't trust the JWT!
            /*
            io.jsonwebtoken.security.SignatureException: JWT signature does not match locally computed signature.
            JWT validity cannot be asserted and should not be trusted.
             */
      e.printStackTrace();
    }
    return null;
  }
}
