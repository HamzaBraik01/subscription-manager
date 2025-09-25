package entity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class Paiement {
    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private Optional<LocalDate> datePaiement;
    private PaiementType typePaiement;
    private PaiementStatut statut;

    // Constructeur
    public Paiement(String idAbonnement, LocalDate dateEcheance,
                    Optional<LocalDate> datePaiement,
                    PaiementType typePaiement, PaiementStatut statut) {
        this.idPaiement = UUID.randomUUID().toString();
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement != null ? datePaiement : Optional.empty();
        this.typePaiement = typePaiement;
        this.statut = statut;
    }

    // Getters / Setters
    public String getIdPaiement() { return idPaiement; }
    public String getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(String idAbonnement) { this.idAbonnement = idAbonnement; }

    public LocalDate getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }

    public Optional<LocalDate> getDatePaiement() { return datePaiement; }
    public void setDatePaiement(Optional<LocalDate> datePaiement) {
        this.datePaiement = datePaiement != null ? datePaiement : Optional.empty();
    }

    public PaiementType getTypePaiement() { return typePaiement; }
    public void setTypePaiement(PaiementType typePaiement) { this.typePaiement = typePaiement; }

    public PaiementStatut getStatut() { return statut; }
    public void setStatut(PaiementStatut statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement='" + idPaiement + '\'' +
                ", idAbonnement='" + idAbonnement + '\'' +
                ", dateEcheance=" + dateEcheance +
                ", datePaiement=" + datePaiement.orElse(null) +
                ", typePaiement=" + typePaiement +
                ", statut=" + statut +
                '}';
    }
}
