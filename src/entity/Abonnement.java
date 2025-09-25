package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Abonnement {
    private String id;
    private String nomService;
    private BigDecimal montantMensuel;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Statut statut;

    public enum Statut {
        ACTIVE, SUSPENDU, RESILIE
    }

    // Constructeur
    public Abonnement(String nomService, BigDecimal montantMensuel,
                      LocalDate dateDebut, LocalDate dateFin, Statut statut) {
        this.id = UUID.randomUUID().toString();
        this.nomService = nomService;
        setMontantMensuel(montantMensuel);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        this.statut = statut;
    }

    // Getters/Setters avec validation minimale
    public String getId() { return id; }

    public String getNomService() { return nomService; }
    public void setNomService(String nomService) { this.nomService = nomService; }

    public BigDecimal getMontantMensuel() { return montantMensuel; }
    public void setMontantMensuel(BigDecimal montantMensuel) {
        if (montantMensuel.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être > 0");
        }
        this.montantMensuel = montantMensuel;
    }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        if (dateFin != null && dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }
    }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        if (dateDebut != null && dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("dateFin doit être après dateDebut");
        }
    }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id='" + id + '\'' +
                ", nomService='" + nomService + '\'' +
                ", montantMensuel=" + montantMensuel +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut=" + statut +
                '}';
    }
}
