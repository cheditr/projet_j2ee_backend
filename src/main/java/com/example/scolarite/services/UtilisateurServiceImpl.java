package com.example.scolarite.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.repositories.UtilisateurRepository;

@Service
public class UtilisateurServiceImpl implements IUtilisateurService {
	@Autowired
	UtilisateurRepository utilisateurRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
	    if (utilisateurRepository.existsByUsername(utilisateur.getUsername())) {
	        throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé");
	    }
	    if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
	        throw new RuntimeException("Cet email est déjà utilisé");
	    }

	    // --> Encoder le mot de passe avant de sauvegarder
	    utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));

	    return utilisateurRepository.save(utilisateur);

	}

	@Override
	public Optional<Utilisateur> GetUserById(Long id) {
		return utilisateurRepository.findById(id);
	}

	@Override
	public List<Utilisateur> GetUsers() {
		return utilisateurRepository.findAll();
	}

	@Override
	public Utilisateur updateUser(Utilisateur utilisateur) {
		return utilisateurRepository.save(utilisateur);
	}

	@Override
	public void deleteUser(Long id) {
		utilisateurRepository.deleteById(id);
		
	}

	@Override
	public void activerDesactiverUtilisateur(Long id, boolean actif) {
        Optional<Utilisateur> optUtilisateur = utilisateurRepository.findById(id);
        if (optUtilisateur.isPresent()) {
            Utilisateur utilisateur = optUtilisateur.get();
            utilisateur.setActif(actif);
            utilisateurRepository.save(utilisateur);
        } else {
            throw new RuntimeException("Utilisateur non trouvé");
        }	
	}
}
