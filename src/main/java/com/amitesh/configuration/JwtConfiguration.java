package com.amitesh.configuration;

import com.amitesh.util.SignatureAlgoKeyType;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Curve;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.Keys;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;

public class JwtConfiguration {

  public static final String JWT_ID = "jjwt.ud";
  public static final String ISSUER = "amitesh.jjwt";
  public static final String AUDIENCE = "self";
  public static final String CLAIM_KEY_NAME = "name";
  public static final String CLAIM_VALUE_NAME = "user-data";
  public static final String CLAIM_KEY_SCOPE = "scope";
  public static final String CLAIM_VALUE_SCOPE = "admin";

  public static final long TTL_MILLISECOND = 30000;

  /* Add tolerance in case of time discrepancy between multiple auth servers */
  public static final long CLOCK_SKEW_SEC = 10;

  public static final PrivateKey ASYMMETRIC_PRIVATE_KEY;

  public static final PublicKey ASYMMETRIC_PUBLIC_KEY;

  private static final String SYMMETRIC_KEY_ARRAY = """
      eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJuYW1lXCI6XCJKb2VcIixcImlkXCI6MTIzfSIsImlhdCI6MTU1MTE2MzEyMiwiZXhwIjoxNTUxMTYzMTI
      0fQ.bLmibkwAmkD_vhq53rig9-jjl8wNZZcvpXFbs2LDacw0yfOPCIUXax8nLwCbcVDFfTfdUrwb-zl0EWz29M0ahgeyJhbGciOiJIUzUxMiJ9.eyJzdW
      IiOiJ7XCJuYW1lXCI6XCJKb2VcIixcImlkXCI6MTIzfSIsImlhdCI6MTU1MTE2MzEyMiwiZXhwIjoxNTUxMTYzMTI0fQ.bLmibkwAmkD_vhq53rig9-jjl
      8wNZZcvpXFbs2LDacw0yfOPCIUXax8nLwCbcVDFfTfdUrwb-zl0EWz29M0ahg
      """;
  /* We need a signing key, so we'll create one just for this example. Usually
   the key would be read from your application configuration instead.
   */
  /* Dynamically select algorithm i.e. HmacSHA512, HmacSHA256 based on Bytes length.
     For fixed algorithm please use following style: SIG.HS256.key().build();
   */
  private static final SecretKey SYMMETRIC_SECRET_KEY = Keys.hmacShaKeyFor(SYMMETRIC_KEY_ARRAY.getBytes());

  static {
    /* Create a test key suitable for the EdDSA signature algorithm using Ed25519 or Ed448 keys
      - Ed25519 algorithm keys must be 255 bits long and produce signatures 512 bits (64 bytes) long.
      - Ed448 algorithm keys must be 448 bits long and produce signatures 912 bits (114 bytes) long.

      Should come statically from configuration file
    */
    Curve curve = Jwks.CRV.Ed25519; //or Ed448
    KeyPair pair = curve.keyPair().build();
    ASYMMETRIC_PRIVATE_KEY = pair.getPrivate();
    ASYMMETRIC_PUBLIC_KEY = pair.getPublic();
  }

  private JwtConfiguration() {
  }

  public static JwtBuilder signWith(final SignatureAlgoKeyType signatureAlgoKeyType) {
    return switch (signatureAlgoKeyType) {
      case SignatureAlgoKeyType.ASYMMETRIC ->
          Jwts.builder().signWith(ASYMMETRIC_PRIVATE_KEY, SIG.EdDSA);
      case SignatureAlgoKeyType.SYMMETRIC -> Jwts.builder().signWith(SYMMETRIC_SECRET_KEY);
    };
  }

  public static JwtParserBuilder verifyWith(final SignatureAlgoKeyType signatureAlgoKeyType) {
    return switch (signatureAlgoKeyType) {
      case SignatureAlgoKeyType.ASYMMETRIC -> Jwts.parser().verifyWith(ASYMMETRIC_PUBLIC_KEY);
      case SignatureAlgoKeyType.SYMMETRIC -> Jwts.parser().verifyWith(SYMMETRIC_SECRET_KEY);
    };
  }
}
