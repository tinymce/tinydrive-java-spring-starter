package cloud.tiny.tinydrivestarter;

import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;

public class JwtHelper {
  public static String createTinyDriveToken(String username, String fullname, Boolean scopeUser, String privateKey)
      throws IOException, GeneralSecurityException, JOSEException {
    Builder builder = new JWTClaimsSet.Builder();

    // Scopes the path to a specific user directory
    if (scopeUser) {
      builder.claim("https://claims.tiny.cloud/drive/root", "/" + username);
    }

    JWTClaimsSet claims = builder
      .subject(username)
      .claim("name", fullname)
      .issueTime(getDeltaTime(0))
      .expirationTime(getDeltaTime(10))
      .build();

    return createToken(claims, privateKey);
  }

  public static String createToken(JWTClaimsSet claims, String privateKey)
      throws IOException, GeneralSecurityException, JOSEException {
    PrivateKey pk = PrivateKeyReader.getPrivateKey(new StringReader(privateKey));
    JWSSigner signer = new RSASSASigner(pk);

    Payload payload = new Payload(claims.toJSONObject());
    JWSObject jwsObject = new JWSObject(new JWSHeader.Builder(JWSAlgorithm.RS256).build(), payload);

    jwsObject.sign(signer);

    return jwsObject.serialize();
  }

  private static Date getDeltaTime(int minutes) {
    LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(minutes, ChronoUnit.MINUTES));
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}