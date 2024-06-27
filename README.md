# JJWT Api demo with version 0.12

https://github.com/jwtk/jjwt


## The JWT specification defines 7 reserved claims. These are:

- iss (issuer): Issuer of the JWT


- sub (subject): Subject of the JWT (the user)


- aud (audience): Recipient for which the JWT is intended


- exp (expiration time): Time after which the JWT expires


- nbf (not before time): Time before which the JWT must not be accepted for processing


- iat (issued at time): Time at which the JWT was issued; can be used to determine age of the JWT


- jti (JWT ID): Unique identifier; can be used to prevent the JWT from being replayed (allows a token to be used only once)


apart from these we can have custom claims
```
Jwts.builder().claim(CLAIM_KEY, CLAIM_VALUE)

Jws<Claims>.getPayload().get(CLAIM_KEY)
```

<hr/>

## Symmetric and Asymmetric Algorithm Keys
This application provides option to use either Symmetric or Asymmetric Signature algorithm keys.
To select please provide the Key type while encryption/decryption (same for both flows)
```
JwtHelper.encryptClaims(payload, SignatureAlgoKeyType.ASYMMETRIC);

JwtHelper.decryptClaims(encryptedJwsClaims, SignatureAlgoKeyType.SYMMETRIC);
```