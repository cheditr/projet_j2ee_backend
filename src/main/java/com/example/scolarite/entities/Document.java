package com.example.scolarite.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {
	
	public Document() {}
    
    public Document(Long id, TypeDocument type, String contenu, LocalDateTime dateGeneration, String cheminFichier,
			Utilisateur etudiant) {
		super();
		this.id = id;
		this.type = type;
		this.contenu = contenu;
		this.dateGeneration = dateGeneration;
		this.cheminFichier = cheminFichier;
		this.etudiant = etudiant;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument type;
    
    private String contenu;
    
    @Column(name = "date_generation")
    private LocalDateTime dateGeneration;
    
    @Column(name = "chemin_fichier")
    private String cheminFichier;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Utilisateur etudiant;
    
    @PrePersist
    protected void onCreate() {
        this.dateGeneration = LocalDateTime.now();
    }
    
    public enum TypeDocument {
        DEMANDE_STAGE,
        LETTRE_AFFECTATION
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TypeDocument getType() {
		return type;
	}

	public void setType(TypeDocument type) {
		this.type = type;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public LocalDateTime getDateGeneration() {
		return dateGeneration;
	}

	public void setDateGeneration(LocalDateTime dateGeneration) {
		this.dateGeneration = dateGeneration;
	}

	public String getCheminFichier() {
		return cheminFichier;
	}

	public void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	public Utilisateur getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Utilisateur etudiant) {
		this.etudiant = etudiant;
	}
}