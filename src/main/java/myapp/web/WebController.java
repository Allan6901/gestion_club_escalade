package myapp.web;

import myapp.dao.CategoryDAO;
import myapp.dao.MemberDAO;
import myapp.dao.TripDAO;
import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class WebController {

    private static final int PAGE_SIZE = 20;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private TripDAO tripDAO;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String categoryTrips(@PathVariable Long id,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        Optional<Category> category = categoryDAO.getCategoryById(id);
        if (category.isEmpty()) return "redirect:/categories";

        Page<Trip> tripPage = tripDAO.getTripsByCategoryPageable(id, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("category", category.get());
        model.addAttribute("trips", tripPage.getContent());
        model.addAttribute("currentPage", tripPage.getNumber());
        model.addAttribute("totalPages", tripPage.getTotalPages());
        model.addAttribute("totalElements", tripPage.getTotalElements());
        return "category_trips";
    }

    @GetMapping("/trips/{id}")
    public String tripDetail(@PathVariable Long id, Model model) {
        Optional<Trip> tripOpt = tripDAO.getTripById(id);
        if (tripOpt.isEmpty()) return "redirect:/categories";

        Trip trip = tripOpt.get();
        model.addAttribute("trip", trip);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !auth.getPrincipal().equals("anonymousUser");
        boolean isAdmin = isAuthenticated && isAdmin(auth);

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isAdmin", isAdmin);

        boolean isParticipant = false;
        boolean canManage = isAdmin;

        if (isAuthenticated) {
            Optional<Member> memberOpt = memberDAO.getMemberByEmail(auth.getName());
            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                if (!isAdmin) {
                    isParticipant = tripDAO.isParticipant(id, member.getId());
                    // Le créateur peut gérer sa sortie
                    canManage = trip.getCreator().getId().equals(member.getId());
                }
            }
        }
        model.addAttribute("isParticipant", isParticipant);
        model.addAttribute("canManage", canManage);

        return "trip_detail";
    }

    @GetMapping("/trips/search")
    public String searchTrips(@RequestParam(required = false) String name,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) Long memberId,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        Date start = (startDate != null && !startDate.isBlank()) ? Date.valueOf(startDate) : null;
        Date end   = (endDate   != null && !endDate.isBlank())   ? Date.valueOf(endDate)   : null;

        Page<Trip> tripPage = tripDAO.searchTripsPageable(
                (name != null && !name.isBlank()) ? name : null,
                categoryId,
                memberId,
                start,
                end,
                PageRequest.of(page, PAGE_SIZE)
        );

        model.addAttribute("trips", tripPage.getContent());
        model.addAttribute("categories", categoryDAO.getAllCategories());
        model.addAttribute("currentPage", tripPage.getNumber());
        model.addAttribute("totalPages", tripPage.getTotalPages());
        model.addAttribute("totalElements", tripPage.getTotalElements());
        model.addAttribute("searchName", name);
        model.addAttribute("searchCategoryId", categoryId);
        model.addAttribute("searchMemberId", memberId);
        model.addAttribute("searchStartDate", startDate);
        model.addAttribute("searchEndDate", endDate);

        if (memberId != null) {
            memberDAO.getMemberById(memberId).ifPresent(m ->
                model.addAttribute("creatorFilter", m.getFirstName() + " " + m.getLastName())
            );
        }

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
        member.setPassword(passwordEncoder.encode(password));
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

    // ======================== Gestion des sorties (tout membre authentifié) ========================

    /** Page "Mes inscriptions" */
    @GetMapping("/member/my-registrations")
    public String myRegistrations(@RequestParam(defaultValue = "0") int page, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        memberDAO.getMemberByEmail(auth.getName()).ifPresent(member -> {
            Page<Trip> tripPage = tripDAO.getRegisteredTripsByMemberPageable(
                    member.getId(), PageRequest.of(page, PAGE_SIZE));
            model.addAttribute("trips", tripPage.getContent());
            model.addAttribute("currentPage", tripPage.getNumber());
            model.addAttribute("totalPages", tripPage.getTotalPages());
            model.addAttribute("totalElements", tripPage.getTotalElements());
            model.addAttribute("memberName", member.getFirstName() + " " + member.getLastName());
        });
        return "my_registrations";
    }

    /** Page "Mes sorties" */
    @GetMapping("/member/my-trips")
    public String myTrips(@RequestParam(defaultValue = "0") int page, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        memberDAO.getMemberByEmail(auth.getName()).ifPresent(member -> {
            Page<Trip> tripPage = tripDAO.getTripsByCreatorPageable(
                    member.getId(), PageRequest.of(page, PAGE_SIZE));
            model.addAttribute("trips", tripPage.getContent());
            model.addAttribute("currentPage", tripPage.getNumber());
            model.addAttribute("totalPages", tripPage.getTotalPages());
            model.addAttribute("totalElements", tripPage.getTotalElements());
            model.addAttribute("memberName", member.getFirstName() + " " + member.getLastName());
        });
        return "my_trips";
    }

    /** Formulaire de création */
    @GetMapping("/member/trips/new")
    public String newTripForm(Model model) {
        model.addAttribute("trip", new Trip());
        model.addAttribute("categories", categoryDAO.getAllCategories());
        return "trip_form";
    }

    /** Créer ou modifier une sortie */
    @PostMapping("/member/trips")
    public String saveTrip(@ModelAttribute Trip trip, @RequestParam Long categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> memberOpt = memberDAO.getMemberByEmail(auth.getName());
        if (memberOpt.isEmpty()) return "redirect:/login";

        Member currentMember = memberOpt.get();
        boolean isAdmin = isAdmin(auth);
        Optional<Category> category = categoryDAO.getCategoryById(categoryId);

        if (trip.getId() == null) {
            // Nouvelle sortie — le créateur est le membre courant
            trip.setCreator(currentMember);
            category.ifPresent(trip::setCategory);
            Trip saved = tripDAO.createTrip(trip);
            return "redirect:/trips/" + saved.getId();
        } else {
            // Modification — vérifier la propriété
            Optional<Trip> existingOpt = tripDAO.getTripById(trip.getId());
            if (existingOpt.isEmpty()) return "redirect:/member/my-trips";

            Trip existing = existingOpt.get();
            if (!isAdmin && !existing.getCreator().getId().equals(currentMember.getId())) {
                return "redirect:/member/my-trips";
            }
            // Mise à jour des champs éditables uniquement (creator inchangé)
            existing.setName(trip.getName());
            existing.setDescription(trip.getDescription());
            existing.setWebsite(trip.getWebsite());
            existing.setDate(trip.getDate());
            category.ifPresent(existing::setCategory);
            tripDAO.updateTrip(existing);
            return "redirect:/trips/" + existing.getId();
        }
    }

    /** Formulaire de modification avec vérification propriété */
    @GetMapping("/member/trips/{id}/edit")
    public String editTripForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> memberOpt = memberDAO.getMemberByEmail(auth.getName());
        if (memberOpt.isEmpty()) return "redirect:/login";

        Optional<Trip> tripOpt = tripDAO.getTripById(id);
        if (tripOpt.isEmpty()) return "redirect:/member/my-trips";

        Trip trip = tripOpt.get();
        if (!isAdmin(auth) && !trip.getCreator().getId().equals(memberOpt.get().getId())) {
            return "redirect:/member/my-trips";
        }

        model.addAttribute("trip", trip);
        model.addAttribute("categories", categoryDAO.getAllCategories());
        return "trip_form";
    }

    /** Suppression avec vérification propriété */
    @PostMapping("/member/trips/{id}/delete")
    public String deleteTrip(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> memberOpt = memberDAO.getMemberByEmail(auth.getName());
        if (memberOpt.isEmpty()) return "redirect:/login";

        Optional<Trip> tripOpt = tripDAO.getTripById(id);
        if (tripOpt.isEmpty()) return "redirect:/member/my-trips";

        Trip trip = tripOpt.get();
        if (!isAdmin(auth) && !trip.getCreator().getId().equals(memberOpt.get().getId())) {
            return "redirect:/member/my-trips";
        }

        tripDAO.deleteTrip(id);
        return "redirect:/member/my-trips";
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
            member.setPassword(passwordEncoder.encode(password));
            memberDAO.updateMember(member);
            model.addAttribute("message", "Mot de passe modifié avec succès. Vous pouvez vous connecter.");
        }
        return "login";
    }

    // ======================== Helper ========================

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}