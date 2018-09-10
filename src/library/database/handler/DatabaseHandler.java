
package library.database.handler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public final class DatabaseHandler {
	
	private static DatabaseHandler handler = null;
	
	// Declaring the major Variables to be used throughout the class
	private static Connection conn = null;
	private static Statement stmt = null;
	
	// Constructor
	private DatabaseHandler() {
		CreateConnection();
		setupBookTable();
		setupMemberTable();
	}
	
	public static DatabaseHandler getInstance() {
		if (handler == null)
			handler = new DatabaseHandler();
		
		return handler;
	}
	
	/*
	 * These methods are written to check whether the Specified table 
	 * exists of or not.
	 * 
	 * If it exists then the further code execution continues.
	 * 
	 * If it doesn't exist then it creates the table in the below 
	 * specified manner.
	 */
	private void setupBookTable() {
		String Table_Name = "Book";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbmetadata = conn.getMetaData();
			ResultSet tables = dbmetadata.getTables(null, null, Table_Name.toUpperCase(), null);
			
			if (tables.next())
				System.out.println("Table " + Table_Name + " Exists.");
			else {
				stmt.execute("CREATE TABLE " + Table_Name + "("
						+ "isbn varchar2(17) constraints book_isbn_pk primary key,\n"
						+ "title varchar2(50) not null,\n"
						+ "author varchar2(40) not null,\n"
						+ "edition_number varchar2(15) not null,\n"
						+ "publisher varchar2(40) not null,\n"
						+ "price number(5,0) not null,\n"
						+ "available char(1) default 'Y' check (available in ('Y','N'))"
						+ ")");
				}
		} catch (SQLException e) {
			e.printStackTrace();
			// REMOVE THIS BEFORE DEPLOYMENT
			System.err.println(e.getMessage() + " -- Database setup problem");
		}
	}
	
	private void setupMemberTable() {
		String Table_Name = "Member";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbmetadata = conn.getMetaData();
			ResultSet tables = dbmetadata.getTables(null, null, Table_Name.toUpperCase(), null);
			
			if (tables.next())
				System.out.println("Table " + Table_Name + " Exists.");
			else {
				stmt.execute("CREATE TABLE " + Table_Name + "("
						+ "id number(6,0) constraints member_id_pk primary key,\n"
						+ "fname varchar2(15) not null,\n"
						+ "lname varchar2(15) not null,\n"
						+ "city varchar2(20) not null,\n"
						+ "address varchar2(100) not null,\n"
						+ "mobile_no number(10,0) not null unique,\n"
						+ "email_id varchar2(50) not null unique"
						+ ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// REMOVE THIS BEFORE DEPLOYMENT
			System.err.println(e.getMessage() + " -- Database setup problem");
		}
	}

	// Method to make the Connection to the Oracle Database
	public static Connection CreateConnection() {
		
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","lib_demo","lib");
			
			return conn;
			
		} catch ( Exception e ) {
			
			JOptionPane.showMessageDialog(null, "Cannot load Oracle Database", "Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		return null;
	}
	
	// Method to execute a particular query passed as a String to this method
	public ResultSet exeQuery(String query) {
		ResultSet result;
		
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("Exception at exeQuery:DataHandler" + e.getLocalizedMessage());
			return null;
		}
		
		return result;
	}
	
	/*
	 * This method performs particular action given to it
	 * which include insert, select statements.
	 */
	public boolean exeAction(String qry) {
		try {
			stmt = conn.createStatement();
			stmt.execute(qry);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at exeAction:DataHandler" + e.getLocalizedMessage());
			return false;
		}
	}
}
