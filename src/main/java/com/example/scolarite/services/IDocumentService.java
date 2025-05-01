package com.example.scolarite.services;

import java.util.List;

import com.example.scolarite.entities.Document;

import jakarta.annotation.Resource;

public interface IDocumentService {
	public List<Document> getDocumentsParEtudiant(Long etudiantId);
	public Document getDocumentParId(Long id);
	public Document genererDemandeStage(Long etudiantId);
	public Document genererLettreAffectation(Long etudiantId);
	public Resource telechargerDocument(Long documentId);

}
