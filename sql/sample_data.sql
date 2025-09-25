CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO abonnement (id, nom_service, montant_mensuel, date_debut, date_fin, statut, type_abonnement, duree_engagement_mois)
VALUES
(gen_random_uuid(), 'Netflix', 12.99, '2025-01-01', NULL, 'Active', 'AvecEngagement', 12),
(gen_random_uuid(), 'Spotify', 9.99, '2025-02-01', NULL, 'Active', 'SansEngagement', NULL),
(gen_random_uuid(), 'Adobe Creative Cloud', 29.99, '2025-03-01', NULL, 'Suspendu', 'AvecEngagement', 6);


-- Netflix
WITH netflix AS (
    SELECT id AS netflix_id FROM abonnement WHERE nom_service = 'Netflix'
),
spotify AS (
    SELECT id AS spotify_id FROM abonnement WHERE nom_service = 'Spotify'
),
adobe AS (
    SELECT id AS adobe_id FROM abonnement WHERE nom_service = 'Adobe Creative Cloud'
)

INSERT INTO paiement (id_paiement, id_abonnement, date_echeance, date_paiement, type_paiement, statut)
SELECT gen_random_uuid(), netflix_id, '2025-01-05', '2025-01-05', 'Carte Bancaire', 'Paye' FROM netflix UNION ALL
SELECT gen_random_uuid(), netflix_id, '2025-02-05', '2025-02-06', 'Carte Bancaire', 'EnRetard' FROM netflix UNION ALL
SELECT gen_random_uuid(), netflix_id, '2025-03-05', NULL, 'Carte Bancaire', 'NonPaye' FROM netflix UNION ALL
SELECT gen_random_uuid(), netflix_id, '2025-04-05', '2025-04-05', 'Carte Bancaire', 'Paye' FROM netflix UNION ALL

SELECT gen_random_uuid(), spotify_id, '2025-02-10', '2025-02-10', 'Paypal', 'Paye' FROM spotify UNION ALL
SELECT gen_random_uuid(), spotify_id, '2025-03-10', NULL, 'Paypal', 'NonPaye' FROM spotify UNION ALL
SELECT gen_random_uuid(), spotify_id, '2025-04-10', NULL, 'Paypal', 'NonPaye' FROM spotify UNION ALL
SELECT gen_random_uuid(), spotify_id, '2025-05-10', '2025-05-12', 'Paypal', 'EnRetard' FROM spotify UNION ALL

SELECT gen_random_uuid(), adobe_id, '2025-03-15', '2025-03-15', 'Virement', 'Paye' FROM adobe UNION ALL
SELECT gen_random_uuid(), adobe_id, '2025-04-15', NULL, 'Virement', 'NonPaye' FROM adobe UNION ALL
SELECT gen_random_uuid(), adobe_id, '2025-05-15', NULL, 'Virement', 'NonPaye' FROM adobe UNION ALL
SELECT gen_random_uuid(), adobe_id, '2025-06-15', '2025-06-20', 'Virement', 'EnRetard' FROM adobe;
