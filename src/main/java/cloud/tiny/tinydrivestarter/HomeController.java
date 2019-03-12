package cloud.tiny.tinydrivestarter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@EnableAutoConfiguration
public class HomeController {
  private final Config config;

  @Autowired
  ApplicationContext context;

  @Autowired
  public HomeController(Config config) {
    this.config = config;
  }

  @GetMapping("/")
  public ModelAndView index(Model model) {
    return new ModelAndView("index");
  }

  @PostMapping(value="/login")
  public ModelAndView indexPost(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
    if (Auth.loginUser(config, username, password)) {
      return new ModelAndView("redirect:/editor");
    } else {
      model.addAttribute("message", "Incorrect username/password.");
      return new ModelAndView("index");
    }
  }

  @GetMapping("/editor")
  public ModelAndView editor(Model model) {
    Optional<Config.User> user = Auth.getLoggedInUser(config);

    if (user.isPresent()) {
      user.ifPresent(u -> {
        model.addAttribute("apiKey", this.config.getApiKey());
        model.addAttribute("fullname", u.getFullName());
      });

      return new ModelAndView("editor");
    } else {
      return new ModelAndView("redirect:/");
    }
  }

  @GetMapping("/logout")
  public ModelAndView logout() {
    Auth.logout();
    return new ModelAndView("redirect:/");
  }
}