package com.example.scolarite.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scolarite.entities.OffreStage;
import com.example.scolarite.repositories.OffreStageRepository;

@Service
public class OffreStageServiceImpl implements IOffreStageService {
    @Autowired
    private OffreStageRepository offreStageRepository;

	@Override
	public List<OffreStage> getAllOffres() {
		return offreStageRepository.findAll();
	}

	@Override
	public OffreStage createOffre(OffreStage offreStage) {
		offreStage.setDatePublication(LocalDate.now());
		return offreStageRepository.save(offreStage);
	}

	@Override
	public OffreStage updateOffre(OffreStage offre) {
		return offreStageRepository.save(offre);
		
	}

	@Override
	public void deleteOffre(Long id) {
		offreStageRepository.deleteById(id);
	}

}
