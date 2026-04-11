package myapp.web.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecureService {

    @PreAuthorize("hasAuthority('ADMIN')")
    public String helloAdmin() {
        return "Hello";
    }

    @PreAuthorize("#userName == principal.username")
    public String helloForUser(String userName) {
        return "Hello " + userName;
    }

}