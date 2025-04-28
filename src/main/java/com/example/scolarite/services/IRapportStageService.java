package com.example.scolarite.services;

import java.util.List;

import com.example.scolarite.entities.RapportStage;
import com.example.scolarite.entities.Utilisateur;

public interface IRapportStageService {
	public RapportStage deposerRapport(Utilisateur etudiant, String nomFichier, byte[] fichier);
    List<RapportStage> getRapportsByEtudiant(Long idEtudiant);

    List<RapportStage> getAllRapports();

}
