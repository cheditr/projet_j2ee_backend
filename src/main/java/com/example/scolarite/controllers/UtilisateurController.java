package com.example.scolarite.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.services.IUtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;

@CrossOrigin("http://localhost:4200") 	
@RestController 
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
	@Autowired
	IUtilisateurService utilisateurService;
	
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String creerUtilisateur(@RequestBody Utilisateur utilisateur) {
    	utilisateurService.creerUtilisateur(utilisateur);
    	return "Succes Enregistrement";
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public Utilisateur obtenirUtilisateur(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.GetUserById(id);
        return utilisateur.get();
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.GetUsers();
        return utilisateurs;
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public Utilisateur mettreAJourUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
    	return utilisateurService.updateUser(utilisateur);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String supprimerUtilisateur(@PathVariable Long id) {
    	utilisateurService.deleteUser(id);
    	return "User deleted";
    }
    

}
