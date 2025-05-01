package com.example.scolarite.services;

import com.example.scolarite.entities.Document;
import com.example.scolarite.entities.Utilisateur;
import com.example.scolarite.repositories.DocumentRepository;
import com.example.scolarite.repositories.UtilisateurRepository;
import com.example.scolarite.services.IDocumentService;

import io.jsonwebtoken.io.IOException;

import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.FileSystemResource;

import jakarta.annotation.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;



@Service
public class DocumentServiceImpl implements IDocumentService {

    private final DocumentRepository documentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final String stockagePath = "documents/";

    public DocumentServiceImpl(DocumentRepository documentRepository, UtilisateurRepository utilisateurRepository) {
        this.documentRepository = documentRepository;
        this.utilisateurRepository = utilisateurRepository;
        // Création du répertoire de stockage s'il n'existe pas
        File directory = new File(stockagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public List<Document> getDocumentsParEtudiant(Long etudiantId) {
        Utilisateur etudiant = utilisateurRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return documentRepository.findByEtudiant(etudiant);
    }

    @Override
    public Document getDocumentParId(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
    }

    @Override
    public Document genererDemandeStage(Long etudiantId) {
        Utilisateur etudiant = utilisateurRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        String contenu = creerContenuDemandeStage(etudiant);
        String filename = "demande_stage_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".html";

        try {
            // Création du fichier
            String cheminComplet = stockagePath + filename;
            
            // >>> Déplacer la création du FileWriter ici dans le try

            try {
                FileWriter fileWriter = new FileWriter(cheminComplet);
                fileWriter.write(contenu);
				fileWriter.close();
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Enregistrement dans la base de données
            Document document = new Document();
            document.setType(Document.TypeDocument.DEMANDE_STAGE);
            document.setContenu(contenu);
            document.setCheminFichier(cheminComplet);
            document.setEtudiant(etudiant);

            return documentRepository.save(document);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la génération du document: " + e.getMessage());
        }
    }
    @Override
    public Document genererLettreAffectation(Long etudiantId) {
        Utilisateur etudiant = utilisateurRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        Document document = new Document();
        document.setType(Document.TypeDocument.LETTRE_AFFECTATION);
        document.setContenu(creerContenuLettreAffectation(etudiant));
        document.setEtudiant(etudiant);
        document.setDateGeneration(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public Resource telechargerDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document non trouvé"));
        
        try {
            Path file = Paths.get(document.getCheminFichier());
            if (Files.exists(file)) {
                return (Resource) new FileSystemResource(file.toFile());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fichier non trouvé");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Erreur lors du téléchargement du document: " + e.getMessage());
        }
    }

    // ======================
    // CREATION DU CONTENU
    // ======================

    private String creerContenuDemandeStage(Utilisateur etudiant) {
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Demande de Stage</title>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; }
                        .header { text-align: center; margin-bottom: 30px; }
                        .date { text-align: right; margin-bottom: 50px; }
                        .signature { margin-top: 80px; text-align: right; }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>UNIVERSITÉ XYZ</h2>
                        <h3>Département Informatique</h3>
                    </div>
                    
                    <div class="date">
                        Le %s
                    </div>
                    
                    <h3>Objet: Demande de stage</h3>
                    
                    <p>
                        À qui de droit,
                    </p>
                    
                    <p>
                        Je soussigné(e), %s, étudiant(e) en Informatique
                        à l'Université XYZ, ai l'honneur de solliciter
                        auprès de votre entreprise un stage de formation.
                    </p>
                    
                    <p>
                        Ce stage s'inscrit dans le cadre de ma formation universitaire
                        et constitue une étape essentielle pour valider mon année académique.
                    </p>
                    
                    <p>
                        Dans l'attente d'une réponse favorable, je vous prie d'agréer,
                        Madame, Monsieur, l'expression de mes salutations distinguées.
                    </p>
                    
                    <div class="signature">
                        %s<br>
                        Étudiant(e) en Informatique
                    </div>
                </body>
                </html>
                """;

        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return String.format(template,
                maintenant.format(formatter),
                etudiant.getUsername(),
                etudiant.getUsername());
    }

    private String creerContenuLettreAffectation(Utilisateur etudiant) {
        // Vous pouvez créer un autre template similaire ici pour la lettre d'affectation
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Lettre d'Affectation</title>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; }
                        .header { text-align: center; margin-bottom: 30px; }
                        .date { text-align: right; margin-bottom: 50px; }
                        .signature { margin-top: 80px; text-align: right; }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>UNIVERSITÉ XYZ</h2>
                        <h3>Département Informatique</h3>
                    </div>
                    
                    <div class="date">
                        Le %s
                    </div>
                    
                    <h3>Objet: Lettre d'affectation de stage</h3>
                    
                    <p>
                        Nous attestons que %s, étudiant(e) inscrit(e) en Département Informatique
                        à l'Université XYZ, est affecté(e) pour un stage pratique à votre entreprise.
                    </p>
                    
                    <p>
                        Ce stage est requis dans le cadre du programme académique de l'étudiant.
                    </p>
                    
                    <p>
                        Veuillez agréer, Madame, Monsieur, l'expression de nos salutations distinguées.
                    </p>
                    
                    <div class="signature">
                        Responsable pédagogique<br>
                        Université XYZ
                    </div>
                </body>
                </html>
                """;

        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return String.format(template,
                maintenant.format(formatter),
                etudiant.getUsername());
    }
}
