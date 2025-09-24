-- Nettoyage préalable
DROP DATABASE IF EXISTS subscription_manager;
CREATE DATABASE subscription_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE subscription_manager;

-- =====================
-- Table : Abonnement
-- =====================
CREATE TABLE abonnement (
    id CHAR(36) PRIMARY KEY, -- UUID (ex: 8-4-4-4-12 format)
    nomService VARCHAR(100) NOT NULL,
    montantMensuel DECIMAL(10,2) NOT NULL CHECK (montantMensuel >= 0),
    dateDebut DATE NOT NULL,
    dateFin DATE,
    statut ENUM('Active', 'Suspendu', 'Resilie') NOT NULL DEFAULT 'Active',
    typeAbonnement ENUM('AvecEngagement', 'SansEngagement') NOT NULL,
    dureeEngagementMois INT DEFAULT NULL,
    
    CONSTRAINT chk_duree_engagement CHECK (
        (typeAbonnement = 'AvecEngagement' AND dureeEngagementMois IS NOT NULL)
        OR (typeAbonnement = 'SansEngagement' AND dureeEngagementMois IS NULL)
    )
);

-- =====================
-- Table : Paiement
-- =====================
CREATE TABLE paiement (
    idPaiement CHAR(36) PRIMARY KEY, -- UUID
    idAbonnement CHAR(36) NOT NULL,
    dateEcheance DATE NOT NULL,
    datePaiement DATE,
    typePaiement VARCHAR(50) NOT NULL,
    statut ENUM('Paye', 'NonPaye', 'EnRetard') NOT NULL,

    -- Contraintes relationnelles
    CONSTRAINT fk_paiement_abonnement FOREIGN KEY (idAbonnement)
        REFERENCES abonnement(id)
        ON DELETE CASCADE
);
