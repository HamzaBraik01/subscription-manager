package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement {
    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, BigDecimal montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin,
                                    Statut statut, int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
        if (dureeEngagementMois <= 0) {
            throw new IllegalArgumentException("Durée engagement doit être > 0");
        }
        this.dureeEngagementMois = dureeEngagementMois;
    }

    public int getDureeEngagementMois() { return dureeEngagementMois; }
    public void setDureeEngagementMois(int dureeEngagementMois) {
        if (dureeEngagementMois <= 0) {
            throw new IllegalArgumentException("Durée engagement doit être > 0");
        }
        this.dureeEngagementMois = dureeEngagementMois;
    }

    @Override
    public String toString() {
        return super.toString() + ", dureeEngagementMois=" + dureeEngagementMois;
    }

    @Override
    public boolean isWithEngagement() {
        return true;
    }
}
