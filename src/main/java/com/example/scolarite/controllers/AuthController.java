package com.example.scolarite.controllers;

import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody Utilisateur utilisateur) {
        try {
            System.out.println("Tentative d'authentification pour l'utilisateur: " + utilisateur.getUsername());
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(utilisateur.getUsername(), utilisateur.getPassword())
            );
            
            System.out.println("Authentification réussie");
            
            // Après l'authentification réussie, générer un JWT
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", utilisateur.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erreur d'authentification: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Erreur d'authentification: " + e.getMessage());
        }
    }
}