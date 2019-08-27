/**
  Dati di esempio
**/

/************** UTENTI ***************/
INSERT INTO utenti
VALUES (2, 'Matteo', 'Destro', '1965-12-12', 'matteo.est@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'SONOILCFDIMATTEO', 'medico_spec', NULL, 20, 20, 1, 0, NULL, 'Medicina generale, UNIPD', '1989-01-01'),
       (3, 'Riccardo', 'Carretta', '1998-06-23', 'riccardo.carretta@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'ABCDE2FGHIL', 'medico', 2, 15, 15, 1, 1, NULL, NULL, NULL),
       (1, 'Steve', 'Azzolin', '1998-06-23', 'steve.azzolin1@gmail.com',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        'ABCDEFGHIL', 'paziente', 3, 15, 15, 1, 1, NULL, NULL, NULL),
       (4, 'Provincia autonoma di Trento', NULL, NULL, 'trento@gov.it',
        '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d',
        NULL, 'ssp', NULL, 22, NULL, NULL, NULL, NULL, NULL, NULL);

/*************** TICKETS **************************/
INSERT INTO ticket
VALUES (1, 50, '', '2019-08-25 20:09:27');

/*************** PRESCRIZIONI ********************/
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
       (11, 1, 3, '2019-08-24 20:28:04');

/*************** VISITE SPECIALISTICHE ***********************/
INSERT INTO visita_specialistica
VALUES (3, 2, 1, 1, 'Allora mi è sembrato di capire che ', 'Mangiare sano', '2019-08-20 22:00:00'),
       (1, 2, 1, 1,
        'Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...Il paziente presenta ...',
        'Si', '2019-08-25 20:09:27');

/****************** PRENOTAZIONI *************************/
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