import random
import string

import mysql.connector
from codicefiscale import build as cf
from faker import Faker
from faker.providers import date_time

fake = Faker('it_IT')
fake.add_provider(date_time)
fake.seed(3482)

db = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="",
    database="prog_web"
)
c = db.cursor()
c.execute("SET foreign_key_checks = 0")

# Utenti
PSWD = '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d'
SPECIALITA = ['Radiologia', 'Medicina interna', 'Ematologia', 'Neurologia', 'Psichiatria', 'Pediatria']
UNI = ['Unitn', 'Unipd', 'Unito', 'Unimi', 'Unirm']
UTENTI_SQL = "insert into utenti(id, nome, cognome, data_nascita, username, password, sesso, cf, ruolo, id_medico, provincia, comune, paziente_attivo, medico_attivo, specialita, laurea, inizio_carriera) values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
N_MEDICI_SPEC = 10
N_MEDICI = 20
N_PAZIENTI = 200


def gen_prov_com():
    prov = random.choice([22, 24])  # 22 = Trento, 24 = Vicenza
    return (prov, 3155 if prov == 24 else
    random.choice([2840, 2941]))  # 3155 = Recoaro (VI), 2840 = Civezzano (TN), 2941 = Trento (TN)


def gen_utente(id, male, ruolo, id_medico, has_specialita, has_laurea):
    nome = fake.first_name_male() if male else fake.first_name_female()
    cognome = fake.last_name_male() if male else fake.last_name_female()
    data_nascita = fake.date_time_between(start_date="-45y")
    username = nome.lower() + "." + cognome.lower() + "@gmail.com"
    CF = cf(nome, cognome, data_nascita, 'M' if male else 'F',
            random.choice(string.ascii_letters).upper() + str(random.randint(100, 999)))
    id_prov, id_com = gen_prov_com()
    specialita = None if not has_specialita else random.choice(SPECIALITA)
    laurea = specialita + ', ' + random.choice(UNI) if specialita is not None else \
        'Medicina, ' + random.choice(UNI) if has_laurea else None
    data_laurea = fake.date_time_between(start_date="-10y") if has_laurea else None
    return (
        id, nome, cognome, data_nascita.strftime("%Y-%m-%d"), username, PSWD, 'm' if male else 'f', CF, ruolo,
        id_medico, id_prov, id_com, 1, 1, specialita, laurea,
        None if data_laurea is None else data_laurea.strftime("%Y-%m-%d"))


c.execute("truncate table utenti")

############# SSP, 1 per provincia
c.execute("select id, nome from province")
res = c.fetchall()
vals = []
id = id_ssp_start = 0

for x in res:
    id_prov, nome = x
    id += 1
    vals.append((id, 'Provincia di ' + nome, nome.lower() + '@gov.it', PSWD, 'ssp', id_prov))

c.executemany(
    "insert into utenti (id, nome, username, password, ruolo, provincia) values (%s, %s, %s, %s, %s, %s)", vals)
id_ssp_end = id_ssp_start + len(vals)

############# Medici specialisti
id_medici_spec_start = id_ssp_end + 1
vals = [(id_medici_spec_start, 'Matteo', 'Destro', '1965-12-12', 'matteo.est@gmail.com', PSWD, 'm', 'DSTMTT65RK154L',
         'medico_spec', None, 22, 2840, 1, 0, 'Radiologia', 'Radiologia, Unitn', '1989-01-21')]

for i in range(id_medici_spec_start + 1, id_medici_spec_start + N_MEDICI_SPEC):
    vals.append(gen_utente(i, fake.pybool(), 'medico_spec', i + N_MEDICI_SPEC, True, True))

c.executemany(UTENTI_SQL, vals)
id_medici_spec_end = id_medici_spec_start + len(vals)

############# Medici
# TODO id medico
id_medici_start = id_medici_spec_end + 1
vals = [(id_medici_start, 'Riccardo', 'Carretta', '1998-06-23', 'riccardo.carretta@gmail.com', PSWD,
         'm', 'CRTRCR99SK121M', 'medico', id_medici_start + 1, 22, 2840, 1, 1, None, 'Medicina, Unitn', '2000-05-13')]

for i in range(id_medici_start + 1, id_medici_start + N_MEDICI):
    vals.append(gen_utente(i, fake.pybool(), 'medico', i - 1, False, True))

c.executemany(UTENTI_SQL, vals)
id_medici_end = id_medici_start + len(vals)

############# Pazienti
# TODO id medico
id_pazienti_start = id_medici_end + 1
vals = [(id_pazienti_start, 'Steve', 'Azzolin', '1998-06-23', 'steve.azzolin1@gmail.com', PSWD,
         'm', 'AZZSTV98WO121E', 'paziente', None, 22, 2941, 1, 1, None, None, None)]

for i in range(id_pazienti_start + 1, id_pazienti_start + N_PAZIENTI):
    vals.append(gen_utente(i, fake.pybool(), 'paziente', random.randint(id_medici_start, id_medici_end), False, False))

c.executemany(UTENTI_SQL, vals)
id_pazienti_end = id_pazienti_start + len(vals)

c.execute("SET foreign_key_checks = 1")
db.commit()
