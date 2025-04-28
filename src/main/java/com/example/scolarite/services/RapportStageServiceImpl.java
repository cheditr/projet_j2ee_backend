package com.example.scolarite.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scolarite.entities.RapportStage;
import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.repositories.RapportStageRepository;

@Service
public class RapportStageServiceImpl implements IRapportStageService {
    @Autowired
    private RapportStageRepository rapportStageRepository;

    public RapportStage deposerRapport(Utilisateur etudiant, String nomFichier, byte[] fichier) {
        RapportStage rapport = new RapportStage();
        rapport.setEtudiant(etudiant);
        rapport.setNomFichier(nomFichier);
        rapport.setFichier(fichier);
        rapport.setDateDepot(LocalDate.now());
        return rapportStageRepository.save(rapport);
    }

	@Override
	public List<RapportStage> getRapportsByEtudiant(Long idEtudiant) {
		return rapportStageRepository.findByEtudiant_Id(idEtudiant);
	}

	@Override
	public List<RapportStage> getAllRapports() {
		return rapportStageRepository.findAll();
	}


}
