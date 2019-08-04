CREATE SCHEMA 'prog_web';
CREATE USER 'user_prog_web'@'localhost' IDENTIFIED BY 'sampirisi';
GRANT INSERT,UPDATE,DELETE,SELECT  ON prog_web.* TO 'user_prog_web'@'localhost';

create table ticket(
    id int not null AUTO_INCREMENT,
    costo float not null,
    tipo char not null,
    time timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id)
)engine=InnoDB;

create table province(
    id int not null AUTO_INCREMENT,
    nome varchar(50) not null,
    PRIMARY KEY(id)
)engine=InnoDB;

create table comuni(
    id int not null AUTO_INCREMENT,
    nome varchar(50) not null,
    id_provincia int not null,
    PRIMARY KEY(id),
    FOREIGN KEY fk_comuni_to_province(id_provincia) REFERENCES province(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)engine=InnoDB;

create table utenti(
    id int not null AUTO_INCREMENT,
    nome varchar(100) not null,
    cognome varchar(100) not null,
    data_nascita date not null,
    username varchar(255) not null,
    password varchar(255) not null,
    cf varchar(20) not null,
    ruolo char(10) not null DEFAULT 'paziente' COMMENT 'paziente | medico | medico_spec | ssp',
    id_medico int DEFAULT NULL,
    provincia int not null,
    comune int not null,
    paziente_attivo boolean DEFAULT TRUE COMMENT 'Per indicare account paziente bloccato o meno',
    medico_attivo boolean DEFAULT FALSE COMMENT 'Per indicare medico in attività oppure in pensione',
    specialita text DEFAULT NULL,
    laurea text DEFAULT NULL,
    inizio_carriera date DEFAULT NULL,
    PRIMARY KEY(id),
    UNIQUE(username),
    UNIQUE(cf),
    FOREIGN KEY fk_utenti_to_comuni(comune) REFERENCES comuni(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_utenti_to_province(provincia) REFERENCES province(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_pazietne_to_medico(id_medico) REFERENCES utenti(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
    /*CHECK( id <> id_medico ),
    CHECK( NOT EXISTS (SELECT u.id FROM utenti u WHERE id_medico = u.id AND u.ruolo <> 'medico') ),*/
)engine=InnoDB;


create table foto(
    id int not null,
    id_utente int not null,
    path text not null,
    time timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id_utente,id),
    FOREIGN KEY fk_foto_to_utenti(id_utente) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;

create table cambio_password(
    id_utente int not null,
    hash text not null,
    time timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id_utente),
    FOREIGN KEY fk_cambio_password_to_utenti(id_utente) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;


create table prescrizione(
    id int not null AUTO_INCREMENT,
    id_paziente int not null,
    id_medico int not null,
    time timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id),
    FOREIGN KEY fk_prescrizione_to_paziente(id_paziente) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_prescrizione_to_medico(id_medico) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;


create table farmaci(
    id int not null AUTO_INCREMENT,
    nome varchar(255) not null,
    costo float not null,
    PRIMARY KEY(id)
)engine=InnoDB;



create table farmaco(
    id_prescrizione int not null,
    id_farmaco int not null,
    costo float not null,
    quantita smallint not null,
    time_vendita timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id_prescrizione),
    FOREIGN KEY fk_farmaco_to_prescrizione(id_prescrizione) REFERENCES prescrizione(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_farmaco_to_farmaci(id_farmaco) REFERENCES prescrizione(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;



create table esami_prescrivibili(
    id int not null AUTO_INCREMENT,
    nome varchar(255) not null,
    PRIMARY KEY(id)
)engine=InnoDB;

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



create table esame(
    id_prescrizione int not null,
    id_esame int not null,
    id_ticket int DEFAULT NULL,
    id_ssp int DEFAULT NULL COMMENT 'NULL se non ancora fatto',
    risultato text DEFAULT NULL,
    time_esame timestamp DEFAULT NULL,
    PRIMARY KEY(id_prescrizione),
    FOREIGN KEY fk_esame_to_prescrizione(id_prescrizione) REFERENCES prescrizione(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_esami_prescrivibili(id_esame) REFERENCES esami_prescrivibili(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_ticket(id_ticket) REFERENCES ticket(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_ssp(id_ssp) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;


create table visita(
    id_prescrizione int not null,
    id_ticket int not null,
    anamnesi text not null,
    time_visita timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id_prescrizione),
    FOREIGN KEY fk_visita_specialistica_to_prescrizione(id_prescrizione) REFERENCES prescrizione(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_ticket(id_ticket) REFERENCES ticket(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;

create table visite_specialistiche(
    id int not null,
    nome int not null,
    PRIMARY KEY(id)
)engine=InnoDB;



create table visita_specialistica(
    id_prescrizione int not null,
    id_medico_specialista int not null,
    id_ticket int not null,
    id_visita_spec int not null,
    anamnesi text not null,
    time_visita timestamp not null DEFAULT NOW(),
    PRIMARY KEY(id_prescrizione),
    FOREIGN KEY fk_visita_specialistica_to_prescrizione(id_prescrizione) REFERENCES prescrizione(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_visita_specialistica_to_utenti(id_medico_specialista) REFERENCES utenti(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_esame_to_ticket(id_ticket) REFERENCES ticket(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    FOREIGN KEY fk_visita_specialistica_to_utenti_to_visite(id_visita_spec) REFERENCES visite_specialistiche(id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)engine=InnoDB;








/*
Query per ricerce di testo approssimate (necessaria la funzione di Levenshtein):
Probabilmente useremo ricerche FullText.

SELECT nome
from test
WHERE levenshtein( soundex('steveq') ,soundex(nome))  <= 2


SELECT nome, levenshtein( soundex('steveq') ,soundex(nome)) 
from test
*/


/*
DATI DI TEST
INSERT INTO `prog_web`.`utenti`
(`id`,
`nome`,
`cognome`,
`data_nascita`,
`username`,
`password`,
`cf`,
`ruolo`,
`id_medico`,
`provincia`,
`comune`,
`paziente_attivo`)
VALUES
(1,
'Steve',
'Azzolin',
'1998-06-23',
'steve.azzolin1@gmail.com',
'1000:5b4240333032306164:f38d165fce8ce42f59d366139ef5d9e1ca1247f0e06e503ee1a611dd9ec40876bb5edb8409f5abe5504aab6628e70cfb3d3a18e99d70357d295002c3d0a308a0',
'ABCDEFGHIL',
'paziente',
NULL,
15,
15,
1);
*/