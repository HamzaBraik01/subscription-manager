package dao;

import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AbonnementDAOImpl implements AbonnementDAO {

    @Override
    public void create(Abonnement abonnement) throws DAOException {
        String sql = "INSERT INTO abonnement " +
                "(id, nom_service, montant_mensuel, date_debut, date_fin, statut, type_abonnement, duree_engagement_mois) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(abonnement.getId())); // UUID correct
            ps.setString(2, abonnement.getNomService());
            ps.setBigDecimal(3, abonnement.getMontantMensuel());
            ps.setDate(4, Date.valueOf(abonnement.getDateDebut()));
            ps.setDate(5, Date.valueOf(abonnement.getDateFin()));
            ps.setString(6, mapStatutToDB(abonnement.getStatut()));
            ps.setString(7, abonnement instanceof AbonnementAvecEngagement ? "AvecEngagement" : "SansEngagement");
            ps.setObject(8, abonnement instanceof AbonnementAvecEngagement ?
                    ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois() : null);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la création d'un abonnement", e);
        }
    }

    @Override
    public Optional<Abonnement> findById(String id) throws DAOException {
        String sql = "SELECT * FROM abonnement WHERE id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAbonnement(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche d'un abonnement", e);
        }
    }

    @Override
    public List<Abonnement> findAll() throws DAOException {
        String sql = "SELECT * FROM abonnement";
        List<Abonnement> list = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToAbonnement(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de tous les abonnements", e);
        }
    }

    @Override
    public void update(Abonnement abonnement) throws DAOException {
        String sql = "UPDATE abonnement SET nom_service=?, montant_mensuel=?, date_debut=?, date_fin=?, statut=?, type_abonnement=?, duree_engagement_mois=? WHERE id=?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, abonnement.getNomService());
            ps.setBigDecimal(2, abonnement.getMontantMensuel());
            ps.setDate(3, Date.valueOf(abonnement.getDateDebut()));
            ps.setDate(4, Date.valueOf(abonnement.getDateFin()));
            ps.setString(5, mapStatutToDB(abonnement.getStatut()));
            ps.setString(6, abonnement instanceof AbonnementAvecEngagement ? "AvecEngagement" : "SansEngagement");
            ps.setObject(7, abonnement instanceof AbonnementAvecEngagement ?
                    ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois() : null);
            ps.setObject(8, UUID.fromString(abonnement.getId()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour d'un abonnement", e);
        }
    }

    @Override
    public void delete(String id) throws DAOException {
        String sql = "DELETE FROM abonnement WHERE id=?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(id));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression d'un abonnement", e);
        }
    }

    @Override
    public List<Abonnement> findActiveSubscriptions() throws DAOException {
        String sql = "SELECT * FROM abonnement WHERE statut='Active'";
        List<Abonnement> list = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToAbonnement(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération des abonnements actifs", e);
        }
    }

    @Override
    public List<Abonnement> findByType(Class<? extends Abonnement> type) throws DAOException {
        String typeStr = type.equals(AbonnementAvecEngagement.class) ? "AvecEngagement" : "SansEngagement";
        String sql = "SELECT * FROM abonnement WHERE type_abonnement=?";
        List<Abonnement> list = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeStr);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAbonnement(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération des abonnements par type", e);
        }
    }

    private String mapStatutToDB(Abonnement.Statut statut) {
        switch (statut) {
            case ACTIVE: return "Active";
            case SUSPENDU: return "Suspendu";
            case RESILIE: return "Resilie";
            default: throw new IllegalArgumentException("Statut inconnu: " + statut);
        }
    }

    private Abonnement mapResultSetToAbonnement(ResultSet rs) throws SQLException {
        Abonnement.Statut statut = mapDBToStatut(rs.getString("statut"));

        String type = rs.getString("type_abonnement");
        if ("AvecEngagement".equals(type)) {
            return new AbonnementAvecEngagement(
                    rs.getString("nom_service"),
                    rs.getBigDecimal("montant_mensuel"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    statut,
                    rs.getInt("duree_engagement_mois")
            );
        } else {
            return new AbonnementSansEngagement(
                    rs.getString("nom_service"),
                    rs.getBigDecimal("montant_mensuel"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    statut
            );
        }
    }

    private Abonnement.Statut mapDBToStatut(String dbStatut) throws SQLException {
        if ("Active".equals(dbStatut)) return Abonnement.Statut.ACTIVE;
        if ("Suspendu".equals(dbStatut)) return Abonnement.Statut.SUSPENDU;
        if ("Resilie".equals(dbStatut)) return Abonnement.Statut.RESILIE;
        throw new SQLException("Statut inconnu: " + dbStatut);
    }
}
