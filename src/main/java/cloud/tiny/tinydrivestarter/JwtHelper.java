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

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

public class JwtHelper {
  public static String createTinyDriveToken(String username, String fullname, Boolean scopeUser, String privateKey)
      throws IOException, GeneralSecurityException, JOSEException {
    Builder builder = new JWTClaimsSet.Builder();

    // When this is set the user will only be able to manage and see files in the specified root
    // directory. This makes it possible to have a dedicated home directory for each user.
    if (scopeUser) {
      builder.claim("https://claims.tiny.cloud/drive/root", "/" + username);
    }

    JWTClaimsSet claims = builder.subject(username).claim("name", fullname).issueTime(getDeltaTime(0))
        .expirationTime(getDeltaTime(10)).build();

    return createToken(claims, privateKey);
  }

  public static String createToken(JWTClaimsSet claims, String privateKey)
      throws IOException, GeneralSecurityException, JOSEException {
    PrivateKey pk = readPrivateKey(privateKey, "");

    JWSSigner signer = new RSASSASigner(pk);

    Payload payload = new Payload(claims.toJSONObject());
    JWSObject jwsObject = new JWSObject(new JWSHeader.Builder(JWSAlgorithm.RS256).build(), payload);

    jwsObject.sign(signer);

    return jwsObject.serialize();
  }

  private static PrivateKey readPrivateKey(String privateKey, String keyPassword) throws IOException {
    PEMParser keyReader = new PEMParser(new StringReader(privateKey));
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
    PEMDecryptorProvider decryptionProv = new JcePEMDecryptorProviderBuilder().build(keyPassword.toCharArray());

    Object keyPair = keyReader.readObject();
    PrivateKeyInfo keyInfo;

    if (keyPair instanceof PEMEncryptedKeyPair) {
      PEMKeyPair decryptedKeyPair = ((PEMEncryptedKeyPair) keyPair).decryptKeyPair(decryptionProv);
      keyInfo = decryptedKeyPair.getPrivateKeyInfo();
    } else if (keyPair instanceof PEMKeyPair) {
      keyInfo = ((PEMKeyPair) keyPair).getPrivateKeyInfo();
    } else {
      keyInfo = (PrivateKeyInfo) keyPair;
    }

    keyReader.close();
    return converter.getPrivateKey(keyInfo);
  }

  private static Date getDeltaTime(int minutes) {
    LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(minutes, ChronoUnit.MINUTES));
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}