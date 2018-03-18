package ic;

import java.util.LinkedList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class InterfaceConsole {
	protected static Scanner in;
    protected static final String CONN_URL = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
    protected static final String USER = "lucasna";
    protected static final String PASSWD = "lucasna";
	protected static Connection conn;
	protected static PreparedStatement stmt;
	protected static String lastLine="";
	protected static String cAns;
	
	public InterfaceConsole(){
		 in = new Scanner(System.in);
		 this.cAns = null;
	}
	
	/**
	 * Cette fonction doit lancer l'inteface.
	 * @return
	 */
	public abstract int launch();
	
	public static String readAns(){
		cAns = in.nextLine();
		return cAns;
	}
	/**
	 * Prepare la requete, la rempli avec lutilisateur et l'envoie.
	 * @param query requete du type insert ou update.. avec des ?
	 * @param fields nom des ? a demander a lutilisateur
	 * @param types type des ?
	 */
	public static void parsedUpdateQuery(String query, String[] fields, String[] types) {
		int N=fields.length;
		try {
			stmt=conn.prepareStatement(query);
			
			for (int i=1; i<=N; i++) {
				System.out.println(fields[i-1]+" :");
				switch (types[i-1]) {
					case "char":
						stmt.setString(i, readAns());
						break;
					case "int":
						stmt.setInt(i, Integer.parseInt(readAns()));
						break;
					default:		
						break;
				}
			}
			stmt.executeQuery();
			conn.commit();
	     } catch (SQLException e) {
	            System.err.println(" Les valeurs entrees au clavier ne sont pas valides.");
	            e.printStackTrace(System.err);
	            System.out.println("Voulez-vous reesayer ? (oui, non)");
	            readAns();
	            switch (cAns) {
	            case "oui":
		            parsedUpdateQuery(query, fields, types);
		            break;
	            case "non":
	            	default :
	            		break;
	            }
	     }
	}
	
	public static void simpleQuery(String query) {
		try {
		// Creation de la requete
        stmt = conn.prepareStatement(query);
        // Execution de la requete
        ResultSet rset = stmt.executeQuery();
		    // Affichage du resultat
        //System.out.println("Results:");
            dumpResultSet(rset);
            //System.out.println("");
            // Fermeture 
    	    rset.close();
		} catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
	}
	
	public static LinkedList<String> queryToList(String query) {
		try {
		// Creation de la requete
        stmt = conn.prepareStatement(query);
        // Execution de la requete
        ResultSet rset = stmt.executeQuery();
		    // Affichage du resultat
        //System.out.println("Results:");
         LinkedList<String> list = dumpResultList(rset);
            //System.out.println("");
            // Fermeture 
    	    rset.close();
    	    return list;
		} catch (SQLException e) {
            System.err.println("query  failed");
            e.printStackTrace(System.err);
            return null;
        }
	}
	
	public static void simpleUpdate(String query) {
		try {
		// Creation de la requete
        stmt = conn.prepareStatement(query);
        stmt.executeUpdate(query);
        
		} catch (SQLException e) {
            System.err.println("insert/delete failed");
            e.printStackTrace(System.err);
        }
	}
	
    protected static void dumpResultSet(ResultSet rset) throws SQLException {
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
    
    protected static LinkedList<String> dumpResultList(ResultSet rset) throws SQLException {
    		LinkedList<String> list = new LinkedList<String>();
    		ResultSetMetaData rsetmd = rset.getMetaData();
        int i = rsetmd.getColumnCount();
        while (rset.next()) {
        		lastLine="";
        		for (int j = 1; j <= i; j++) {
        			lastLine += rset.getString(j) + " ";
            }
        		list.add(lastLine);
        }
        return list;
    }
    
    public static String getLastLine() {
		return lastLine;
    }	

    public static void commit(){
    	try {
    		conn.commit();
    	} catch (SQLException e) {
 	            System.err.println(" fetch failed");
 	            e.printStackTrace(System.err);
    	}
    }
 
    public static void goDown(int i) {
    		for (int k=0; k<i; k++) {
    			System.out.println("");
    		}
    }
}
