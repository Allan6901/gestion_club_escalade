package myapp.web;

import myapp.model.Membre;
import myapp.repo.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MembreController {

    @Autowired
    private MembreRepository membreRepository;

    @GetMapping("/membres")
    public String listMembres(Model model) {
        model.addAttribute("membres", membreRepository.findAll());
        return "membres";
    }

    @PostMapping("/membres")
    public String addMembre(@RequestParam String nom, @RequestParam String prenom, @RequestParam String email, @RequestParam String motDePasse) {
        Membre membre = new Membre();
        membre.setNom(nom);
        membre.setPrenom(prenom);
        membre.setEmail(email);
        membre.setMotDePasse(motDePasse);
        membreRepository.save(membre);
        return "redirect:/membres";
    }

}
