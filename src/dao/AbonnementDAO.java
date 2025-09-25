package dao;

import entity.Abonnement;
import java.util.List;
import java.util.Optional;

public interface AbonnementDAO {
    void create(Abonnement abonnement) throws DAOException;
    Optional<Abonnement> findById(String id) throws DAOException;
    List<Abonnement> findAll() throws DAOException;
    void update(Abonnement abonnement) throws DAOException;
    void delete(String id) throws DAOException;
    List<Abonnement> findActiveSubscriptions() throws DAOException;
    List<Abonnement> findByType(Class<? extends Abonnement> type) throws DAOException;
}
