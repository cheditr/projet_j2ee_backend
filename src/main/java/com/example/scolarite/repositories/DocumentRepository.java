package com.example.scolarite.repositories;

import org.springframework.stereotype.Repository;

import com.example.scolarite.entities.Document;
import com.example.scolarite.entities.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByEtudiant(Utilisateur utilisateur);
    
    List<Document> findByEtudiantAndType(Utilisateur utilisateur, Document.TypeDocument type);
    
    boolean existsByEtudiantAndType(Utilisateur utilisateur, Document.TypeDocument type);
}
