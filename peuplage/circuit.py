from random import randint
ville = ["Paris","Moscou","New Dehli","Londres","Rio de Janeiro","Amsterdam","New York", "Tanger", "Tokyo", "Madrid"]
villes = ["'Paris', 'France'","'Moscou', 'Russie'","'New Dehli', 'Inde'", "'Londres', 'Angleterre'", "'Rio de Janeiro', 'Bresil'", "'Amsterdam', 'Pays-Bas'", "'New York', 'Etats-Unis'", "'Tanger', 'Maroc'", "'Tokyo', 'Japon'", "'Madrid', 'Espagne'"]

circuit = open("circuit.sql", "w")
etape  =  open("etapes.sql", "w")
Lieuvisiter= ['Tour Eiffel', 'Champs-elysees', 'Kremlin de Moscou', 'Parc Gorki', 'Fort Rouge', 'Qutb Minar', 'Big Ben', 'British Museum', 'Christ Rédempteur', 'Corcovado', 'Rijksmuseum Amsterdam', 'Heineken Experience', 'Central Park', 'Statue de la Liberte','Musee de la Kasbah', \
'Grottes Hercule', 'Senso-ji', 'Tokyo Skytree', 'Parc du Retiro', 'Puerta del Sol']
# Etapes(idCircuit_, ordre_, nomLieu, ville, pays, nbJours)

# Circuit(idCircuit_, descriptif, villeDepart, paysDepart, villeArrivee, paysArrivee, nbJoursTotal, prixCircuit)
chaine2 = "Nous parcourerons"
compteur = 0
def convert(entier):
    chaine = "0000" + str(entier)
    sortie = "C" + chaine[-4] + chaine[-3] + chaine[-2] + chaine[-1]
    return(sortie)

for i in range (len(villes)):
    for j in range (len(villes)):
        if i != j:
            compteur+= 1
            nbjours = randint(8, 30)
            prixCircuit = randint(1200, 3000)
            nbjours_liste = [nbjours//4,nbjours//4,nbjours//4,nbjours - nbjours//4]
            circuit.write("Insert into Circuit values(\'{}\', \'{} {} {} {}\', {}, {}, {}, {})\n".format(convert(compteur),chaine2,ville[i], "et",ville[j],villes[i],villes[j],nbjours, prixCircuit))
            for k in range(1,3):
                etape.write("Insert into Etapes values(\'{}\', {}, \'{}\', {},  {})\n".format(convert(compteur), k, Lieuvisiter[2*i +k-1],villes[i],nbjours_liste[k-1]))
            for k in range(3,5):
                etape.write("Insert into Etapes values(\'{}\', {}, \'{}\', {},  {})\n".format(convert(compteur), k, Lieuvisiter[2*j +k-3],villes[j],nbjours_liste[k-1]))
