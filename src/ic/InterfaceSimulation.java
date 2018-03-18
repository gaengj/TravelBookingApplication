package ic;

import java.util.HashMap;
import java.util.Scanner;

import javafx.util.Pair;

public class InterfaceSimulation extends InterfaceConsole{
	private InterfaceClient ic;
	private String idSimulation;
	private String nbPersonnesSimu;
	private String dateDepartSimu;
	private String dateArriveeSimu;
	private int coutTotal;
	private HashMap<Pair<String,String>, String> CircuitsSelectionnes;
	
	public InterfaceSimulation(InterfaceClient ic){
		this.ic = ic;
		this.CircuitsSelectionnes = new HashMap<Pair<String,String>, String>();
	}
	
	@Override
	public int launch(){
		System.out.println("Bienvenue dans le mode simulation.");
		System.out.println("Quel est le nombre de personnes de la simulation?");
		nbPersonnesSimu = super.in.nextLine();
		System.out.println("Quelle est la date de depart prevue? Ecrire la date sous la forme AAAA-MM-JJ");
		dateDepartSimu = super.in.nextLine();
		System.out.println("Quelle est la date d'arrivee prevue? Ecrire la date sous la forme AAAA-MM-JJ");
		dateArriveeSimu = super.in.nextLine();
		
		String query="INSERT INTO Simulation "
				+ "VALUES (SimuKey.nextval,  + TO_DATE('" + dateDepartSimu + "', 'YYYY-MM-DD') "+ ", + TO_DATE('" + dateArriveeSimu +"', 'YYYY-MM-DD') " + ", '" + nbPersonnesSimu + "')";
		simpleUpdate(query);
		System.out.println("Votre identifiant simulation est:");
		simpleQuery("SELECT Simukey.currval FROM Dual");
		idSimulation=super.getLastLine();
		
		System.out.println("Veuillez entrer l'identifiant circuit du premier circuit selectionne.");
		parseDateCircuit();
		int reponse = 1;
		while (reponse==1) {
			System.out.println("Voulez-vous selectionner un autre circuit?");
			reponse = ouiNon();
			if (reponse == 1){
				System.out.println("Veuillez entrer l'identifiant circuit du circuit.");
				parseDateCircuit();
			}
		}
		commit();
		System.out.println("Votre identifiant simulation est:");
		simpleQuery("SELECT Simukey.currval FROM Dual");
		System.out.println("Voulez-vous valider votre simulation pour en faire une reservation?");
		reponse = ouiNon();
		if (reponse == 1) {
		this.ic.getResa().launch(idSimulation);
		}
		this.ic.launch();
		return 0;
	}

	private void parseDateCircuit() {
		String idCircuit = super.in.nextLine();
		System.out.println("Entrer la date de depart pour ce circuit.");
		String dateDepartCircuit = super.in.nextLine();
		//v�rifier que le circuit existe, que c'est bien une date, ...
		
		String query="INSERT INTO DateCircuitSimulation "
				+ "VALUES ('"+ idCircuit +"',  TO_DATE('" + dateDepartCircuit + "', 'YYYY-MM-DD') "+ ", "+idSimulation+")";
		simpleUpdate(query);
		parseHotel();
	}
	
	private void parseHotel() {
		System.out.println("Souhaitez vous reserver un hotel pour ce circuit?");
		int reponse = ouiNon();
		if (reponse == 2) { parseHotel();}
		if (reponse == 0) { return;}
		while(reponse == 1) {
			System.out.println("Entrer le nom de l'hotel.");
			String nomHotel = super.in.nextLine();
			System.out.println("Entrer la ville dans laquelle se trouve l'hotel.");
			String ville = super.in.nextLine();
			System.out.println("Entrer le pays dans lequel se trouve l'hotel.");
			String pays = super.in.nextLine();
			System.out.println("Entrer la date d'entree pour cet hotel.");
			String dateEntree = super.in.nextLine();
			System.out.println("Entrer le nombre de nuits pour cet hotel.");
			String nbJoursHotel = super.in.nextLine();
			System.out.println("Entrer le nombre de petit dejeuners demandes par jour.");
			String nbPetitDej = super.in.nextLine();
			
			
			String query="INSERT INTO ResaHotel "
					+ "VALUES (ResaHotelKey.nextval , TO_DATE('" + dateEntree + "', 'YYYY-MM-DD'), '"+ nbJoursHotel + "', '"+ nbPetitDej + "', '"+ nomHotel + "', '"+ ville + "', '" + pays + "')";
			simpleUpdate(query);
			query="INSERT INTO ResaHotelSimulation "
					+ "VALUES (ResaHotelKey.currval ,"+idSimulation+" )";
			simpleUpdate(query);
			System.out.println("Voulez vous reserver un autre hotel pour ce circuit?");
			reponse = ouiNon();
		}

		
	}
	
	private int ouiNon() {
		String text = super.in.nextLine();
		text = text.toLowerCase();
		text = text.replace(" ", "");
		if (text.equals("non")) {
			return 0;
		} 
		if (text.equals("oui")) {
			return 1;
		}
		return 2; 
	}
	

}
