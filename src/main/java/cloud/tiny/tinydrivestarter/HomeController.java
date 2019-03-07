package cloud.tiny.tinydrivestarter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableAutoConfiguration
public class HomeController {
  @GetMapping("/")
  public String index(Model model) {
     model.addAttribute("message", "Hello world!");
     return "index";
  }
}