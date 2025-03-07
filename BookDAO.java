package lib;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(String title, String author, int quantity) throws SQLException {
        String query = "INSERT INTO books (title, author, quantity, available) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, title);
            pst.setString(2, author);
            pst.setInt(3, quantity);
            pst.setInt(4, quantity);
            pst.executeUpdate();
        }
    }

    public List<String> getAvailableBooks() throws SQLException {
        List<String> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE available > 0";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(rs.getInt("id") + ". " + rs.getString("title") + " by " + rs.getString("author"));
            }
        }
        return books;
    }

    public void issueBook(int studentId, int bookId) throws SQLException {
        String updateQuery = "UPDATE books SET available = available - 1 WHERE id = ?";
        String insertQuery = "INSERT INTO issued_books (student_id, book_id, issue_date) VALUES (?, ?, CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement updateStmt = con.prepareStatement(updateQuery);
             PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

            updateStmt.setInt(1, bookId);
            insertStmt.setInt(1, studentId);
            insertStmt.setInt(2, bookId);

            updateStmt.executeUpdate();
            insertStmt.executeUpdate();
        }
    }

    public void returnBook(int studentId, int bookId) throws SQLException {
        String updateQuery = "UPDATE books SET available = available + 1 WHERE id = ?";
        String returnQuery = "UPDATE issued_books SET return_date = CURDATE() WHERE student_id = ? AND book_id = ? AND return_date IS NULL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement updateStmt = con.prepareStatement(updateQuery);
             PreparedStatement returnStmt = con.prepareStatement(returnQuery)) {

            updateStmt.setInt(1, bookId);
            returnStmt.setInt(1, studentId);
            returnStmt.setInt(2, bookId);

            updateStmt.executeUpdate();
            returnStmt.executeUpdate();
        }
    }

    public List<String> generateReport() throws SQLException {
        List<String> reports = new ArrayList<>();
        String query = "SELECT s.name, b.title, i.issue_date, i.return_date " +
                       "FROM issued_books i " +
                       "JOIN students s ON i.student_id = s.id " +
                       "JOIN books b ON i.book_id = b.id";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(rs.getString("name") + " borrowed '" + rs.getString("title") +
                        "' on " + rs.getDate("issue_date") +
                        ", returned: " + (rs.getDate("return_date") != null ? rs.getDate("return_date") : "Not returned"));
            }
        }
        return reports;
    }
}
