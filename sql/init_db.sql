
DROP TABLE IF EXISTS paiement CASCADE;
DROP TABLE IF EXISTS abonnement CASCADE;

CREATE TABLE abonnement (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- PostgreSQL UUID
    nom_service VARCHAR(100) NOT NULL,
    montant_mensuel NUMERIC(10,2) NOT NULL CHECK (montant_mensuel >= 0),
    date_debut DATE NOT NULL,
    date_fin DATE,
    statut VARCHAR(20) NOT NULL CHECK (statut IN ('Active', 'Suspendu', 'Resilie')),
    type_abonnement VARCHAR(30) NOT NULL CHECK (type_abonnement IN ('AvecEngagement', 'SansEngagement')),
    duree_engagement_mois INT,

    CONSTRAINT chk_duree_engagement CHECK (
        (type_abonnement = 'AvecEngagement' AND duree_engagement_mois IS NOT NULL)
        OR (type_abonnement = 'SansEngagement' AND duree_engagement_mois IS NULL)
    )
);

CREATE TABLE paiement (
    id_paiement UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_abonnement UUID NOT NULL,
    date_echeance DATE NOT NULL,
    date_paiement DATE,
    type_paiement VARCHAR(50) NOT NULL,
    statut VARCHAR(20) NOT NULL CHECK (statut IN ('Paye', 'NonPaye', 'EnRetard')),

    -- Contraintes relationnelles
    CONSTRAINT fk_paiement_abonnement FOREIGN KEY (id_abonnement)
        REFERENCES abonnement(id)
        ON DELETE CASCADE
);

-
CREATE INDEX idx_paiement_date_echeance ON paiement(date_echeance);
CREATE INDEX idx_paiement_statut ON paiement(statut);
