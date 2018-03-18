package ic;

import java.io.Console;
import java.util.LinkedList;

public class InterfaceResa extends InterfaceConsole{
	
	private int idSimu;
	private int idClient;
	private int prixResa=0;
	private int prixTotal=0;
	
	private InterfaceClient ic;
	private String query;

	private String[] fields;
	private String[] types;
	
	
	public InterfaceResa(InterfaceClient ic){
		this.ic = ic;
		this.idSimu = 0;
		this.types = null;
		this.fields = null;
		this.idClient = 0;
	}
	                             
	@Override
	public int launch(){		
		goDown(40);
		System.out.println("__________________Mode Reservation ___________________");
		System.out.println("Bievenue dans le mode Reservation.");
		System.out.println("Taper X pour revenir au menu principal.");
		goDown(10);
		recoverSimu();
		commit();
		goDown(40);
		recoverClient();
		commit();
		goDown(40);
		confirmAll();
		commit();
		endMenu();
		System.out.println("Sortie du mode reservation.");
		return 0;
	}
	
	public int launch(String idSimu) {
		this.idSimu = Integer.parseInt(idSimu);
		this.launch();
		return 0;
	}
	
	public void finalResa(){
		query="INSERT INTO Reservation (numResa, idSimulation, idClient) "
				+ "VALUES (ResaKey.nextval, '" + idSimu + "', '" + idClient + "')";
		System.out.println(query);
		simpleUpdate(query);
		System.out.println("Félicitation, vous venez de réserver.");
	}
	
	public void confirmAll(){
		System.out.println("Debut de la reservation de la simulation.");
		//dispSimuDetails(0);
		System.out.println("Pour pouvoir la valider,");
		System.out.println("vous allez devoir de confirmer chacunes des reservations (hotel/circuit).");
	
		query="SELECT rh.idResaHotel "
				+ "FROM ResaHotelSimulation rhs, ResaHotel rh "
				+ "WHERE rhs.idSimulation='"+idSimu+"' "
				+ "AND rh.idResaHotel=rhs.idResaHotel ";
		LinkedList<String> resaHotelList = queryToList(query);	
		
		if (resaHotelList.isEmpty()) {
			System.out.println("Vous ne prévoyez aucune réservation d'Hotel.");
		}
		
		for (String idrh : resaHotelList) {
			resaHotel(idrh);
		}
		
		
		query="SELECT dc.idCircuit, dc.dateDepartCircuit "
				+ "FROM DateCircuitSimulation dcs, DateCircuit dc "
				+ "WHERE dcs.idSimulation='"+idSimu+"' "
				+ "AND dc.idCircuit=dcs.idCircuit "
				+ "AND dc.datedepartCircuit=dcs.datedepartCircuit";
		LinkedList<String> resaCircuitList = queryToList(query);	
		
		if (resaCircuitList.isEmpty()) {
			System.out.println("Vous ne prévoyez aucune réservation circuit.");
		}
		for (String line : resaCircuitList) {
			System.out.println(line);
			String idc=line.split(" ")[0];
			String dc=line.split(" ")[1] ;
			resaCircuit(idc, dc);
		}
		
		System.out.println("Fin des vérifications, récapitulatif :");
		dispSimuDetails(0);
		System.out.println("Prix total de la reservation de ce qui a été validé :"+ prixTotal);
		System.out.println("Voici votre simulation,voulez vous reserver l'ensemble maintenant (oui) ou non (non) ?");
		readAns();
		switch (cAns) {
		case "oui":
			System.out.println("Votre voyage va être reserver. Merci !");
			finalResa();
			break;
		case "non":
			System.out.println("Vous venez d'annuer la reservation. Votre simulation est toujours disponible sous le numero :"+idSimu+".");
			default:
				break;
		}
		
	}
	
	private void endMenu(){
		this.ic.launch();
	}
		
