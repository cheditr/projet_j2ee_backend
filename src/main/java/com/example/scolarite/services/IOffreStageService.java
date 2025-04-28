package com.example.scolarite.services;

import java.util.List;

import com.example.scolarite.entities.OffreStage;

public interface IOffreStageService {
	public List<OffreStage> getAllOffres();
	public OffreStage createOffre(OffreStage offreStage);
	public OffreStage updateOffre(OffreStage offre);
	public void deleteOffre(Long id);

}
