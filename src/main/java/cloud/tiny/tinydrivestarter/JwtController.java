package cloud.tiny.tinydrivestarter;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpSession;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.util.StandardCharset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@EnableAutoConfiguration
public final class JwtController {
  private final Config config;

  @Autowired
  ApplicationContext context;

  @Autowired
  public JwtController(Config config) {
    this.config = config;
  }

  @PostMapping("/jwt")
  public TokenResult index() throws GeneralSecurityException, JOSEException, IOException {
    Config.User user = Auth.getLoggedInUser(this.config).orElseThrow(() -> new GeneralSecurityException("User is not logged in."));
    String token = JwtHelper.createTinyDriveToken(user.getUserName(), user.getFullName(), config.isScopedUser(), this.getPrivateKey());
    return new TokenResult(token);
  }

  public static HttpSession session() {
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attr.getRequest().getSession(true);
  }

  private String getPrivateKey() throws IOException{
    Resource resource = context.getResource("classpath:private.key");
    return StreamUtils.copyToString(resource.getInputStream(), StandardCharset.UTF_8);
  }

  static class TokenResult {
    private final String token;

    public TokenResult(String token) {
      this.token = token;
    }

    public String getToken() {
      return token;
    }
  }
}