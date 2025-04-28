package com.example.scolarite.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class RapportStage {
	
	public RapportStage() {}
	
    public RapportStage(Long id, Utilisateur etudiant, String nomFichier, LocalDate dateDepot, byte[] fichier) {
		super();
		this.id = id;
		this.etudiant = etudiant;
		this.nomFichier = nomFichier;
		this.dateDepot = dateDepot;
		this.fichier = fichier;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Utilisateur etudiant;

    private String nomFichier;

    private LocalDate dateDepot;

    @Lob
    private byte[] fichier; // ou un chemin vers un fichier stocké

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Utilisateur getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Utilisateur etudiant) {
		this.etudiant = etudiant;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	public LocalDate getDateDepot() {
		return dateDepot;
	}

	public void setDateDepot(LocalDate dateDepot) {
		this.dateDepot = dateDepot;
	}

	public byte[] getFichier() {
		return fichier;
	}

	public void setFichier(byte[] fichier) {
		this.fichier = fichier;
	}
    
    
}
