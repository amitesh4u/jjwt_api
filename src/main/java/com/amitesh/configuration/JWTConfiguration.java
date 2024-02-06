package com.amitesh.configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.KeyPair;

public class JWTConfiguration {

  /* We need a signing key, so we'll create one just for this example. Usually
   the key would be read from your application configuration instead.
   */
  private static final Key SECRET_KEY_HS256 = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  /* Testing random key generation logic */
//    static{
//        for(int i = 1 ; i < 10 ; i++){
//           System.out.println(
//               Arrays.toString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));
//        }
//    }
  private static final Key SECRET_KEY_HS384 = Keys.secretKeyFor(SignatureAlgorithm.HS384);
  private static final Key SECRET_KEY_HS512 = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  private static final KeyPair SECRET_KEY_RS512 = Keys.keyPairFor(SignatureAlgorithm.RS512);
  private static final Key DUMMY_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final long TTL_MILLISECOND = 5000;
  private static final long CLOCK_SKEW_SEC = 1000;
  private static final String KEY_VAL = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJuYW1lXCI6XCJKb2VcIixcImlkXCI6MTIzfSIsImlhdCI6MTU1MTE2MzEyMiwiZXhwIjoxNTUxMTYzMTI0fQ.bLmibkwAmkD_vhq53rig9-jjl8wNZZcvpXFbs2LDacw0yfOPCIUXax8nLwCbcVDFfTfdUrwb-zl0EWz29M0ahg";
  private static final Key SECRET_KEY_CUSTOM = Keys.hmacShaKeyFor(KEY_VAL.getBytes());
  private JWTConfiguration() {
  }

  public static long getClockSkewSec() {
    return CLOCK_SKEW_SEC;
  }

  public static Key getSecretKey() {
    return SECRET_KEY_CUSTOM;
  }

  public static KeyPair getSecretKeyPair() {
    return SECRET_KEY_RS512;
  }

  public static Key getDummySecretKey() {
    return DUMMY_SECRET_KEY;
  }

  public static long getTTL() {
    return TTL_MILLISECOND;
  }
}
