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
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200") 	
@RestController
@RequestMapping("/api/rapports")

public class RapportStageController {
    @Autowired
    private IRapportStageService rapportStageService;
    @Autowired
    private RapportStageRepository rapportStageRepository;
    @Autowired

    private UtilisateurRepository utilisateurRepository;
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

    @GetMapping("/mes-rapports")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<RapportDTO>> getMesRapports(Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurFromAuthentication(authentication);
        List<RapportStage> rapports = rapportStageService.getRapportsByEtudiant(utilisateur.getId());
        // Convertir en DTO pour ne pas envoyer tout le contenu binaire du fichier

        List<RapportDTO> rapportDTOs = rapports.stream()
            .map(rapport -> new RapportDTO(
                rapport.getId(),
                rapport.getNomFichier(),
                rapport.getDateDepot().toString()))

            .collect(Collectors.toList());
        return ResponseEntity.ok(rapportDTOs);

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
    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<String> supprimerRapport(@PathVariable Long id, Authentication authentication) {
        Utilisateur etudiant = getUtilisateurFromAuthentication(authentication);

        Optional<RapportStage> rapportOpt = rapportStageRepository.findById(id);
        if (rapportOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rapport introuvable.");
        }

        RapportStage rapport = rapportOpt.get();
        if (!rapport.getEtudiant().getId().equals(etudiant.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous ne pouvez supprimer que vos propres rapports.");
        }

        rapportStageRepository.delete(rapport);
        return ResponseEntity.ok("Rapport supprimé avec succès.");
    }

    @GetMapping("/telecharger/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<byte[]> telechargerRapport(@PathVariable Long id, Authentication authentication) {
        Optional<RapportStage> rapportOpt = rapportStageRepository.findById(id);
        if (rapportOpt.isPresent()) {
            RapportStage rapport = rapportOpt.get();           
            // Si c'est un étudiant, vérifier qu'il est bien le propriétaire du rapport
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ETUDIANT"))) {
                Utilisateur utilisateur = getUtilisateurFromAuthentication(authentication);

                if (!rapport.getEtudiant().getId().equals(utilisateur.getId())) {

                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

            }

            return ResponseEntity.ok()

                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rapport.getNomFichier() + "\"")

                    .contentType(MediaType.APPLICATION_PDF)

                    .body(rapport.getFichier());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Classe DTO pour les rapports (pour éviter d'envoyer le contenu binaire)

    public static class RapportDTO {

        private Long id;
        private String nomFichier;
        private String dateDepot;      
        public RapportDTO(Long id, String nomFichier, String dateDepot) {

            this.id = id;
            this.nomFichier = nomFichier;
            this.dateDepot = dateDepot;

        }      
        public Long getId() {

            return id;

        }        
        public String getNomFichier() {

            return nomFichier;
        }

        public String getDateDepot() {

            return dateDepot;
        }
    }
}

