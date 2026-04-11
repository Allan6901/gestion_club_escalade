package myapp.web;

import myapp.model.Member;
import myapp.repo.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/members")
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "members";
    }

    @PostMapping("/members")
    public String addMember(@RequestParam String lastName, @RequestParam String firstName, @RequestParam String email, @RequestParam String password) {
        Member member = new Member();
        member.setLastName(lastName);
        member.setFirstName(firstName);
        member.setEmail(email);
        member.setPassword(password);
        memberRepository.save(member);
        return "redirect:/members";
    }

}
