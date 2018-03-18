package ic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InterfaceClient extends InterfaceConsole{
	private InterfaceConsultation consult;
	private InterfaceResa resa;
	private InterfaceSimulation simu;
	
	public InterfaceClient() {
		consult = new InterfaceConsultation(this);
		resa = new InterfaceResa(this);
		simu = new InterfaceSimulation(this);
		System.out.println("                ____  __    __  __ __  __ __ ");
		System.out.println("               /    ||  |__|  ||  |  ||  |  |");
		System.out.println("              |  o  ||  |  |  ||  |  ||  |  |");
		System.out.println("              |     ||  |  |  ||  _  ||  ~  |");
		System.out.println("              |  _  ||  `  '  ||  |  ||___, |");
		System.out.println("              |  |  | \\      / |  |  ||     |");
		System.out.println("              |__|__|  \\_/\\_/  |__|__||____/ ");
		System.out.println("____________________________________________________");
		System.out.println("Bonjour, bienvenue dans l'application de reservation AWHY ");
		establishConnection();
	}
	
	public void establishConnection() {
		 try {
			    // Enregistrement du driver Oracle
			    //System.out.print("Loading Oracle driver... "); 
			    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		        //System.out.println("loaded");

			    // Etablissement de la connection
			    //System.out.print("Connecting to the database... "); 
			    conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
		        //System.out.println("connected");
		        
		        // Demarrage de la transaction (implicite dans notre cas)
		        conn.setAutoCommit(false);
		        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		  } catch (SQLException e) {
	            System.err.println("failed");
	            e.printStackTrace(System.err);
		  }
	}
	
	public InterfaceConsultation getConsult() {
		return consult;
	}

	public InterfaceResa getResa() {
		return resa;
	}

	public InterfaceSimulation getSimu() {
		return simu;
	}
	
	@Override
	public int launch(){
		menuPrincipal();
		System.out.println("Sortie de l'application.");
		close();
		return 0;
	}

	public void close() {
		try {
        conn.close();
		} catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
		}	
	}
	
	public void menuPrincipal(){
		System.out.println("___________________ Menu Principal ___________________");
		System.out.println("Pour consulter la brochure, ecrivez CONSULT, \n"
				+ "Pour effectuer une simulation de voyage, ecrivez SIMU, \n"
				+ "Enfin, pour effectuer une reservation, ecrivez RESA");
		String mode = in.nextLine();
		switch (mode) {
		case "CONSULT" :
			consult.launch();
			break;
		case "SIMU" : 
			simu.launch();
			break;
		case "RESA" :
			resa.launch();
			break;
		case "X":
			break;
		default :
			menuPrincipal();
		}
	}

}
