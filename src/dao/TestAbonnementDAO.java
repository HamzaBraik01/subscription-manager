// Exemple simplifié pour TestAbonnementDAO
package dao;

import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TestAbonnementDAO {
    public static void main(String[] args) throws DAOException {
        AbonnementDAO dao = new AbonnementDAOImpl();

        Abonnement a1 = new AbonnementAvecEngagement(
                "Netflix",
                new BigDecimal("50.0"),
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2026, 9, 1),
                Abonnement.Statut.ACTIVE,
                12
        );

        Abonnement a2 = new AbonnementSansEngagement(
                "Spotify",
                new BigDecimal("30.0"),
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 12, 1),
                Abonnement.Statut.RESILIE
        );

        dao.create(a1);
        dao.create(a2);
        System.out.println("Création terminée.");

        Optional<Abonnement> found = dao.findById(a1.getId());
        System.out.println("findById: " + found);

        List<Abonnement> all = dao.findAll();
        System.out.println("findAll:");
        all.forEach(System.out::println);

        a1.setNomService("Netflix Premium");
        dao.update(a1);
        System.out.println("Update terminé. findById après update:");
        System.out.println(dao.findById(a1.getId()));

        List<Abonnement> activeSubs = dao.findActiveSubscriptions();
        System.out.println("Abonnements actifs:");
        activeSubs.forEach(System.out::println);

        List<Abonnement> avecEngagement = dao.findByType(AbonnementAvecEngagement.class);
        System.out.println("Abonnements avec engagement:");
        avecEngagement.forEach(System.out::println);

        dao.delete(a2.getId());
        System.out.println("Suppression terminée. findAll après delete:");
        dao.findAll().forEach(System.out::println);
    }
}
