package myapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class StoreController {

    @Autowired
    Store store;

    @ModelAttribute("store")
    public Store newStore() {
        return store;
    }

    @GetMapping("/")
    public String show() {
        return "store";
    }

    @GetMapping("/open")
    public String login() {
        store.setState("It's open");
        return "store";
    }

    @GetMapping("/close")
    public String logout() {
        store.setState("It's close");
        return "store";
    }
}