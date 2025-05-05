package com.example.scolarite.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.scolarite.entities.RapportStage;
import com.example.scolarite.entities.Utilisateur;

public interface IRapportStageService {
	public RapportStage deposerRapport(Utilisateur etudiant, MultipartFile file);
    List<RapportStage> getRapportsByEtudiant(Long idEtudiant);

    List<RapportStage> getAllRapports();

}
