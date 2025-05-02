package com.example.scolarite.controllers;

import com.example.scolarite.entities.Document;
import com.example.scolarite.services.IDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200") 	
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final IDocumentService documentService;

    public DocumentController(IDocumentService documentService) {
        this.documentService = documentService;
    }

    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ADMIN')")
    @GetMapping("/etudiant/{etudiantId}")
    public List<Document> getDocumentsParEtudiant(@PathVariable Long etudiantId) {
        return documentService.getDocumentsParEtudiant(etudiantId);
    }

    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Document getDocumentParId(@PathVariable Long id) {
        return documentService.getDocumentParId(id);
    }

    @PreAuthorize("hasRole('ETUDIANT')")
    @PostMapping("/generer/demande-stage/{etudiantId}")
    public Document genererDemandeStage(@PathVariable Long etudiantId) {
        return documentService.genererDemandeStage(etudiantId);
    }

    @PreAuthorize("hasRole('ETUDIANT')")
    @PostMapping("/generer/lettre-affectation/{etudiantId}")
    public Document genererLettreAffectation(@PathVariable Long etudiantId) {
        return documentService.genererLettreAffectation(etudiantId);
    }

    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ADMIN')")
    @GetMapping("/telecharger/{documentId}")
    public ResponseEntity<Resource> telechargerDocument(@PathVariable Long documentId) {
        Resource file = (Resource) documentService.telechargerDocument(documentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
