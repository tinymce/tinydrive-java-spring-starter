package cloud.tiny.tinydrivestarter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@EnableAutoConfiguration
public class JwtController {
  @Autowired
  ApplicationContext context;

  @GetMapping("/jwt")
  public TokenResult index() throws IOException {
    if (session().getAttribute("x") != null) {
      session().setAttribute("x", "x" + session().getAttribute("x"));
    } else {
      session().setAttribute("x", "");
    }

    Resource resource = context.getResource("classpath:private.key");
    String privateKey = getString(resource.getInputStream());

    return new TokenResult("hello" + session().getAttribute("x") + privateKey);
  }

  public static HttpSession session() {
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attr.getRequest().getSession(true); // true == allow create
  }

  private String getString(InputStream is) throws IOException {
    int ch;
    StringBuilder sb = new StringBuilder();
    while ((ch = is.read()) != -1) {
        sb.append((char) ch);
    }
    return sb.toString();
}
}