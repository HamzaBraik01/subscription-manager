package dao;

import entity.Paiement;
import entity.PaiementStatut;
import entity.PaiementType;

import java.time.LocalDate;
import java.util.Optional;

public class TestPaiementDAO {
    public static void main(String[] args) {

        PaiementDAO dao = new PaiementDAOImpl();

        try {
            // Créer un paiement
            Paiement paiement = new Paiement(
                "4528869f-6fb4-4f27-b5c5-cdff8d416689", 
                Optional.empty(),                         
                PaiementType.CB,
                PaiementStatut.NonPaye
            );

            dao.create(paiement);
            System.out.println("Paiement créé avec succès !");

            // Test de mise à jour du paiement
            paiement.setDatePaiement(Optional.of(LocalDate.now()));
            paiement.setStatut(PaiementStatut.Paye);

            // Récupérer tous les paiements
            dao.findAll().forEach(p -> {
                System.out.println("Paiement abonnement: " + p.getIdAbonnement() +
                                   ", échéance: " + p.getDateEcheance() +
                                   ", paiement: " + p.getDatePaiement().orElse(null) +
                                   ", type: " + p.getTypePaiement() +
                                   ", statut: " + p.getStatut());
            });

        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
