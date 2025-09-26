package dao;

import entity.Paiement;
import entity.PaiementStatut;
import entity.PaiementType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void create(Paiement paiement) throws DAOException {
        String sql = "INSERT INTO paiement(id_abonnement, date_echeance, date_paiement, type_paiement, statut) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(paiement.getIdAbonnement()));
            ps.setDate(2, Date.valueOf(paiement.getDateEcheance()));
            ps.setDate(3, paiement.getDatePaiement().map(Date::valueOf).orElse(null));
            ps.setString(4, paiement.getTypePaiement().name());
            ps.setString(5, paiement.getStatut().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la création du paiement", e);
        }
    }

    @Override
    public Optional<Paiement> findById(String idPaiement) throws DAOException {
        String sql = "SELECT * FROM paiement WHERE id_paiement = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(idPaiement));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Paiement paiement = mapResultSet(rs);
                return Optional.of(paiement);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Erreur findById Paiement", e);
        }
    }

    @Override
    public List<Paiement> findByAbonnement(String idAbonnement) throws DAOException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_abonnement = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(idAbonnement));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur findByAbonnement Paiement", e);
        }
        return paiements;
    }

    @Override
    public List<Paiement> findAll() throws DAOException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur findAll Paiement", e);
        }
        return paiements;
    }

    @Override
    public void update(Paiement paiement) throws DAOException {
        String sql = "UPDATE paiement SET id_abonnement = ?, date_echeance = ?, date_paiement = ?, type_paiement = ?, statut = ? WHERE id_paiement = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(paiement.getIdAbonnement()));
            ps.setDate(2, Date.valueOf(paiement.getDateEcheance()));
            ps.setDate(3, paiement.getDatePaiement().map(Date::valueOf).orElse(null));
            ps.setString(4, paiement.getTypePaiement().name());
            ps.setString(5, paiement.getStatut().name());
            ps.setObject(6, UUID.fromString(paiement.getIdPaiement()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour du paiement", e);
        }
    }

    @Override
    public void delete(String idPaiement) throws DAOException {
        String sql = "DELETE FROM paiement WHERE id_paiement = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(idPaiement));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression du paiement", e);
        }
    }

    @Override
    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) throws DAOException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_abonnement = ? AND statut = 'NonPaye'";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(idAbonnement));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur findUnpaidByAbonnement Paiement", e);
        }
        return paiements;
    }

    @Override
    public List<Paiement> findLastPayments(int limit) throws DAOException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE date_paiement IS NOT NULL ORDER BY date_paiement DESC LIMIT ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur findLastPayments Paiement", e);
        }
        return paiements;
    }

    private Paiement mapResultSet(ResultSet rs) throws SQLException {
        return new Paiement(
            rs.getString("id_abonnement"),
            rs.getDate("date_echeance").toLocalDate(),
            Optional.ofNullable(rs.getDate("date_paiement")).map(Date::toLocalDate),
            PaiementType.valueOf(rs.getString("type_paiement")),
            PaiementStatut.valueOf(rs.getString("statut"))
        );
    }
}
