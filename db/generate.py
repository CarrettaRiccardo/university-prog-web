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

PSWD = '1000:e1cbefc13d8c5931dafe6f1c92af1abe:d894e4657999033774b7432696e409d3fc26e46622ecbd4739070896561aa76dc6071f640b36fa0c417d308cc1cee62b38623beaf837a6fdee52c1085a830e6d'
SPECIALITA = ['Radiologia', 'Medicina interna', 'Ematologia', 'Neurologia', 'Psichiatria', 'Pediatria']
UNI = ['Unitn', 'Unipd', 'Unito', 'Unimi', 'Unirm']
UTENTI_SQL = "insert into utenti(id, nome, cognome, data_nascita, username, password, sesso, cf, ruolo, id_medico, provincia, comune, paziente_attivo, medico_attivo, specialita, laurea, inizio_carriera) values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
N_MEDICI_SPEC = 10
N_MEDICI = 20
N_PAZIENTI = 200
N_RICETTE = 1000
N_ESAMI = 1000
N_VISITE = 1000
N_VISITE_SPEC = 1000
N_PRESCRIZIONI = N_RICETTE + N_ESAMI + N_VISITE + N_VISITE_SPEC


# Utenti
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
id_ssp = id_ssp_start = 0

for x in res:
    id_prov, nome = x
    id_ssp += 1
    vals.append((id_ssp, 'Provincia di ' + nome, nome.lower() + '@gov.it', PSWD, 'ssp', id_prov))

c.executemany(
    "insert into utenti (id, nome, username, password, ruolo, provincia) values (%s, %s, %s, %s, %s, %s)", vals)
id_ssp_end = id_ssp_start + len(vals)

############# Medici specialisti
id_medici_spec_start = id_ssp_end + 1
vals = [(id_medici_spec_start, 'Matteo', 'Destro', '1965-12-12', 'matteo.est@gmail.com', PSWD, 'm', 'DSTMTT65RK154L',
         'medico_spec', id_medici_spec_start + N_MEDICI_SPEC + 1, 22, 2840, 1, 0, 'Radiologia', 'Radiologia, Unitn',
         '1989-01-21')]

for i in range(id_medici_spec_start + 1, id_medici_spec_start + N_MEDICI_SPEC):
    vals.append(gen_utente(i, fake.pybool(), 'medico_spec', i + N_MEDICI_SPEC, True, True))

c.executemany(UTENTI_SQL, vals)
id_medici_spec_end = id_medici_spec_start + len(vals) - 1

############# Medici
id_medici_start = id_medici_spec_end + 1
vals = [(id_medici_start, 'Riccardo', 'Carretta', '1998-06-23', 'riccardo.carretta@gmail.com', PSWD,
         'm', 'CRTRCR99SK121M', 'medico', id_medici_start + 1, 22, 2840, 1, 1, None, 'Medicina, Unitn', '2000-05-13')]

for i in range(id_medici_start + 1, id_medici_start + N_MEDICI):
    vals.append(gen_utente(i, fake.pybool(), 'medico', i - 1, False, True))

c.executemany(UTENTI_SQL, vals)
id_medici_end = id_medici_start + len(vals) - 1

############# Pazienti
id_pazienti_start = id_medici_end + 1
vals = [(id_pazienti_start, 'Steve', 'Azzolin', '1998-06-23', 'steve.azzolin1@gmail.com', PSWD,
         'm', 'AZZSTV98WO121E', 'paziente', id_medici_start, 22, 2941, 1, 1, None, None, None)]

for i in range(id_pazienti_start + 1, id_pazienti_start + N_PAZIENTI):
    vals.append(gen_utente(i, fake.pybool(), 'paziente', random.randint(id_medici_start, id_medici_end), False, False))

c.executemany(UTENTI_SQL, vals)
id_pazienti_end = id_pazienti_start + len(vals) - 1

################ Competenze medici specialisti
vals = []
for id in range(id_medici_spec_start, id_medici_end + 1):
    for j in range(0, random.randint(5, 10)):
        vals.append((id, random.randint(1, 134)))
c.execute("truncate table competenze_medico_spec")
c.executemany("insert ignore into competenze_medico_spec values (%s, %s)", vals)  # ignoro eventuali duplicati

############### Tickets TODO: stesso paziente per prestazione e ticket
vals = []
for id in range(1, N_ESAMI + 1):
    vals.append((id, 11, 'e', fake.date_time_between(start_date="-1y").strftime("%Y-%m-%d"),
                 random.randint(id_pazienti_start, id_pazienti_end)))
for id in range(N_ESAMI + 1, N_ESAMI + N_VISITE_SPEC + 1):
    vals.append((id, 50, 'v', fake.date_time_between(start_date="-1y").strftime("%Y-%m-%d"),
                 random.randint(id_pazienti_start, id_pazienti_end)))
c.execute("truncate table ticket")
c.executemany("insert ignore into ticket values (%s, %s,%s, %s, %s)", vals)

############### Prescrizioni
vals = []
for id in range(1, N_PRESCRIZIONI + 1):
    vals.append((id, random.randint(id_pazienti_start, id_pazienti_end), random.randint(id_medici_start, id_medici_end),
                 fake.date_time_between(start_date="-1y").strftime("%Y-%m-%d")))
c.execute("truncate table prescrizione")
c.executemany("insert into prescrizione values (%s, %s, %s, %s)", vals)

############### Ricette
vals = []
for id in range(1, N_RICETTE + 1):
    vals.append((id, random.randint(1, 7860), random.randint(100, 1000) / 10, random.randint(1, 10),
                 fake.date_time_between(start_date="-1y").strftime("%Y-%m-%d")))
c.execute("truncate table farmaco")
c.executemany("insert into farmaco values (%s, %s, %s, %s, %s)", vals)

############### Esami TODO: FK id_ssp come provincia paziente + risultato esame
vals = []
for id in range(N_RICETTE + 1, N_RICETTE + N_ESAMI + 1):
    vals.append((id, random.randint(1, 158), random.randint(1, N_ESAMI + 1), 22,
                 "Risultato esame ", fake.date_time_between(start_date="-1y").strftime("%Y-%m-%d")))
c.execute("truncate table esame")
c.executemany("insert into esame values (%s, %s, %s, %s, %s, %s)", vals)

############### Visite TODO: risultato visita
vals = []
for id in range(N_RICETTE + N_ESAMI + 1, N_RICETTE + N_ESAMI + N_VISITE + 1):
    vals.append((id, "Risultato visita "))
c.execute("truncate table visita")
c.executemany("insert into visita values (%s, %s)", vals)

############### Visite specialistiche TODO: risultato visita_specialistica + cura
vals = []
for id in range(N_RICETTE + N_ESAMI + N_VISITE + 1, N_RICETTE + N_ESAMI + N_VISITE + N_VISITE_SPEC + 1):
    vals.append((id, random.randint(id_medici_spec_start, id_medici_spec_end),
                 random.randint(N_ESAMI + 1, N_ESAMI + N_VISITE_SPEC + 1), random.randint(1, 134),
                 "Risultato visita specialistica", "Cura",
                 fake.date_time_between(start_date="-1y").strftime("%Y-%m-%d")))
c.execute("truncate table visita_specialistica")
c.executemany("insert ignore into visita_specialistica values (%s, %s, %s, %s, %s, %s, %s)", vals)

################ Commit query
c.execute("SET foreign_key_checks = 1")
db.commit()
