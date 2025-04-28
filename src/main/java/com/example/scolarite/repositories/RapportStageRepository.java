package com.example.scolarite.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scolarite.entities.RapportStage;

public interface RapportStageRepository extends JpaRepository<RapportStage, Long> {
	List<RapportStage> findByEtudiant_Id(Long idEtudiant);
}
