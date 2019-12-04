package jdbcUI;

import java.sql.*;


public class JDBCManager {
	// Paramètres de connection
	String url ="jdbc:mariadb://localhost:3306/employees"; 	// URL de connexion (par défaut)
	String login = "root"; 									// user (par défaut)
	String password = "password"; 							// pwd (par défaut)
	String driverName = "org.mariadb.jdbc.Driver"; 			// driver (par défaut)
	Connection connection;
	Driver driver;
	
	// Gestion des commandes
	boolean connected = false;
	boolean autocommit = true;
	int nb_row_updated = 0;


	public JDBCManager() {
	}

/**
	* Initialisation de la connection à la base de données
*/
	public void connect(String url, String login, String password, String driverName) throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
		driver = DriverManager.getDriver(url);
		connection = DriverManager.getConnection(url, login, password);
		connected = true;
		System.out.println("Connecté à "+url);
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
		driver = DriverManager.getDriver(url);
		connection = DriverManager.getConnection(url, login, password);
		connected = true;
		System.out.println("Connecté à "+url);
	}

    public boolean isConnected() {
        return connected;
    }
    
    
    public ResultSet executeStatement(String sql) throws SQLException {
        Statement statement = null;
        ResultSet result = null;

        try {
			statement = connection.createStatement();
            if(statement.execute(sql)) { // true si retourne un ResultSet
				result = statement.getResultSet();
			} else nb_row_updated = statement.getUpdateCount();
        }
        catch(SQLException e) {
            throw e;
        }
        finally {
            if(statement != null) statement.close();
        }
        
        return result;
    }

	// Commit
	public void commit() {
		try {
			connection.commit();
			System.out.println("Commit r�ussi");
		}
		catch(SQLException e) {System.out.println("Erreur : "+e.getMessage());}
		finally{}
	}

	// Active ou desactive le mode autocommit
	public void autoCommit() {
		try {
			connection.setAutoCommit(! autocommit);
			autocommit = ! autocommit;
			System.out.println("Mode autocommit : "+autocommit);
		}
		catch(SQLException e) {System.out.println("Erreur : "+e.getMessage());}
		finally{}
	}

	// Rollback
	public void rollback() {
		try {
			connection.rollback();
			System.out.println("Rollback réussi");
		}
		catch(SQLException e) {System.out.println("Erreur : "+e.getMessage());}
		finally{}
	}

	public int getNbRowsUpdated(){
		return this.nb_row_updated;
	}

    // Quit
    public void quit() {
		try {
			connection.close();
		} catch (Exception e) {e.printStackTrace();}
	}

} 