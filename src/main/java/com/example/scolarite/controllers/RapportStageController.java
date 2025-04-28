package com.example.scolarite.controllers;

import com.example.scolarite.entities.RapportStage;
import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.repositories.UtilisateurRepository;
import com.example.scolarite.services.IRapportStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rapports")
public class RapportStageController {

    @Autowired
    private IRapportStageService rapportStageService;

    // Étudiant dépose un rapport
    @PostMapping("/deposer")
    @PreAuthorize("hasRole('ETUDIANT')")
    public RapportStage deposerRapport(
            @RequestParam("nomFichier") String nomFichier,
            @RequestParam("fichier") byte[] fichier,
            Authentication authentication) {

        // Récupérer l'utilisateur connecté (étudiant)
        Utilisateur etudiant = (Utilisateur) authentication.getPrincipal();

        // Appeler la méthode du service pour déposer le rapport
        return rapportStageService.deposerRapport(etudiant, nomFichier, fichier);
    }

    // Étudiant consulte ses rapports
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    

    @GetMapping("/mes-rapports")
    public List<?> getMesRapports(Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurFromAuthentication(authentication);
        
        // Ici tu utilises utilisateur comme avant
        System.out.println("Utilisateur connecté : " + utilisateur.getUsername());

        // Exemple de retour (adapte selon ton besoin réel)
        return List.of(); // Remplacer par le vrai service pour récupérer les rapports
    }

    private Utilisateur getUtilisateurFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec username : " + username));
    }
    
    // Admin voit tous les rapports
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RapportStage> getAllRapports() {
        return rapportStageService.getAllRapports();
    }


}
