package luis.angel.doctorapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/")
    public String index() {
        return "index.html";  // Esto sirve la página principal de tu aplicación React
    }
}
