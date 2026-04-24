package myapp.security;

import myapp.model.Member;
import myapp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    // Gère la connexion de Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> memberOpt = memberService.getMemberByEmail(email);
        if (memberOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
        }

        Member member = memberOpt.get();
        String role = (member.getRole() != null) ? member.getRole() : "MEMBER";
        return User.withUsername(member.getEmail())
                .password(member.getPassword())
                .roles(role)
                .build();
    }
}