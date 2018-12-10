
package library.database.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseHandlerMain {
	
	// Declaring the major Variables to be used throughout the class
	private static Connection conn = null;
	private static Statement stmt = null;

	// Method to make the Connection to the Oracle Database
	public static Connection CreateConnection () {
		
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","library","lib123");
			
			return conn;
			
		} catch ( Exception e ) {
			
			JOptionPane.showMessageDialog(null, "Cannot load Oracle Database", "Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		return null;
	}

}
