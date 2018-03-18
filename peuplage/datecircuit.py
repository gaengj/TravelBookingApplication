from random import randint

date_circuit = open("date_circuit.sql", "w")

# Etapes(idCircuit_, ordre_, nomLieu, ville, pays, nbJours)
# --DateCircuit(idCircuit_, dateDepartCircuit_, nbPersonnes)

# Circuit(idCircuit_, descriptif, villeDepart, paysDepart, villeArrivee, paysArrivee, nbJoursTotal, prixCircuit)
chaine2 = "Nous parcourerons"
compteur = 0
def convert(entier):
    chaine = "0000" + str(entier)
    sortie = "C" + chaine[-4] + chaine[-3] + chaine[-2] + chaine[-1]
    return(sortie)

for i in range (91):
    mois_aleatoire = randint(1,12)
    nombre = randint(1,3)
    nombre2 = randint(1,14)
    mois_depart = mois_aleatoire + nombre
    date_aleatoire =  randint(1,28)
    date_depart = date_aleatoire + nombre2
    nbPersonnes = randint(1,6)
    compteur+= 1
    chaine1 = "TO_DATE('2018-{}-{}','YYYY-MM-DD')".format(mois_aleatoire,date_aleatoire)
    date_circuit.write("Insert into DateCircuit values(\'{}\', \'{}\',\'{}\');\n".format(convert(compteur),chaine1,nbPersonnes))
