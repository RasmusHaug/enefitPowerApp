# Enefit Assignment

## Kirjeldus

Katse lahendada Enefit koduülesannet tarkvara inseneri positsioonile.

Rakendus back-end kasutab [Java Spring Boot 3.4.1](https://spring.io/) koos [Gradle](https://gradle.org/) ja [PostgreSQL](https://www.postgresql.org/) andmebaasiga.
Rakundese front-end kasutab [Vite](https://vite.dev/) + [React](https://react.dev/) + [Typescript](https://www.typescriptlang.org/) ja [Tailwind CSS](https://tailwindcss.com/) disaini frameworkina.

Applikatsioon lubab kasutajatel registreerida, sisse logida ja näha oma elektri kasutust. Elektri hinna võtab rakendus Eleringi avalikust APIst.
Et säästa Eleringi ning mitte tekitada neile mittetahtlik DOS rünnak salvestab back-end Eleringi andmed oma andmebaasi ning kui võimalik kasutab andmeid sealt üle konstantse Elering API pingimise.

Kasutaja saab endale lisada eluasemeid piiramatult ja igale eluasemele lisada elektri tarbimist.
Hetkel applikatsioon näitab ainult viimase 7 päeva Eluasemete kulu andmeid.
Andmebaasis on kõik varasemad andmed salvestatud.

Applikatsioon krüpteerib kasutaja paroolid andmebaasis ja kontrollib, et kasutaja lisab ainult enda eluasemetele uusi tarbimisi.
Lisaks back-end kasutab looge, et teavitada järgmist informatsiooni serveri administraatorile:
+ Uus kasutaja registreeritud
+ Uus kasutaja ebaõnnestus registreerimisega
+ Kasutaja logib sisse
+ Kasutaja ebaõnnestub sisse logida
+ Kasutaja loeb elektri kulu andmeid
+ Kasutaja proovis lugeda teise inimese elektrikulu andmeid
+ Laadides andmeid Eleringi API-st

**Rakendusega ei ole kaasas ettevalmistatud andmebaasi.**

## Installeerimine
Rakenduse testimiseks ning lookalseks jooksmiseks tuleb installida järgnevad front-end ja back-end rakendused.

### Front-end
Laadides projekt alla, navigeeri konsooliga kloonitud projekti.
Navigeeri **frontend** kausta käsklusega
```cmd
cd frontend
```

ning installi dependencies käsklusega
```
npm install
```

Selle tulemusena peaksid kõik lisad alla laadima.

#### Front-end Moodulid
1. [NodeJS 22.13.1 (LTS)](https://nodejs.org/en/download)
	1. **npm** versioni 11.0.0 jaoks.
2. [TypeScript]() - `npm install typescript --save-dev`
3. [Tailwind CSS](https://tailwindcss.com/docs/installation/using-vite) - `npm install tailwindcss @tailwindcss/vite`

##### NPM moodulid
Vaja läheb järgnevaid NPM mooduleid:
1. [@heroicons/react](https://heroicons.com/) - `npm install @heroicons/react`
2. [@headlessui/react](https://headlessui.com/react/disclosure) - `npm install @headlessui/react`
3. [Recharts](https://recharts.org/en-US/) - `npm install recharts`
4. [Dayjs](https://www.npmjs.com/package/dayjs) - `npm install dayjs`

### Back-End
Back end nõuab, et arvutis on installitud:
- [Java](https://www.java.com/en/)([JVM](https://www.java.com/download/ie_manual.jsp)) - Java Virtual Machine
- [Java Development Kit](https://www.oracle.com/java/technologies/downloads/#java21) (JDK) - Versioon `java 21.0.5 2024-10-15 LTS`
- [PostgreSQL](https://www.postgresql.org/)

Kasutades [Spring Boot Initializr](https://start.spring.io/) loodi Back-end järgneva Template alusel:
- Gradle - Groovy
- Java Language
- Spring Boot 3.4.1
- Packaging `Jar`
- Java `21`
- Dependencies
	- Spring Web
	- Spring Data JPA
	- PostgreSQL Driver

### Valikulised Rakendused
1. [PgAdmin ](https://www.postgresql.org/ftp/pgadmin/pgadmin4/v8.14/windows/) et andmebaase mugavalt käsitleda


## Rakenduse Jooksutamin

Kui kõik eelnimetatud vajalik installitud, saab rakendust jooksutada.

### Andmebaasi seadistamine
Alustuseks tuleb seadistada andmebaas.
Juhul kui veel ei ole, tuleb installida [PostgreSQL](https://www.postgresql.org/) arvutisse.

PostgreSQL installimis asukoha võib suvaliselt valida kus soovite, aga meelde tuleb jätta järgnevad seadistused:
+ kasutajanimi (vaikimisi **postgres**)
+ port (vaikimisi **5432**)
+ parool - kasutaja enda määrata

Neid andmeid hiljem kasutame **Backend-is** et konfigureerida ühendus andmebaasiga.

### Back-End
Backend-i jooksutamiseks tuleb eelnevalt seadistada andmebaasi asukoht ja kasutaja.
Mine projekti juur kausta (`enefitPowerApp`) ja ava järgmine fail endale meeldiva rakendusega:
```shell
vim backend\src\main\resources\application.properties
```

seejärel muuda järgnevad andmed vastavalt andmebaasi installeerimise käigus määratud väärtustega:
```application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=
```

Juhul kui ei muutnud PostgreSQL installeerimisel vaikimisi seadistatud väärtusi tuleb ainul sinu valitud parool lisada.


Backend-i jooksutamiseks navigeeri `backend` kausta projekti juur kaustas (`enefitPowerApp/backend`).
```shell
cd backend
```

seejärel jooksuta programm järgneva käsklusega
```shell
./gradlew bootRun
```

Seejärel algab rakenduse käivitamise protsess.
Rakendus käivitus edukalt kui viimased read on järgnevad:
```shell
Started EnefitpowerApplication in 3.982 seconds (process running for 4.314)
<==========---> 80% EXECUTING [11s]
> :bootRun
```
Seejärel jäta see terminal taustal lahti ning liigu edasi [front-end peatükini](#Front-End).



Juhul kui tekkis mingi viga, vaata ehk on sellest dokumenteeritud [Kippuvad Vead](#Kippuvad-vead) peatükkis.

### Front-End
Frontend-i jooksutamine peaks lihtsam olema, kuna puudub manuaalne konfiguratsioon.
Navigeeri uue terminaliga projekti juur kausta `enefitPowerApp` ja sealt navigeeri `frontend` kausta.
```shell
cd frontend
```

Frontend-i jooksutamiseks kasuta järgmist käsklust
```shell
npm run dev
```

Juhul kui kõik töötab, peaks konsooli välja nägema järgnevalt
```shell

> frontend@0.0.0 dev
> vite


  VITE v6.0.11  ready in 717 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help

```

Seejärel front-end käivitus edukalt ning rakendus jookseb.

## Rakendusest Endast
Kui rakendus töötab, tuleb navigeerida front-end aadressile (http://localhost:5173/) kus peaks ette tulema sisselogimise aken.

Kuna andmebaas on värsekelt loodud puuduvad sealt kasutajad.
Klikka `Registeeri UUS kasutaja` nupule ja täida väljad endale meeldejäävate andmetega.

Juhul kui Back-end ei tuvasta viga sisestatud andmetes logib sind ka kohe sisse.

### Elektri Börs
**Elektri Börs** on loodud, et kasutajale näidata Eleringi andmetest saadud informatsiooni.
Navigeerides leheküljele saadetakse `Fetch` käsklus backend-i mis kogub kokku viimase aasta elektri keskmise hinna, arvutab kuu keskmise elektri hinna ning lõpetuseks edastab andmed kliendile.

Kasutades [Recharts](https://recharts.org/en-US/) pluginat näitab saadud andmeid tulpdiagrammina.
Kokku on 4 tulpdiagrammi,
1. Kuu keskmine Elektri hind sent kWh kohta
2. Kuu keskmine Elektri hind sent kWh kohta koos käibemaksuga
3. Kuu keskmine Elektri hind EUR/MWh
4. Kuu keskmine Elektri hind EUR/MWh koos käibemaksuga

### Tarbimine
**Tarbimine** annab kasutajale võimaluse lisada eluasemeid, nähe kindla eluaseme elektrikulu või lisada kindlale eluasemele uus tarbimine.

Uut eluase salvestades küsib applikatsioon järgnevat informatsiooni:
1. `Eluase Nimetus` - kasutaja enda valitud nimetus
2. `Aadress` - asukoha aadress
3. `Linn` - asukoha linn
4. `Postiindeks` - asukoha postiindeks

Täites kõik neli vormi ja klikates **Salvesta** ilmub uus asukoht ekraanile.
Sellele lisandub uus nupp `Lisa uus tarbimine` kus kasutaja saab salvestada elektri tarbimisi elukoha kohta.
1. `Kogus kWh` - kogus elektrit kasutatud. Mõõduna kasutatakse **kWh** ühikut.
2. `Kuupäev kulutatud` - mis kuupäeval elektrit kasutatu. Kellaaeg on hetkel eemaldatud lihtsustamise eesmärgil.

Täites 2 lahtrit ja klikates **SALVESTA** ilmub tulpdiagramm koos värskelt lisatud elektrikuluga.

Juhul kui kasutaja lisab elektri tarbimise juba olemasolevale kuupäevale liidetakse see selle päeva kulule lisaks.


## Kippuvad Vead
### Back-end veateated
1.
```shell
: SQL Error: 0, SQLState: 08004
: The server requested SCRAM-based authentication, but no password was provided.
: HHH000342: Could not obtain connection to query metadata

org.hibernate.exception.JDBCConnectionException: unable to obtain isolated JDBC connection [The server requested SCRAM-based authentication, but no password was provided.]
```
tähendab, et andmebaasi parooli pole seadistatud.
See tuleb seadistada [back end peatüki alguses](#Back-End)

2.
```shell
: SQL Error: 0, SQLState: 28P01
: FATAL: password authentication failed for user "postgres"
: HHH000342: Could not obtain connection to query metadata

org.hibernate.exception.GenericJDBCException: unable to obtain isolated JDBC connection [FATAL: password authentication failed for user "postgres"]
```
Andmebaasi parool on vale, vaata see uuesti üle `application.properties` failist ning uuenda õigega parooliga.
