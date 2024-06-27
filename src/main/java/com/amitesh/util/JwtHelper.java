package com.amitesh.util;

import static com.amitesh.configuration.JwtConfiguration.AUDIENCE;
import static com.amitesh.configuration.JwtConfiguration.CLAIM_KEY_NAME;
import static com.amitesh.configuration.JwtConfiguration.CLAIM_KEY_SCOPE;
import static com.amitesh.configuration.JwtConfiguration.CLAIM_VALUE_NAME;
import static com.amitesh.configuration.JwtConfiguration.CLAIM_VALUE_SCOPE;
import static com.amitesh.configuration.JwtConfiguration.CLOCK_SKEW_SEC;
import static com.amitesh.configuration.JwtConfiguration.ISSUER;
import static com.amitesh.configuration.JwtConfiguration.JWT_ID;
import static com.amitesh.configuration.JwtConfiguration.TTL_MILLISECOND;

import com.amitesh.configuration.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import java.util.Date;

public class JwtHelper {

  private JwtHelper() {
  }

  /*
      eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.I12a3j-7rr4DtsUFAGJaeUSVxQYdqAqInK-onmrCDOA
      base64URLEncode( header.getBytes("UTF-8") ).base64URLEncode( claims.getBytes("UTF-8") ).base64URLEncode( signature )
  */
  public static String encryptClaims(final String payload, final SignatureAlgoKeyType signatureAlgoKeyType) {
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    JwtBuilder jwtBuilder = JwtConfiguration.signWith(signatureAlgoKeyType)
        .id(JWT_ID)
        .issuer(ISSUER)
        .audience().add(AUDIENCE).and()
        .claim(CLAIM_KEY_NAME, CLAIM_VALUE_NAME)
        .claim(CLAIM_KEY_SCOPE, CLAIM_VALUE_SCOPE)
        .subject(payload)
        .issuedAt(now);
    System.out.println("Payload: " + payload);

    long ttl = TTL_MILLISECOND;
    System.out.println("TTL in millisecond: " + ttl);
    long expMillis = nowMillis + ttl;
    Date exp = new Date(expMillis);
    jwtBuilder.expiration(exp);
    jwtBuilder.notBefore(now); //The token should be rejected if the current time is before the time
    System.out.println("Current Time: " + nowMillis);
    System.out.println("Expiring At: " + exp.getTime());

    return jwtBuilder.compact();
  }

  public static Jws<Claims> decryptClaims(final String encodedPayload,
      final SignatureAlgoKeyType signatureAlgoKeyType) {
    try {
      return JwtConfiguration.verifyWith(signatureAlgoKeyType)
          .clockSkewSeconds(CLOCK_SKEW_SEC)
          .requireAudience(AUDIENCE)
          .requireId(JWT_ID)
          .requireIssuer(ISSUER)
          .require(CLAIM_KEY_NAME, CLAIM_VALUE_NAME)
          .require(CLAIM_KEY_SCOPE, CLAIM_VALUE_SCOPE)
          .build()
          .parseSignedClaims(encodedPayload);
    } catch (JwtException e) {
      //don't trust the JWT!
      System.err.println(e.getMessage());
    }
    return null;
  }
}
