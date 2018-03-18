package ic;

import java.util.Scanner;

import requests.SimpleQuery;
public class InterfaceConsultation extends InterfaceConsole{

	private InterfaceClient ic;
	private String statement;
	private String pays = "*";
	private String prixMax = "*";
	private String ville = "*";
	private String lieuAVisiter = "*";
	// date depart
	
	
	public InterfaceConsultation(InterfaceClient ic){
		this.ic = ic;
	}
	
	@Override
	public int launch(){
		System.out.println("Bienvenue en mode Consultation.");
		System.out.println("Voici la brochure complete.");
		query();
		System.out.println("Vous pouvez affiner votre recherche selon un pays, une ville, un lieu "
				+ "a visiter ou encore trier les circuits selon un prix maximal. \nPour cela, veuillez "
				+ "taper respectivement, une commande à la fois : \npays=[nom_pays], "
				+ "\nprixMax=[votre_prix_max], \nville=[nom_ville],  \nlieuAVisiter=[nom_lieu]"
				+ "\nPour revenir au menu principal, écrivez menu");
		parse();
		return 0;
	}
	
	public void parse() {		
		while(true) {
			String text = super.in.nextLine();
			text = text.trim();
			Scanner scanner = new Scanner(text);
		    scanner.useDelimiter("=");
		    if (scanner.hasNext()){
		      String name = scanner.next();
		      name = name.trim();
		      String value = "";
		      value = value.trim();
			  if (scanner.hasNext()){ value = scanner.next();}
		      log("le champ est : " + name + ", et la valeur est : " + value); //pour le deboggage
		      switch(name){
		      case("menu"):
		    	  this.ic.launch();
		          return;
		      case("X"):
		    	  return;
		      case("reset"):
		    		pays = "*";
		  			prixMax = "*";
		  			ville = "*";
		  			lieuAVisiter = "*";
		      default:
		    	  switch(name){
			    	  case("pays"):
				    	  pays = value;
				          query();
				    	  break;
				      case("prixMax"):
				    	  prixMax = value;
			          	  query();
				    	  break;
				      case("ville"):
				    	  ville = value;
			              query();
				    	  break;
				      case("lieuAVisiter"):
				    	  lieuAVisiter = value;
			              query();
				    	  break;
				      default:
				    	  log("syntaxe invalide, veuillez reessayer.");
				      }
		    	  }
		      
		    } else {
		      log("Entree invalide, veuillez reessayer.");
		    }
		    scanner.close();
		}

	}
	
	private static void log(Object aObject){
	    System.out.println(String.valueOf(aObject));
	  }
	
	private void query() {
		System.out.println();
		
		System.out.println("DateCircuit.dateDepartCircuit \t DateCircuit.nbPersonnes \t Circuit.idCircuit \t Circuit.descriptif \t "
				+ "Circuit.prixCircuit \t Circuit.nbJoursTotal \t Etapes.ordre \t Etapes.nbJours \t LieuAVisiter.nomLieu ");
		
		statement = "SELECT DateCircuit.dateDepartCircuit, DateCircuit.nbPersonnes, Circuit.idCircuit, Circuit.descriptif, "
				+ " Circuit.prixCircuit, Circuit.nbJoursTotal, Etapes.ordre, Etapes.nbJours, LieuAVisiter.nomLieu, "
				+ " LieuAVisiter.adresseLieu, LieuAVisiter.descriptifLieu, LieuAVisiter.prix, Ville.nomVille, Ville.pays "
				+ " FROM Circuit, Etapes, LieuAvisiter, Ville, DateCircuit WHERE Circuit.idCircuit = DateCircuit.idCircuit "
				+ " AND Circuit.idCircuit = Etapes.idCircuit AND Etapes.nomLieu = LieuAVisiter.nomLieu AND LieuAvisiter.pays = Ville.pays ";
		
				if (!pays.equals("*")){
					statement += " AND Ville.pays = '" + pays + "'";
				}
				if  (!prixMax.equals("*")){
					statement += " AND Circuit.prixCircuit<= '" + prixMax + "'";
				}
				if (!ville.equals("*")){
					statement += " AND Ville.nomVille= '" + ville + "'";
				}
				if (!lieuAVisiter.equals("*")){
					statement += " AND lieuAVisiter.nomLieu= '" + lieuAVisiter + "'";
				}
				
		simpleQuery(statement);
		
		System.out.println("Hotel.nomHotel \t Circuit.idCircuit \t Hotel.adresseHotel \t Hotel.nbChambresTotal \t Hotel.prixChambre \t "
				+ "Hotel.prixPetitDejeuner \t Ville.nomVille \t Ville.pays");
		
		statement = "SELECT DISTINCT Hotel.nomHotel, Circuit.idCircuit, Hotel.adresseHotel, Hotel.nbChambresTotal, Hotel.prixChambre, Hotel.prixPetitDejeuner, "
				+ " Ville.nomVille, Ville.pays FROM Hotel, Circuit, Etapes, LieuAvisiter, Ville, DateCircuit "
				+ " WHERE Circuit.idCircuit = DateCircuit.idCircuit AND Circuit.idCircuit = Etapes.idCircuit "
				+ " AND Etapes.nomLieu = LieuAVisiter.nomLieu AND LieuAvisiter.pays = Ville.pays AND Ville.nomVille = Hotel.Ville ";
		if (!pays.equals("*")){
			statement += " AND Ville.pays = '" + pays + "'";
		}
		if  (!prixMax.equals("*")){
			statement += " AND Circuit.prixCircuit<= '" + prixMax + "'";
		}
		if (!ville.equals("*")){
			statement += " AND Ville.nomVille= '" + ville + "'";
		}
		if (!lieuAVisiter.equals("*")){
			statement += " AND lieuAVisiter.nomLieu= '" + lieuAVisiter + "'";
		}
		
		simpleQuery(statement);
		
		System.out.println("pays= "+pays+", ville="+ville+", LieuAVisiter="+lieuAVisiter+", prixMax="+prixMax);
	}
		
}
