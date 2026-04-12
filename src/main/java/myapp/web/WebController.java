package myapp.web;

import myapp.dao.CategoryDAO;
import myapp.dao.MemberDAO;
import myapp.dao.TripDAO;
import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class WebController {

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private TripDAO tripDAO;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // ======================== Pages publiques ========================

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryDAO.getAllCategories());
        return "categories";
    }

    @GetMapping("/categories/{id}/trips")
    public String categoryTrips(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryDAO.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            model.addAttribute("trips", categoryDAO.getTripsByCategory(id));
            return "category_trips";
        }
        return "redirect:/categories";
    }

    @GetMapping("/trips/{id}")
    public String tripDetail(@PathVariable Long id, Model model) {
        Optional<Trip> trip = tripDAO.getTripById(id);
        if (trip.isEmpty()) {
            return "redirect:/categories";
        }

        model.addAttribute("trip", trip.get());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !auth.getPrincipal().equals("anonymousUser");
        boolean isAdmin = isAuthenticated && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isAdmin", isAdmin);

        if (isAuthenticated && !isAdmin) {
            memberDAO.getMemberByEmail(auth.getName()).ifPresent(member ->
                model.addAttribute("isParticipant", tripDAO.isParticipant(id, member.getId()))
            );
        }
        if (!model.containsAttribute("isParticipant")) {
            model.addAttribute("isParticipant", false);
        }

        return "trip_detail";
    }

    @GetMapping("/trips/search")
    public String searchTrips(@RequestParam(required = false) String name,
                              @RequestParam(required = false) Long categoryId,
                              Model model) {
        List<Trip> trips;
        if ((name != null && !name.isEmpty()) || categoryId != null) {
            trips = tripDAO.searchTrips(name, categoryId, null);
        } else {
            trips = tripDAO.getAllTrips();
        }
        model.addAttribute("trips", trips);
        model.addAttribute("categories", categoryDAO.getAllCategories());
        return "search";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ======================== Inscription à l'application ========================

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 Model model) {
        if (memberDAO.getMemberByEmail(email).isPresent()) {
            model.addAttribute("error", "Un compte existe déjà avec cet email.");
            return "register";
        }
        Member member = new Member();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setPassword(password);
        member.setRole("MEMBER");
        memberDAO.createMember(member);
        return "redirect:/login?registered";
    }

    // ======================== Inscription / désinscription à une sortie ========================

    @PostMapping("/trips/{id}/register")
    public String registerForTrip(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        memberDAO.getMemberByEmail(auth.getName()).ifPresent(member ->
            tripDAO.addParticipant(id, member.getId())
        );
        return "redirect:/trips/" + id;
    }

    @PostMapping("/trips/{id}/unregister")
    public String unregisterFromTrip(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        memberDAO.getMemberByEmail(auth.getName()).ifPresent(member ->
            tripDAO.removeParticipant(id, member.getId())
        );
        return "redirect:/trips/" + id;
    }

    // ======================== Gestion des sorties (ADMIN uniquement) ========================

    @GetMapping("/member/trips/new")
    public String newTripForm(Model model) {
        model.addAttribute("trip", new Trip());
        model.addAttribute("categories", categoryDAO.getAllCategories());
        return "trip_form";
    }

    @PostMapping("/member/trips")
    public String saveTrip(@ModelAttribute Trip trip, @RequestParam Long categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> member = memberDAO.getMemberByEmail(auth.getName());
        if (member.isPresent()) {
            trip.setCreator(member.get());
            categoryDAO.getCategoryById(categoryId).ifPresent(trip::setCategory);
            if (trip.getId() == null) {
                tripDAO.createTrip(trip);
            } else {
                tripDAO.updateTrip(trip);
            }
        }
        return "redirect:/categories";
    }

    @GetMapping("/member/trips/{id}/edit")
    public String editTripForm(@PathVariable Long id, Model model) {
        Optional<Trip> trip = tripDAO.getTripById(id);
        if (trip.isPresent()) {
            model.addAttribute("trip", trip.get());
            model.addAttribute("categories", categoryDAO.getAllCategories());
            return "trip_form";
        }
        return "redirect:/categories";
    }

    @PostMapping("/member/trips/{id}/delete")
    public String deleteTrip(@PathVariable Long id) {
        tripDAO.deleteTrip(id);
        return "redirect:/categories";
    }

    // ======================== Récupération de mot de passe ========================

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@RequestParam String email, Model model) {
        Optional<Member> member = memberDAO.getMemberByEmail(email);
        if (member.isPresent()) {
            String token = UUID.randomUUID().toString();
            if (mailSender != null) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("noreply@escalade.com");
                    message.setTo(email);
                    message.setSubject("Récupération de mot de passe");
                    message.setText("Pour réinitialiser votre mot de passe, cliquez sur ce lien : "
                            + "http://localhost:8080/reset-password?token=" + token + "&email=" + email);
                    mailSender.send(message);
                } catch (Exception e) {
                    System.out.println("Erreur d'envoi de mail simulé : " + e.getMessage());
                }
            }
            model.addAttribute("message", "Un email a été envoyé pour réinitialiser votre mot de passe.");
        } else {
            model.addAttribute("error", "Aucun compte trouvé avec cet email.");
        }
        return "forgot_password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String email, @RequestParam String token, Model model) {
        model.addAttribute("email", email);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordSubmit(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Member> memberOpt = memberDAO.getMemberByEmail(email);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setPassword(password);
            memberDAO.updateMember(member);
            model.addAttribute("message", "Mot de passe modifié avec succès. Vous pouvez vous connecter.");
        }
        return "login";
    }
}