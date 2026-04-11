package myapp.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myapp.model.XUser;
import myapp.repo.XUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private XUserRepository userRepository;

    /*
     * À partir d'un XUser, chargé depuis la base, nous allons construire une
     * instance de la classe User offerte par Spring Security (même si nous
     * n'utilisons pas toutes les possibilités). La classe User est une
     * implémentation de l'interface UserDetails qui représente un utilisateur
     * authentifié dans Spring Security.
     * 
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        XUser xuser = userRepository.findById(username)//
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(xuser.getUserName())// Compte userName
                .password(xuser.getPassword())// Mot de passe
                .authorities(xuser.getAuthorities().toArray(new String[0]))// Autorisations
                .disabled(false)// Compte toujours actif
                .accountExpired(false)// Compte jamais expiré
                .accountLocked(false)// Compte jamais verrouillé
                .credentialsExpired(false)// mot de passe jamais expiré
                .build();
    }
}