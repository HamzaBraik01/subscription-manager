
USE subscription_manager;

INSERT INTO abonnement (id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois)
VALUES
(UUID(), 'Netflix', 12.99, '2025-01-01', NULL, 'Active', 'AvecEngagement', 12),
(UUID(), 'Spotify', 9.99, '2025-02-01', NULL, 'Active', 'SansEngagement', NULL),
(UUID(), 'Adobe Creative Cloud', 29.99, '2025-03-01', NULL, 'Suspendu', 'AvecEngagement', 6);

-- --------------------------
-- Récupérer les IDs pour les paiements

SET @netflixId = (SELECT id FROM abonnement WHERE nomService='Netflix');
SET @spotifyId = (SELECT id FROM abonnement WHERE nomService='Spotify');
SET @adobeId = (SELECT id FROM abonnement WHERE nomService='Adobe Creative Cloud');

-- --------------------------

INSERT INTO paiement (idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut)
VALUES
(UUID(), @netflixId, '2025-01-05', '2025-01-05', 'Carte Bancaire', 'Paye'),
(UUID(), @netflixId, '2025-02-05', '2025-02-06', 'Carte Bancaire', 'EnRetard'),
(UUID(), @netflixId, '2025-03-05', NULL, 'Carte Bancaire', 'NonPaye'),
(UUID(), @netflixId, '2025-04-05', '2025-04-05', 'Carte Bancaire', 'Paye'),

(UUID(), @spotifyId, '2025-02-10', '2025-02-10', 'Paypal', 'Paye'),
(UUID(), @spotifyId, '2025-03-10', NULL, 'Paypal', 'NonPaye'),
(UUID(), @spotifyId, '2025-04-10', NULL, 'Paypal', 'NonPaye'),
(UUID(), @spotifyId, '2025-05-10', '2025-05-12', 'Paypal', 'EnRetard'),

(UUID(), @adobeId, '2025-03-15', '2025-03-15', 'Virement', 'Paye'),
(UUID(), @adobeId, '2025-04-15', NULL, 'Virement', 'NonPaye'),
(UUID(), @adobeId, '2025-05-15', NULL, 'Virement', 'NonPaye'),
(UUID(), @adobeId, '2025-06-15', '2025-06-20', 'Virement', 'EnRetard');
