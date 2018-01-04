package stickers.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMappingController {
    @GetMapping("admin")
    public  String adminMain() {
        return "admin";
    }
}