	public void resaHotel(String idResaHotel) {
		
		System.out.println();
		query="SELECT * "
				+ "FROM ResaHotel "
				+ "WHERE idResaHotel='"+idResaHotel+"' ";
		System.out.println("IDRESAHOTEL \t DATEENTREE \t NBJOURSHOTEL \t NBPETITDEJ  \t NOMHOTEL ");
		sendQuery();
		
		
		System.out.println("Prix de cette resa");
		query="SELECT h.prixChambre pc, s.nbPersonnesSimu nbp, h.prixPetitdejeuner ppd, rh.nbpetitdej nbpd   "
				+ "FROM Hotel h, ResaHotel rh, ResaHotelSimulation rhs, Simulation s  "
				+ "WHERE rh.nomHotel=h.nomHotel "
				+ "AND rh.pays=h.pays "
				+ "AND rh.ville=h.ville "
				+ "AND rhs.idSimulation='" + idSimu +"' "
				+ "AND s.idSimulation='" + idSimu +"' "
				+ "AND rhs.idResaHotel='"+idResaHotel+"' "
				+ "AND rh.idResaHotel='"+idResaHotel+"' ";
		System.out.println(" PRIXCHAMBRE \t NBPERSONNES \t PRIXPETITDEJ \t NBPETITDEJ");
		sendQuery();
		
		System.out.println("TOTAL");
		query="SELECT  (h.prixChambre*s.nbPersonnesSimu + h.prixPetitdejeuner*rh.nbpetitdej) total  "
				+ "FROM Hotel h, ResaHotel rh, ResaHotelSimulation rhs, Simulation s "
				+ "WHERE rh.nomHotel=h.nomHotel "
				+ "AND rh.pays=h.pays "
				+ "AND rh.ville=h.ville "
				+ "AND rhs.idSimulation='" + idSimu +"' "
				+ "AND s.idSimulation='" + idSimu +"' "
				+ "AND rhs.idResaHotel='"+idResaHotel+"' "
				+ "AND rh.idResaHotel='"+idResaHotel+"' ";
		sendQuery();
		prixResa = Integer.parseInt( getLastLine() );
		
		int dispo = checkHotelDispo(idResaHotel);
		if (dispo==1) {
			System.out.println("Nous sommes désolés mais la reservation "
					+ "d'hotel suivante n'est plus valable. Il faut modifier vos dates.");
			System.out.println("Il faut que vous modifiez cette reservation.");
			System.out.println("Supprimer ce circuit (oui) ou sortir (non)");
			
			switch (readAns()){
			case "oui":
				deleteResaHotel(idResaHotel);
				break;
			case "non":
				System.out.println("Abandon.");
				this.ic.launch();
				break;
			default :
					break;
			}
			
			// Il faut refaire une simulation !
		} else {
			System.out.println(" Prix pour cet Hotel : "+prixResa);
			System.out.println("Prix pour l'ensemble de la reservation sans celle la " +prixTotal);
			System.out.println("L'hotel est disponible, le reserver maintenant (oui) ou bien supprimer cette reservation (non) ?");
			readAns();
			switch (cAns) {
			case "oui":
				prixTotal += prixResa;
				System.out.println("Reservé !.");
				break;
			case "non":
				deleteResaHotel(idResaHotel);
				default:
					break;
			}
		}
	}

	private void deleteResaHotel(String idResaHotel) {
		query="DELETE FROM ResaHotelSimulation rhs "
				+ "WHERE rhs.idSimulation='" +idSimu + "' "
				+ "AND rhs.idResaHotel='" + idResaHotel + "' ";
		System.out.println(query);
		simpleUpdate(query);
	}
	

