package com.example.scolarite.services;

import java.util.List;
import java.util.Optional;

import com.example.scolarite.entities.Utilisateur;

public interface IUtilisateurService {
	public Utilisateur creerUtilisateur(Utilisateur utilisateur);
	public Optional<Utilisateur> GetUserById(Long id);
	public List<Utilisateur> GetUsers();
	public Utilisateur updateUser(Utilisateur utilisateur);
	public void deleteUser(Long id);
	public void activerDesactiverUtilisateur(Long id, boolean actif);
	

}
