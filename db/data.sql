/**
  Dati di esempio
**/

/* Disabilitazione FK per TRUNCATE TABLE, riabilitate alla fine */
set foreign_key_checks = 0;

/************************************** UTENTI **************************************/
TRUNCATE table utenti;
/* SSP */
INSERT INTO utenti
VALUES (1, 'Provincia autonoma di Trento', NULL, NULL, 'trento@gov.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        NULL, NULL, 'ssp', NULL, 22, NULL, NULL, NULL, NULL, NULL, NULL),
       (2, 'Provincia di Vicenza', NULL, NULL, 'vicenza@gov.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        NULL, NULL, 'ssp', NULL, 24, NULL, NULL, NULL, NULL, NULL, NULL),
       (3, 'Provincia autonoma di Bolzano', NULL, NULL, 'bolzano@gov.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        NULL, NULL, 'ssp', NULL, 21, NULL, NULL, NULL, NULL, NULL, NULL),
       (4, 'Provincia di Padova', NULL, NULL, 'padova@gov.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        NULL, NULL, 'ssp', NULL, 28, NULL, NULL, NULL, NULL, NULL, NULL);

/* Medici specialisti */
INSERT INTO utenti
VALUES (5, 'Matteo', 'Destro', '1965-12-12', 'matteo.est@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'm', 'DSTMTT65RK154L', 'medico_spec', NULL, 22, 2840, 1, 0, 'Radiologo', 'Medicina interna, Unitn',
        '1989-01-21'),
       (6, 'Giovanni', 'Rossi', '1980-01-30', 'giovanni.rossi@sanitymanager.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'm', 'RSSGVN84JK251U', 'medico_spec', NULL, 24, 3155, 1, 0, 'Radiologo', 'Medicina interna, Unipd',
        '1999-04-23'),
       (7, 'Francesco', 'Verdi', '1970-05-21', 'francesco.verdi@sanitymanager.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'm', 'VRDFRC70DO119K', 'medico_spec', NULL, 21, 2778, 1, 0, 'Radiologo', 'Medicina interna, Unito',
        '1981-06-12');

TRUNCATE table competenze_medico_spec;
INSERT INTO competenze_medico_spec /* id da 1 a 133 per visite_spec */
VALUES (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5),
       (5, 6),
       (5, 7),
       (5, 8),
       (5, 9),
       (5, 10),
       (6, 11),
       (6, 12),
       (6, 13),
       (6, 14),
       (6, 15),
       (6, 16),
       (6, 17),
       (6, 18),
       (6, 19),
       (6, 20),
       (7, 21),
       (7, 22),
       (7, 23),
       (7, 24),
       (7, 25),
       (7, 26),
       (7, 27),
       (7, 28),
       (7, 29),
       (7, 30);

/* Medici */
INSERT INTO utenti
VALUES (8, 'Riccardo', 'Carretta', '1998-06-23', 'riccardo.carretta@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'm', 'CRTRCR99SK121M', 'medico', 9, 22, 15, 1, 1, NULL, 'Medicina, Unitn', '2000-05-13'),
       (9, 'Giuseppe', 'Bartolini', '1978-06-23', 'giusepe.bartolini@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'm', 'BRTGSP78SE127M', 'medico', 8, 22, 15, 1, 1, NULL, 'Medicina, Unipd', '1994-05-26');

/* Pazienti */
INSERT INTO utenti
VALUES (10, 'Steve', 'Azzolin', '1998-06-23', 'steve.azzolin1@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'm', 'ABCDEFGHIL', 'paziente', 8, 22, 15, 1, 1, NULL, 'Medicina, UNIRM', '2007-07-01');

/*************** TICKETS **************************/
TRUNCATE table ticket;
INSERT INTO ticket
VALUES (1, 50, 'e', '2019-08-25 20:09:27', 1);

/*************** PRESCRIZIONI ********************/
TRUNCATE table prescrizione;
INSERT INTO prescrizione
VALUES (1, 1, 3, '2019-08-19 20:05:27'),
       (2, 1, 3, '2019-08-19 20:08:34'),
       (3, 1, 3, '2019-08-19 20:09:11'),
       (4, 1, 3, '2019-08-19 20:09:42'),
       (5, 1, 3, '2019-08-19 20:25:34'),
       (6, 1, 3, '2019-08-19 20:29:12'),
       (7, 1, 2, '2019-08-19 20:30:38'),
       (8, 1, 2, '2019-08-19 20:32:37'),
       (9, 1, 2, '2019-08-19 20:34:15'),
       (10, 1, 2, '2019-08-19 20:40:47'),
       (11, 1, 3, '2019-08-24 20:28:04'),
       (12, 1, 3, '2019-09-03 20:28:04'),
       (13, 1, 3, '2019-09-03 20:28:04');

/*************** VISITE SPECIALISTICHE ***********************/
TRUNCATE table visita_specialistica;
INSERT INTO visita_specialistica
VALUES (3, 2, 1, 1, 'Allora mi è ò ù sembrato di capire che ', 'Mangiare sano', '2019-08-20 22:00:00'),
       (1, 2, 1, 1,
        'Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...',
        'Si', '2019-08-25 20:09:27');

/****************** PRENOTAZIONI *************************/
TRUNCATE table prenotazione;
INSERT INTO prenotazione
(id_paziente,
 id_medico,
 time)
VALUES (1, 2, '2019-08-17 15:00:00'),
       (3, 2, '2019-08-18 13:00:00'),
       (3, 2, '2019-08-19 11:00:00'),
       (1, 2, '2019-08-20 10:00:00'),
       (1, 2, '2019-08-21 15:20:00'),
       (3, 2, '2019-08-22 12:30:00'),
       (1, 2, '2019-08-23 08:40:00'),
       (3, 2, '2019-08-24 14:50:00'),
       (1, 2, '2019-08-25 09:00:00'),
       (1, 2, '2019-08-25 11:10:00'),
       (3, 2, '2019-08-26 09:20:00'),
       (3, 2, '2019-08-27 08:30:00'),
       (1, 2, '2019-08-28 12:40:00'),
       (3, 2, '2019-08-29 11:50:00'),
       (1, 2, '2019-08-28 08:00:00'),
       (3, 2, '2019-08-31 09:10:00'),
       (1, 2, '2019-09-01 10:20:00'),
       (1, 2, '2019-09-01 13:30:00'),
       (3, 2, '2019-09-02 17:40:00'),
       (3, 2, '2019-09-03 08:50:00'),
       (1, 2, '2019-08-03 09:00:00'),
       (3, 2, '2019-08-04 15:10:00');

/*************** RICETTE (farmaco) ********************/
TRUNCATE table farmaco;
INSERT INTO farmaco
VALUES (12, 2, 12.9, 2, '2019-08-04 15:10:00');

/*************** ESAMI ********************/
TRUNCATE table esame;
INSERT INTO esame
VALUES (13, 1, 1, 4, 'Referto esamo', '2019-08-04 22:15:00');

/* Riabilitazione FK */
set foreign_key_checks = 1;