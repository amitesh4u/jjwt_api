package com.amitesh.main;

import com.amitesh.configuration.JwtConfiguration;
import com.amitesh.pojo.UserData;
import com.amitesh.util.JwtHelper;
import com.amitesh.util.SignatureAlgoKeyType;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Arrays;

public class JwtDemo {

  private static final Gson GSON = new Gson();

  public static void main(String[] args) {
    UserData userDataIn = new UserData("Joe", 123);
    System.out.println("UserData Input: " + userDataIn);

    final String payload = GSON.toJson(userDataIn);
    String encryptedJwsClaims = JwtHelper.encryptClaims(payload, SignatureAlgoKeyType.ASYMMETRIC);
    System.out.println("Encoded value: " + encryptedJwsClaims);

    Jws<Claims> jwsClaims = JwtHelper.decryptClaims(encryptedJwsClaims, SignatureAlgoKeyType.ASYMMETRIC);

    System.out.println("Decoded value: " + jwsClaims);
    if (null != jwsClaims) {
      System.out.println("Algorithm: " + jwsClaims.getHeader().getAlgorithm());

      Claims payloadOut = jwsClaims.getPayload();
      System.out.println("JWT Id: " + payloadOut.getId());
      System.out.println("Issuer: " + payloadOut.getIssuer());
      System.out.println("Audience: " + payloadOut.getAudience());
      System.out.println("Issued At: " + payloadOut.getIssuedAt());
      System.out.println("Expiring At: " + payloadOut.getExpiration());
      System.out.println("Claim Name: " + payloadOut.get(JwtConfiguration.CLAIM_KEY_NAME));
      System.out.println("Claim Scope: " + payloadOut.get(JwtConfiguration.CLAIM_KEY_SCOPE));
      System.out.println("Signature: " + Arrays.toString(jwsClaims.getDigest()));

      String subject = payloadOut.getSubject();
      System.out.println("Subject: " + subject);

      UserData userDataOut = GSON.fromJson(subject, UserData.class);
      System.out.println("UserData Output: " + userDataOut);

      System.out.println("Is UserData object same? " + userDataIn.equals(userDataOut));
    }
  }
}

