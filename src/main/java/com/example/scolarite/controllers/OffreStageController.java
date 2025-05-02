package com.example.scolarite.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scolarite.entities.OffreStage;
import com.example.scolarite.services.IOffreStageService;

@CrossOrigin("http://localhost:4200") 	
@RestController
@RequestMapping("/api/offres")
public class OffreStageController {
    @Autowired
    private IOffreStageService offreStageService;
    
    //Consulter toutes les offres(accessible pour tous)
    @GetMapping
    public List<OffreStage> getAllOffres() {
        return offreStageService.getAllOffres();
    }
    
    //2.créer une offre(seul pour les admins)
    @PostMapping("/publier-offre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> publierOffre(@RequestBody OffreStage offreStage) {
        OffreStage createdOffre = offreStageService.createOffre(offreStage);
        return ResponseEntity.ok("Offre publiée avec succès !");
    }

    // Modifier une offre (admin seulement)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OffreStage updateOffre(@RequestBody OffreStage updatedOffre) {
        return offreStageService.updateOffre(updatedOffre);
    }

    // Supprimer une offre (admin seulement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOffre(@PathVariable Long id) {
        offreStageService.deleteOffre(id);
    }



}
