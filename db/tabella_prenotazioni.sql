create table prenotazione(
    id_paziente int not null,
    id_medico int not null,
    data timestamp not null,
    PRIMARY KEY(id_paziente, id_medico, data_visita),
    FOREIGN KEY fk_prescrizione_to_paziente(id_paziente) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_prescrizione_to_medico(id_medico) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;

INSERT INTO `prog_web`.`prenotazione`
(`id_paziente`,
`id_medico`,
`data`) 
VALUES 
(1,2,'2019-08-17 15:00:00'),
(3,2,'2019-08-18 13:00:00'),
(3,2,'2019-08-19 11:00:00'),
(1,2,'2019-08-20 10:00:00'),
(1,2,'2019-08-21 15:00:00'),
(3,2,'2019-08-22 12:00:00'),
(1,2,'2019-08-23 08:00:00'),
(3,2,'2019-08-24 14:00:00'),
(1,2,'2019-08-25 09:00:00'),
(1,2,'2019-08-25 11:00:00'),
(3,2,'2019-08-26 09:00:00'),
(3,2,'2019-08-27 08:00:00'),
(1,2,'2019-08-28 12:00:00'),
(3,2,'2019-08-29 11:00:00'),
(1,2,'2019-08-28 08:00:00'),
(3,2,'2019-08-31 09:00:00'),
(1,2,'2019-09-01 10:00:00'),
(1,2,'2019-09-01 13:00:00'),
(3,2,'2019-09-02 17:00:00'),
(3,2,'2019-09-03 08:00:00'),
(1,2,'2019-08-03 09:00:00'),
(3,2,'2019-08-04 15:00:00');