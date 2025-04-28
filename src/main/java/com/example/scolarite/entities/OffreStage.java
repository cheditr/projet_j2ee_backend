package com.example.scolarite.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OffreStage {
	
	//Contructeur par defaut
	public OffreStage() {}
	
	
	//Constructeur avec champs
    public OffreStage(Long id, String titre, String description, String entreprise, String periode, String lieu,
			String remuneration, String competencesRequises, LocalDate datePublication, String lienExterne) {
		super();
		this.id = id;
		this.titre = titre;
		this.description = description;
		this.entreprise = entreprise;
		this.periode = periode;
		this.lieu = lieu;
		this.remuneration = remuneration;
		this.competencesRequises = competencesRequises;
		this.datePublication = datePublication;
		this.lienExterne = lienExterne;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String entreprise;
    private String periode;
    private String lieu;
    private String remuneration;
    private String competencesRequises;
    private LocalDate datePublication;
    private String lienExterne;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntreprise() {
		return entreprise;
	}
	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}
	public String getPeriode() {
		return periode;
	}
	public void setPeriode(String periode) {
		this.periode = periode;
	}
	public String getLieu() {
		return lieu;
	}
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
	public String getRemuneration() {
		return remuneration;
	}
	public void setRemuneration(String remuneration) {
		this.remuneration = remuneration;
	}
	public String getCompetencesRequises() {
		return competencesRequises;
	}
	public void setCompetencesRequises(String competencesRequises) {
		this.competencesRequises = competencesRequises;
	}
	public LocalDate getDatePublication() {
		return datePublication;
	}
	public void setDatePublication(LocalDate datePublication) {
		this.datePublication = datePublication;
	}
	public String getLienExterne() {
		return lienExterne;
	}
	public void setLienExterne(String lienExterne) {
		this.lienExterne = lienExterne;
	}

}
