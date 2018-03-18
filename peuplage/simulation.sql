INSERT INTO DateCircuit (idcircuit, dateDepartCircuit, nbPersonnes)
VALUES ('C0001', TO_DATE('2017-12-22','YYYY-MM-DD'), '2');

INSERT INTO Simulation (idSimulation, dateDepartSimu, dateArriveeSimu, nbPersonnesSimu)
VALUES (SimuKey.nextval, TO_DATE('2017-12-20','YYYY-MM-DD'),TO_DATE('2018-01-01','YYYY-MM-DD'), '2')
;

INSERT INTO DateCircuitSimulation (idCircuit, dateDepartCircuit, idSimulation)
VALUES ('C0001', TO_DATE('2017-12-22', 'YYYY-MM-DD'), SimuKey.currval);

INSERT INTO ResaHotel (idResaHotel, dateEntree, nbJoursHotel, nbPetitDej, nomHotel, ville, pays)
VALUES ('RH001', TO_DATE('2017-12-22', 'YYYY-MM-DD'), '5', '2', 'Marceau Champs', 'Paris', 'France')
;

INSERT INTO ResaHotelSimulation (idResaHotel, idSimulation)
VALUES ('RH001', SimuKey.currval);

/**
INSERT INTO VilleSimulation (nomVille, pays, idSimulation)
VALUES ("Londres", "Angleterre", SimuKey.curval);


INSERT INTO LieuAVisiter (nomVille, pays, nomLieu, idSimulation)
VALUES ("Paris", "France", 'Tour Eiffel', SimuKey.curval);


INSERT INTO DateCircuitSimulation (idCircuit, dateDepartCircuit, idSimulation)
VALUES ("C0001", TO_DATE('2017-12-22', 'YYYY-MM-DD'), SimuKey.curval);
*/