	/**
	 * Verifie si une resaHotel est toujours valable.
	 * @param idResaHotel check si la cette resa est valable.
	 * @return 1 si l'hotel n'est pas dispo et 0 sinon.
	 */
	public int checkHotelDispo(String idResaHotel) {
		int capaciteHotel = 0;
		int nbResaHotel = 0;
		int nbPersonnes = 0;
		//Nombre de reservations enregistrees par pour cet hotel
		query = "SELECT sum( s.nbPersonnesSimu ) "
				+ "FROM ResaHotel rh1, ResaHotel rh2, ResaHotelSimulation rhs, "
					+ "Reservation r, Simulation s "
				/* Selectionne la ResaHotel correspondant au Client */
				+ "WHERE rh1.idResaHotel='"+ idResaHotel +"' "
				/* Slectionne toutes les ResaHotel qui concerne l'hotel reserve par le Client */
				+ "AND rh2.nomHotel=rh1.nomHotel "
				+ "AND rh2.ville=rh1.ville "
				+ "AND rh2.pays=rh1.pays "
				/* Selectionne toutes les ResaHotel associees a une vrai Reservation */
				+ "AND rhs.idSimulation=s.idSimulation "
				+ "AND rhs.idResaHotel=rh2.idResaHotel "
				/* Selectionne toues les reservations ayant lieu pendant la meme periode
				 * que le Client i.e 
				 * (arrive < departClient et depart > arriveClient) */
				+ "AND rh2.dateEntree <= (rh1.dateEntree + rh1.nbJoursHotel) "
				+ "AND ( rh2.dateEntree + rh2.nbJoursHotel ) >= rh1.dateEntree ";

		
		System.out.println("Nombre de reservations pour cet hotel a ces dates :");
		sendQuery();
		if ( ! getLastLine().equals("null")) {
			nbResaHotel = Integer.parseInt(getLastLine());
		}
		System.out.println("Nombre de chambres attribuées a notre agence :");
		query = "SELECT h.nbChambresTotal "
				+ "FROM ResaHotel rh, Hotel h "
				+ "WHERE rh.idResaHotel='"+ idResaHotel +"' "
				+ "AND h.nomHotel=rh.nomHotel "
				+ "AND h.ville=rh.ville "
				+ "AND h.pays=rh.pays ";
		sendQuery();
		if ( ! getLastLine().equals("null")) {
			capaciteHotel = Integer.parseInt(getLastLine());
		}
		int nbChambresDispo = capaciteHotel - nbResaHotel;
		
		
		System.out.println("Nombre de personnes dans votre simulation :");
		query = "SELECT nbPersonnesSimu "
				+ "FROM Simulation "
				+ "WHERE idSimulation='"+idSimu+"' ";
		sendQuery();
		if ( ! getLastLine().equals("null")) {
			nbPersonnes = Integer.parseInt(getLastLine());
		}
		if (nbPersonnes > nbChambresDispo) {
			System.out.println("Désolé il n'y a plus de place à ces dates.");
			return 1;
		} else {
			return 0;
		}
	}
	
	public void resaCircuit(String idResaCircuit, String dateDepartCircuit) {
		System.out.println("Reservation circuit :" + idResaCircuit + " "+ dateDepartCircuit);
		
		
		
		System.out.println("Prix de cette resa");
		System.out.println("PRIXCIRCUIT \t NPERSONNES ");
		query="SELECT DISTINCT c.prixCircuit p, s.nbPersonnesSimu n "
				+ "FROM Circuit c, Simulation s, DateCircuitSimulation dcs "
				+ "WHERE c.idCircuit=dcs.idCircuit "
				+ "AND s.idSimulation='" + idSimu +"' "
				+ "AND dcs.idCircuit='"+idResaCircuit+"' "
				+ "AND dcs.dateDepartCircuit=TO_DATE('"+dateDepartCircuit+"' , 'YYYY-MM-DD')";
		sendQuery();
		
		System.out.println("TOTAL");
		query="SELECT DISTINCT (c.prixCircuit * s.nbPersonnesSimu) "
				+ "FROM Circuit c, Simulation s, DateCircuitSimulation dcs "
				+ "WHERE c.idCircuit=dcs.idCircuit "
				+ "AND s.idSimulation='" + idSimu +"' "
				+ "AND dcs.idCircuit='"+idResaCircuit+"' "
				+ "AND dcs.dateDepartCircuit=TO_DATE('"+dateDepartCircuit+"' , 'YYYY-MM-DD')";
		sendQuery();
		prixResa = Integer.parseInt( getLastLine() );
	
		int dispo = checkCircuitDispo(idResaCircuit, dateDepartCircuit);
		if (dispo==1) {
			System.out.println("Nous sommes désolés mais ce circuit est plein.");
			System.out.println("Il faut changer de date de circuit.");			
			System.out.println("Supprimer ce circuit (oui) ou sortir (non)");
			
			switch (readAns()){
			case "oui":
				deleteCircuit(idResaCircuit, dateDepartCircuit);
				break;
			case "non":
				System.out.println("Abandon.");
				this.ic.launch();
				break;
			default :
				break;
					
			}
		} else {
			System.out.println("Le circuit selectionné est disponible, le reserver maintenant (oui) ou annuler cette reservation (non) ?");
			readAns();
			switch (cAns) {
			case "oui":				
				prixTotal += prixResa;
				break;
			case "non":
				deleteCircuit(idResaCircuit, dateDepartCircuit);
				break;
			default:
					break;
			}
		}
	}
	
