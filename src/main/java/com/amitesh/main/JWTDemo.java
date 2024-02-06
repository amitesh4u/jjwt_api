package com.amitesh.main;

import com.amitesh.pojo.UserData;
import com.amitesh.util.JwtHelper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JWTDemo {

  public static final Gson GSON = new Gson();

  public static void main(String[] args) {
    UserData userDataIn = new UserData();
    userDataIn.setId(123);
    userDataIn.setName("Joe");
    System.out.println("UserData Input: " + userDataIn);

    final String payload = GSON.toJson(userDataIn);
    String compactJws = JwtHelper.encode(payload);
    System.out.println("Encoded value: " + compactJws);

//        try {
//            Thread.sleep(JWTConfiguration.getTTL() + 1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    Jws<Claims> claimsJws = JwtHelper.decodeClaims(compactJws);

    System.out.println("Decoded value: " + claimsJws);
    if (null != claimsJws) {
      System.out.println("Algorithm: " + claimsJws.getHeader().getAlgorithm());
      String subject = claimsJws.getBody().getSubject();
      System.out.println("Subject: " + subject);
      System.out.println("Signature: " + claimsJws.getSignature());

      UserData userDataOut = GSON.fromJson(subject, UserData.class);
      System.out.println("UserData Output: " + userDataOut);

      System.out.println("Is UserData object same? " + userDataIn.equals(userDataOut));
    }
  }
}

