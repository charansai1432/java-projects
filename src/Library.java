import java.sql.*;

public class Library {

    // Add a new book
    public void addBook(Book book) {
        String query = "INSERT INTO books (id, title, author, isIssued) VALUES (?, ?, ?, false)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.executeUpdate();
            System.out.println(" Book added to database.");
        } catch (SQLException e) {
            System.out.println("❌ Error adding book: " + e.getMessage());
        }
    }

    // Add a new member
    public void addMember(Member member) {
        String query = "INSERT INTO members (id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, member.getId());
            stmt.setString(2, member.getName());
            stmt.executeUpdate();
            System.out.println("✅ Member added to database.");
        } catch (SQLException e) {
            System.out.println("❌ Error adding member: " + e.getMessage());
        }
    }

    // Issue a book
    public void issueBook(int bookId) {
        String checkQuery = "SELECT isIssued FROM books WHERE id=?";
        String updateQuery = "UPDATE books SET isIssued=true WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("isIssued")) {
                    System.out.println("❌ Book already issued.");
                } else {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, bookId);
                        updateStmt.executeUpdate();
                        System.out.println("📕 Book issued successfully.");
                    }
                }
            } else {
                System.out.println("❌ Book not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error issuing book: " + e.getMessage());
        }
    }

    // Return a book
    public void returnBook(int bookId) {
        String checkQuery = "SELECT isIssued FROM books WHERE id=?";
        String updateQuery = "UPDATE books SET isIssued=false WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("isIssued")) {
                    System.out.println("❌ Book was not issued.");
                } else {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, bookId);
                        updateStmt.executeUpdate();
                        System.out.println("📗 Book returned successfully.");
                    }
                }
            } else {
                System.out.println("❌ Book not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error returning book: " + e.getMessage());
        }
    }

    // Display all books
    public void displayBooks() {
        String query = "SELECT * FROM books";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " - " +
                    rs.getString("title") + " by " +
                    rs.getString("author") +
                    " [" + (rs.getBoolean("isIssued") ? "Issued" : "Available") + "]"
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying books: " + e.getMessage());
        }
    }

    // Display all members
    public void displayMembers() {
        String query = "SELECT * FROM members";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying members: " + e.getMessage());
        }
    }
}