	private void deleteCircuit(String idResaCircuit, String dateDepartCircuit) {
		query="DELETE FROM dateCircuitSimulation dcs "
				+ "WHERE dcs.idSimulation='" +idSimu + "' "
				+ "AND dcs.idCircuit='" + idResaCircuit + "' "
				+ "AND dcs.dateDepartCircuit=TO_DATE('" + dateDepartCircuit + "' , 'YYYY-MM-DD')";
		System.out.println(query);
		simpleUpdate(query);
	}

	private int checkCircuitDispo(String idCircuit, String dateDepartCircuit) {
		int nbResaCircuit = 0;
		query = "SELECT sum( s.nbPersonnesSimu ) "
				+ "FROM DateCircuitSimulation dcs, "
					+ "Reservation r, Simulation s "
				/* Selectionne la DateCircuit correspondant au Client */
				+ "WHERE dcs.idCircuit='"+ idCircuit +"' "
				+ "AND dcs.dateDepartCircuit=TO_DATE('"+ dateDepartCircuit +"', 'YYYY-MM-DD') "
				/* Selectionne toutes les DateCircuitSimulation associees a une vrai Reservation */
				+ "AND r.idSimulation=dcs.idSimulation "
				+ "AND s.idSimulation=r.idSimulation ";
		System.out.println("Nombre de reservation pour ce circuit :");
		sendQuery();
		if ( ! getLastLine().equals("null")) {
			nbResaCircuit = Integer.parseInt(getLastLine());
		}
		
		int capaciteCircuit = 0;
		query = "SELECT dc.nbPersonnes "
				+ "FROM DateCircuit dc "
				+ "WHERE  dc.idCircuit='"+ idCircuit +"' "
				+ "AND dc.dateDepartCircuit=TO_DATE('" + dateDepartCircuit + "', 'YYYY-MM-DD') ";
		System.out.println("Capacite du circuit :");
		sendQuery();
		if ( ! getLastLine().equals("null")) {
			capaciteCircuit = Integer.parseInt(getLastLine());
		}
		
		System.out.println("Nombre de personnes dans votre simulation :");
		int nbPersonnes = 0;
		query = "SELECT nbPersonnesSimu "
				+ "FROM Simulation "
				+ "WHERE idSimulation='"+idSimu+"' ";
		sendQuery();
		if ( ! getLastLine().equals("null")) {
			nbPersonnes = Integer.parseInt(getLastLine());
		}
		int nbPlacesDispo = capaciteCircuit - nbResaCircuit;
		if (nbPlacesDispo < nbPersonnes) {
			System.out.println("Désolé il n'y a plus assez de place à ces dates.");
			return 1;
		} else {
			return 0;
		}
	}

	public int recoverClient() {
		if (idClient==0) {
			System.out.println("Avez vous un compte Client ? (oui, non, X)");
			readAns();
			switch (cAns) {
			case "oui":
				askClientId();
				break;
			case "non":
				System.out.println("Vous allez maintenant créer un compte client.");
				createAccount();
				break;
			case "X":
				this.ic.launch();
				break;
			default:
				launch();
				break;
			}
			return 1;
		} else {
			System.out.println("Etes vous le client suivant ?");
			query="SELECT * FROM Client WHERE idClient= "+idClient;
			sendQuery(0);
			System.out.println("Confirmer ? (oui, non, X)");		
			switch (readAns()) {
			case "oui":
				return 0;
			case "X":
				this.ic.launch();
				break;
			case "non":
			default :
				this.idClient=0;
				recoverClient();
				break;
			}
			return 1;
		}
	}
	
