package library.database.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import library.data.model.Book;
import library.ui.listMember.ListMemberController.Member;

public class DatabaseHelper {

	public static boolean insertNewBook(Book book) {
		try {
			PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
					"INSERT INTO BOOK (ISBN, TITLE, AUTHOR, EDITION_NUMBER, PUBLISHER, PRICE, AVAILABLE) \n"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
			statement.setString(1, book.getIsbn());
			statement.setString(2, book.getTitle());
			statement.setString(3, book.getAuthor());
			statement.setString(4, book.getEdition_number());
			statement.setString(5, book.getPublisher());
			statement.setString(6, book.getPrice());
			statement.setBoolean(7, book.getAvailable());
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean insertNewMember(String name, String city, String address, String mobile, String email) {
		try {
			PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
					"INSERT INTO MEMBER (NAME, CITY, ADDRESS, MOBILE_NO, EMAIL_ID) \n"
					+ "VALUES (?, ?, ?, ?, ?)");
			statement.setString(1, name);
			statement.setString(2, city);
			statement.setString(3, address);
			statement.setString(4, mobile);
			statement.setString(5, email);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean checkBookExistence(String isbn) {
		try {
			String checkstmt = "SELECT COUNT(*) FROM BOOK WHERE ISBN = ?";
			PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean checkMemberExistence(Integer id) {
		try {
			String checkstmt = "SELECT COUNT(*) FROM MEMBER WHERE ID = ?";
			PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static ResultSet getBookInfoWithIssueData(String isbn) {
		try {
			String query = "SELECT\n"
					+ "BOOK.TITLE, BOOK.AUTHOR, BOOK.PUBLISHER, ISSUE.ISSUE_TIME\n"
					+ "FROM BOOK\n"
					+ "LEFT JOIN ISSUE ON\n"
					+ "BOOK.ISBN = ISSUE.ISBN\n"
					+ "WHERE BOOK.ISBN = ?";
			PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(query);
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void wipeTable(String tableName) {
		try {
			Statement statement = DatabaseHandler.getInstance().getConnection().createStatement();
			statement.execute("DELETE FROM " + tableName + " WHERE TRUE");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
