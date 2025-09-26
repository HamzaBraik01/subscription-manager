package dao;

import entity.Paiement;
import java.util.List;
import java.util.Optional;

public interface PaiementDAO {
    void create(Paiement paiement) throws DAOException;
    Optional<Paiement> findById(String idPaiement) throws DAOException;
    List<Paiement> findByAbonnement(String idAbonnement) throws DAOException;
    List<Paiement> findAll() throws DAOException;
    void update(Paiement paiement) throws DAOException;
    void delete(String idPaiement) throws DAOException;
    List<Paiement> findUnpaidByAbonnement(String idAbonnement) throws DAOException;
    List<Paiement> findLastPayments(int limit) throws DAOException;
}
