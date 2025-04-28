package com.example.scolarite.security;

import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByUsernameAndActifTrue(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
            
            System.out.println("Utilisateur trouvé: " + utilisateur.getUsername() + ", rôle: " + utilisateur.getRole());
            
            return new User(
                    utilisateur.getUsername(),
                    utilisateur.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()))  // Utilisation de "ROLE_" + le nom de l'énumération
            );
        } catch (Exception e) {
            System.err.println("Erreur dans loadUserByUsername: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
