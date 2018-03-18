--start initialisation.sql
--on DELETE CASCADE?

Drop Table ResaHotelSimulation;
Drop Table DateCircuitSimulation;
Drop Table Reservation;
Drop Table Client;
Drop Table ResaHotel;
Drop Table Simulation;
Drop Table Hotel;
Drop Table Etapes;
Drop Table DateCircuit;
Drop Table Circuit;
Drop Table LieuAvisiter;
Drop Table Ville;


-- Ville(nomVille_, pays_)
Create Table Ville (
    nomVille char(20),
    pays char(20),
    PRIMARY KEY (nomVille, pays)
);

-- LieuAvisiter(nomLieu_, ville_, pays_, adresseLieu, descriptifLieu, prix)
Create Table LieuAvisiter (
    nomLieu char(20),
    nomVille char(20),
    pays char(20),
    adresseLieu varchar(50),
    descriptifLieu varchar(100),
    prix INTEGER CHECK(prix >= 0),
    PRIMARY KEY (nomLieu, nomVille, pays),
    FOREIGN KEY(nomVille, pays) REFERENCES Ville(nomVille, pays) ON DELETE CASCADE
);

--Circuit(idCircuit_, descriptif, villeDepart, paysDepart, villeArrivee, paysArrivee, nbJoursTotal, prixCircuit)
 Create Table Circuit(
     idCircuit char(5) PRIMARY KEY,
     descriptif char(50),
     villeDepart char(20),     paysDepart char(20),
     villeArrivee char(20),
     paysArrivee char(20),
     nbJoursTotal INTEGER CHECK(nbJoursTotal > 0),
     prixCircuit INTEGER CHECK(prixCircuit > 0),
     FOREIGN KEY(villeDepart, paysDepart) REFERENCES Ville(nomVille, pays),
     FOREIGN KEY(villeArrivee, paysArrivee) REFERENCES Ville(nomVille, pays)
 );


--DateCircuit(idCircuit_, dateDepartCircuit_, nbPersonnes)
Create Table DateCircuit(
     idCircuit char(5),
     dateDepartCircuit date,
     nbPersonnes INTEGER CHECK(nbPersonnes >= 0),
     PRIMARY KEY(idCircuit, dateDepartCircuit),
     FOREIGN KEY(idCircuit) REFERENCES Circuit(idCircuit) ON DELETE CASCADE
 );

--Etapes(idCircuit_, ordre_, nomLieu, ville, pays, nbJours)
Create Table Etapes(
     idCircuit char(5),
     ordre INTEGER CHECK(ordre > 0),
     nomLieu char(20),
     ville char(20),
     pays char(20),
     nbJours INTEGER,
     PRIMARY KEY(idCircuit, ordre),
     FOREIGN KEY(idCircuit) REFERENCES Circuit(idCircuit) ON DELETE CASCADE,
     FOREIGN KEY(nomLieu, ville, pays) REFERENCES LieuAvisiter(nomLieu, nomVille, pays)
 );

--Hotel(nomHotel_, ville_, pays_, adresseHotel, nbChambresTotal, prixChambre, prixPetitDejeuner)
Create Table Hotel(
     nomHotel char(20),
     ville char(20),
     pays char(20),
     adresseHotel char(50),
     nbChambresTotal INTEGER CHECK(nbChambresTotal > 0),
     prixChambre INTEGER CHECK(prixChambre > 0),
     prixPetitDejeuner INTEGER CHECK(prixPetitDejeuner >= 0),
     PRIMARY KEY(nomHotel, ville, pays),
     FOREIGN KEY(ville, pays) REFERENCES Ville(nomVille, pays) ON DELETE CASCADE
 );


Create Table Simulation(
     idSimulation char(5) PRIMARY KEY,
     dateDepartSimu date,
     dateArriveeSimu date,
     nbPersonnesSimu INTEGER CHECK(nbPersonnesSimu > 0)
);
     
Create Table ResaHotel(
     idResaHotel char(5),
     dateEntree date,
     nbJoursHotel INTEGER CHECK(nbJoursHotel > 0),
     nbPetitDej INTEGER CHECK(nbPetitDej >= 0),
     nomHotel char(20),
     ville char(20),
     pays char(20),
     PRIMARY KEY(idResaHotel),
     FOREIGN KEY(nomHotel, ville, pays) REFERENCES Hotel(nomHotel, ville, pays)
);

Create Table Client(
     idClient char(5) PRIMARY KEY,
     nomClient char(40),
     typeClient char(20) Constraint typeDuClient CHECK (typeClient IN ('societe', 'groupe', 'individuel')), 
     annee INTEGER CHECK(annee > 1900),
     typePaiement char(20) Constraint typeDePaiement CHECK (typePaiement IN ('carte', 'especes', 'cheque','bitcoin'))
);

Create Table Reservation(
     numResa char(5),
     idClient char(5),
	 idSimulation char(5),
     PRIMARY KEY(numResa),
     FOREIGN KEY(idClient) REFERENCES Client(idClient),
	 FOREIGN KEY(idSimulation) REFERENCES Simulation(idSimulation)
);


-- Créer les tables correspondant aux associations 


--entité faible circuit-étape

--association binaire dont une des cardinalités est (1...1): vérifier
--      Ville - LieuAVisiter
--      LieuAVisiter - étape
--      Circuit - dateCircuit
--      Ville - Hotel
--      Hotel - ResaHotel
--      Client - Reservation
--      Circuit - Ville
--		Simulation - reservation

--association binaire dont une des cardinalités est (0...1):
-- aucun

--association binaire dont une des cardinalités est (x...*):
--      DateCircuit - Simulation


Create Table DateCircuitSimulation(
     idCircuit char(5),
     dateDepartCircuit date,
     idSimulation char(5),
     PRIMARY KEY (idCircuit, dateDepartCircuit, idSimulation),
     FOREIGN KEY (idCircuit, dateDepartCircuit) REFERENCES DateCircuit(idCircuit, dateDepartCircuit),
     FOREIGN KEY (idSimulation) REFERENCES Simulation(idSimulation)
     );

Create Table ResaHotelSimulation(
     idResaHotel char(5),
     idSimulation char(5),
     PRIMARY KEY (idResaHotel, idSimulation),
     FOREIGN KEY (idResaHotel) REFERENCES ResaHotel(idResaHotel),
     FOREIGN KEY (idSimulation) REFERENCES Simulation(idSimulation)
     );