	public int askClientId(){
		System.out.println("Veuillez entrer votre numero de client");
		this.idClient = Integer.parseInt(readAns());
		query="SELECT * FROM Client WHERE idClient= "+idClient;
		sendQuery(0);
		System.out.println("Confirmer ? (oui, non, X)");
		switch (readAns()) {
		case "oui":
			return 0;
		case "X":
			this.ic.launch();
			break;
		case "non":
		default :
			recoverClient();
			break;
		}
		return 1;
	} 
	
	public void recoverSimu(){
		if (idSimu == 0){
			System.out.println("Avez vous deja fait une simulation ? (oui, non)");
			readAns();
			switch (cAns) {
			case "oui":
				askIdSimu();
				break;
			case "non":
				System.out.println("Veuillez d'abord effectuer une simulation.");
				this.ic.getSimu().launch();
				break;
			case "X":
				this.ic.launch();
				break;
			default:
					recoverSimu();
					break;
			}
		} else {
			dispSimuDetails(1);
			System.out.println("Est-ce bien votre simulation ? (oui, non, X)");
			readAns();
			switch (cAns) {
			case "oui":
				System.out.println("Vous allez commencer la reservation de la simulation precedente.");
				break;
			case "X":
				this.ic.launch();
				break;
			default :
				askIdSimu();	
				break;
			}
		}
	}

	public int askIdSimu(){
		System.out.println("Veuillez entrer votre numero de simulation : ");
		readAns();
		setIdSimu(Integer.parseInt(cAns));
		System.out.println("Simulation numero : "+idSimu);
		dispSimuDetails(1);
		System.out.println("Est-ce bien votre simulation ?");
		readAns();
		switch (cAns) {
		case "oui":
			System.out.println("Vous allez commencer la reservation de la simulation precedente.");
			break;
		default :
			askIdSimu();	
			break;
		}
		return 1;
	}
	
	public void createAccount() {
		query="INSERT INTO Client ( idClient, nomClient, typeClient, annee, typePaiement) "
				+"VALUES (ClientKey.nextval, ?, ?, ?, ?)";
		fields=new String[] {"nom", "type (individuel, societe, groupe)", "annee", "moyen de payement (carte, especes, cheque, bitcoin)"};
		types=new String[] {"char", "char", "int", "char"};
		parsedUpdateQuery(query, fields, types );
		
		query="SELECT  ClientKey.currval FROM Dual";
		sendQuery();
		idClient = Integer.parseInt(getLastLine());
		
		System.out.println("Merci d'avoir créé votre compte Client :");
		query="SELECT * FROM Client WHERE idClient="+idClient;
		sendQuery();
	}
	
	/**
	 * 
	 * @param opt 0 : ne fait rien.
	 */
	private void dispSimuDetails(int opt){
		System.out.println("Voici votre Simulation.");
		query = "SELECT * "
				+ "FROM Simulation s "
				+ "WHERE s.idSimulation='"+Integer.toString(idSimu)+"' ";
		
		System.out.println("IDSIMULATION \t DATEDEPARTSIMU \t DATEARRIVE \t NBPERSONNESSIMU");
		sendQuery();
		if (getLastLine().equals("")){
			System.out.println("Il n'y a aucune simulation existante.");
		}
	
		System.out.println("Votre simulation comprend :");
		query = "SELECT count(*) "
				+ "FROM ResaHotelSimulation "
				+ "WHERE idSimulation='"+Integer.toString(idSimu)+"' ";
		sendQuery();

		System.out.println("reservation(s) d'hotels");
		
		query = "SELECT count(*) "
				+ "FROM DateCircuitSimulation "
				+ "WHERE idSimulation='"+Integer.toString(idSimu)+"' ";
		sendQuery();
		System.out.println("reservation(s) de circuits.");
	}

	
	private void sendQuery() {
		sendQuery(1);
	}
	
	private void sendQuery(int affiche_requete){
		if (affiche_requete==0) {
			System.out.println(query);
		}
		simpleQuery(query);
	}
	
	public void setIdSimu(int idSimu){
		this.idSimu = idSimu;
	}

}
