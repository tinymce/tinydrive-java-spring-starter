package cloud.tiny.tinydrivestarter;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Auth {
  public static boolean loginUser(Config config, String username, String password) {
    Optional<Config.User> user = findUser(config, username, password);

    user.ifPresent(u -> {
      session().setAttribute("username", u.getUserName());
    });

    return user.isPresent();
  }

  public static void logout() {
    session().removeAttribute("username");
  }

  public static Optional<Config.User> getLoggedInUser(Config config) {
    String userName = (String) session().getAttribute("username");
    return config.getUsers().stream()
      .filter(u -> u.getUserName().equals(userName))
      .findFirst();
  }

  private static HttpSession session() {
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attr.getRequest().getSession(true);
  }

  private static Optional<Config.User> findUser(Config config, String username, String password) {
    return config.getUsers().stream()
      .filter(u -> u.getUserName().equals(username) && u.getPassword().equals(password))
      .findFirst();
  }
}