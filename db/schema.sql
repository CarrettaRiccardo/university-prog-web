/* Creazione utente */
CREATE USER IF NOT EXISTS 'user_prog_web'@'localhost' IDENTIFIED BY 'sampirisi';
GRANT INSERT, UPDATE, DELETE, SELECT ON prog_web.* TO 'user_prog_web'@'localhost';

/* Disabilitazione FK per DROP TABLE, riabilitate alla fine */
set foreign_key_checks = 0;

drop table if exists competenze_medico_spec;
create table competenze_medico_spec
(
    id_medico_spec    int       not null,
    id_visita_spec    int       not null,
    PRIMARY KEY (id_medico_spec,id_visita_spec),
    FOREIGN KEY fk_competenza_to_ms (id_medico_spec) REFERENCES utenti (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_competenza_to_vs (id_visita_spec) REFERENCES visite_specialistiche (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) engine = InnoDB;

drop table if exists ticket;
create table ticket
(
    id    int       not null AUTO_INCREMENT,
    costo float     not null,
    tipo  char      not null,
    time  timestamp not null DEFAULT NOW(),
    PRIMARY KEY (id)
) engine = InnoDB;

drop table if exists province;
create table province
(
    id   int         not null AUTO_INCREMENT,
    nome varchar(50) not null,
    PRIMARY KEY (id)
) engine = InnoDB;

drop table if exists comuni;
create table comuni
(
    id           int         not null AUTO_INCREMENT,
    nome         varchar(50) not null,
    id_provincia int         not null,
    PRIMARY KEY (id),
    FOREIGN KEY fk_comuni_to_province (id_provincia) REFERENCES province (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) engine = InnoDB;

drop table if exists utenti;
create table utenti
(
    id              int          not null AUTO_INCREMENT,
    nome            varchar(100) not null,
    cognome         varchar(100) COMMENT 'NULL solo per SSP',
    data_nascita    date COMMENT 'NULL solo per SSP',
    username        varchar(255) not null,
    password        varchar(255) not null,
    sesso           char(1)   DEFAULT NULL COMMENT 'm|f',
    cf              varchar(20)  COMMENT 'NULL solo per SSP',
    ruolo           char(32)     not null DEFAULT 'paziente' COMMENT 'paziente | medico | medico_spec | ssp',
    id_medico       int                   DEFAULT NULL,
    provincia       int          not null,
    comune          int          COMMENT 'NULL solo per SSP',
    paziente_attivo boolean               DEFAULT TRUE COMMENT 'Per indicare account paziente bloccato o meno',
    medico_attivo   boolean               DEFAULT FALSE COMMENT 'Per indicare medico in attività oppure in pensione',
    specialita      text                  DEFAULT NULL,
    laurea          text                  DEFAULT NULL,
    inizio_carriera date                  DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (username),
    UNIQUE (cf),
    FOREIGN KEY fk_utente_to_comuni (comune) REFERENCES comuni (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_utenti_to_province (provincia) REFERENCES province (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_paziente_to_medico (id_medico) REFERENCES utenti (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
    /*CHECK( id <> id_medico ),
    CHECK( NOT EXISTS (SELECT u.id FROM utenti u WHERE id_medico = u.id AND u.ruolo <> 'medico') ),*/
) engine = InnoDB;

drop table if exists foto;
create table foto
(
    id        int       not null,
    id_utente int       not null,
    path      text      not null,
    time      timestamp not null DEFAULT NOW(),
    PRIMARY KEY (id_utente, id),
    FOREIGN KEY fk_foto_to_utenti (id_utente) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;

drop table if exists cambio_password;
create table cambio_password
(
    id_utente int       not null,
    hash      text      not null,
    time      timestamp not null DEFAULT NOW(),
    PRIMARY KEY (id_utente),
    FOREIGN KEY fk_cambio_password_to_utenti (id_utente) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;

drop table if exists prescrizione;
create table prescrizione
(
    id          int       not null AUTO_INCREMENT,
    id_paziente int       not null,
    id_medico   int       not null,
    time        timestamp not null DEFAULT NOW(),
    PRIMARY KEY (id),
    FOREIGN KEY fk_prescrizione_to_paziente (id_paziente) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_prescrizione_to_medico (id_medico) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;

drop table if exists farmaci;
create table farmaci
(
    id    int          not null AUTO_INCREMENT,
    nome  varchar(255) not null,
    costo float        not null,
    PRIMARY KEY (id),
    INDEX (nome)
) engine = InnoDB;


drop table if exists farmaco;
create table farmaco
(
    id_prescrizione int       not null,
    id_farmaco      int       not null,
    costo           float, /*NULL <-> non ancora comprata*/
    quantita        smallint  not null,
    time_vendita    timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id_prescrizione),
    FOREIGN KEY fk_farmaco_to_prescrizione (id_prescrizione) REFERENCES prescrizione (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_farmaco_to_farmaci (id_farmaco) REFERENCES prescrizione (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;


drop table if exists esami_prescrivibili;
create table esami_prescrivibili
(
    id   int          not null AUTO_INCREMENT,
    nome varchar(255) not null,
    PRIMARY KEY (id)
) engine = InnoDB;

/*
Aggiunto come opzione tra i vari utenti
create table ssp(
    id int not null AUTO_INCREMENT,
    nome varchar(255) not null,
    id_provincia int not null,
    username varchar(255) not null,
    password varchar(255) not null,
    PRIMARY KEY(id),
    FOREIGN KEY fk_ssp_to_province(id_provincia) REFERENCES province(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;*/

drop table if exists esame;
create table esame
(
    id_prescrizione int not null,
    id_esame        int not null,
    id_ticket       int  DEFAULT NULL,
    id_ssp          int  DEFAULT NULL COMMENT 'NULL se non ancora fatto',
    risultato       text DEFAULT NULL,
    time_esame      timestamp,
    PRIMARY KEY (id_prescrizione),
    FOREIGN KEY fk_esame_to_prescrizione (id_prescrizione) REFERENCES prescrizione (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_esami_prescrivibili (id_esame) REFERENCES esami_prescrivibili (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_ticket (id_ticket) REFERENCES ticket (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_ssp (id_ssp) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;


drop table if exists visita;
create table visita
(
    id_prescrizione int  not null,
    anamnesi        text not null,
    PRIMARY KEY (id_prescrizione),
    FOREIGN KEY fk_visita_to_prescrizione (id_prescrizione) REFERENCES prescrizione (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;

drop table if exists visite_specialistiche;
create table visite_specialistiche
(
    id   int  not null,
    nome text not null,
    PRIMARY KEY (id)
) engine = InnoDB;


drop table if exists visita_specialistica;
create table visita_specialistica
(
    id_prescrizione       int       not null,
    id_medico_specialista int            DEFAULT NULL, /*NULL <-> visita non ancora fatta*/
    id_ticket             int            DEFAULT NULL,
    id_visita_spec        int            DEFAULT NULL,
    anamnesi              text           DEFAULT NULL,
    cura                  text           DEFAULT NULL,
    time_visita           timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id_prescrizione),
    UNIQUE (time_visita),
    FOREIGN KEY fk_visita_specialistica_to_prescrizione (id_prescrizione) REFERENCES prescrizione (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_visita_specialistica_to_utenti (id_medico_specialista) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_visita_specialistica_to_ticket (id_ticket) REFERENCES ticket (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_visita_specialistica_to_visite (id_visita_spec) REFERENCES visite_specialistiche (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;


drop table if exists prenotazione;
create table prenotazione
(
    id_paziente int       not null,
    id_medico   int       not null,
    time        timestamp not null,
    PRIMARY KEY (id_paziente, id_medico, time),
    FOREIGN KEY fk_prenotazione_to_paziente (id_paziente) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_prenotazione_to_medico (id_medico) REFERENCES utenti (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) engine = InnoDB;


drop table if exists log_time;
create table log_time
(
    url          text not null,
    time_took    int  not null,
    time_request timestamp DEFAULT NOW()
) engine = InnoDB;

/* Riabilitazione FK */
set foreign_key_checks = 0;