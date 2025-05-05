package com.example.scolarite.controllers;

import com.example.scolarite.entities.RapportStage;
import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.repositories.RapportStageRepository;
import com.example.scolarite.repositories.UtilisateurRepository;
import com.example.scolarite.services.IRapportStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200") 	
@RestController
@RequestMapping("/api/rapports")
public class RapportStageController {

    @Autowired
    private IRapportStageService rapportStageService;
    
    @Autowired
    private RapportStageRepository rapportStageRepository;

    // Étudiant dépose un rapport
    @PostMapping("/deposer")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<String> deposerRapport(
    		@RequestParam("file") MultipartFile file,
            Authentication authentication) {

        // Récupérer l'utilisateur connecté (étudiant)
        Utilisateur etudiant = getUtilisateurFromAuthentication(authentication);
        try {
        	rapportStageService.deposerRapport(etudiant, file);
            return ResponseEntity.ok("PDF enregistré avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur d'enregistrement.");
        }
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
    
    @GetMapping("/telecharger/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> telechargerRapport(@PathVariable Long id) {
        Optional<RapportStage> rapportOpt = rapportStageRepository.findById(id);
        
        if (rapportOpt.isPresent()) {
            RapportStage rapport = rapportOpt.get();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rapport.getNomFichier() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(rapport.getFichier());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
