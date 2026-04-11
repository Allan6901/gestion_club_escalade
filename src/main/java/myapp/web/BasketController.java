package myapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/basket")
// Pour ce contrôleur, la donnée "basket"
// est stockée en session.
@SessionAttributes("basket")
public class BasketController {

    // Création d'un panier dans le modèle et donc aussi la session
    @ModelAttribute("basket")
    public Basket emptyBasket() {
        return new Basket();
    }

    @GetMapping("/")
    public String showBasket(@ModelAttribute("basket") Basket basket) {
        return "basket";
    }

    @GetMapping("/add/{product}")
    public String addProduct(//
            @ModelAttribute("basket") Basket basket, //
            @PathVariable("product") String product, //
            RedirectAttributes attributes) {
        System.out.println(product);
        basket.getProducts().add(product);
        attributes.addFlashAttribute("message", "Produit ajouté !");
        return "redirect:/basket/";
    }

    @GetMapping("/reset")
    public String resetBasket(//
            @ModelAttribute("basket") Basket basket, //
            RedirectAttributes attributes) {
        basket.getProducts().clear();
        attributes.addFlashAttribute("message", "Panier vidé.");
        return "redirect:/basket/";
    }
}