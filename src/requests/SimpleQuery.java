package requests;

import java.sql.*;


public class SimpleQuery {
	private static String lastLine = "";
    static final String CONN_URL = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
    static final String USER = "arbelott";
    static final String PASSWD = "arbelott";

    /**
     * 
     * @param PRE_STMT requete a executer.
     * @param mode vaut 0 si on fait une interrogation, vaut 1 si on fait une update.
     */
    public SimpleQuery(String PRE_STMT) {
        try {
	    // Enregistrement du driver Oracle
	    //System.out.print("Loading Oracle driver... "); 
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //System.out.println("loaded");

	    // Etablissement de la connection
	    //System.out.print("Connecting to the database... "); 
	    Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            //System.out.println("connected");

	    // Creation de la requete
            PreparedStatement stmt = conn.prepareStatement(PRE_STMT);
	    
    
	        // Execution de la requete
            ResultSet rset = stmt.executeQuery();
			    // Affichage du resultat
            //System.out.println("Results:");
	            dumpResultSet(rset);
	            //System.out.println("");
         // Fermeture 
	    	    rset.close();
	    
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
    }

    private void dumpResultSet(ResultSet rset) throws SQLException {
        ResultSetMetaData rsetmd = rset.getMetaData();
        int i = rsetmd.getColumnCount();
        while (rset.next()) {
            lastLine = "";
        		for (int j = 1; j <= i; j++) {
                System.out.print(rset.getString(j) + "\t");
                lastLine = lastLine+rset.getString(j);
            }
	    System.out.println();
        }
    }
    
    public static String getLastLine() {
    		return lastLine;
    }

    public static void main(String args[]) {
        new SimpleQuery("select * from emp");
    }
}
