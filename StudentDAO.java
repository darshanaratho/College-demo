package lib;

import java.sql.*;

public class StudentDAO {
    public void addStudent(String name, String email) throws SQLException {
        String query = "INSERT INTO students (name, email) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, email);
            pst.executeUpdate();
        }
    }

    public int getStudentId(String email) throws SQLException {
        String query = "SELECT id FROM students WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }
}

