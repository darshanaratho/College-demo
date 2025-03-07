package lib;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO();
        StudentDAO studentDAO = new StudentDAO();

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. View Available Books");
            System.out.println("3. Add Student");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Generate Report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter book title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author name: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        bookDAO.addBook(title, author, quantity);
                        System.out.println("Book added successfully!");
                        break;

                    case 2:
                        List<String> books = bookDAO.getAvailableBooks();
                        books.forEach(System.out::println);
                        break;

                    case 3:
                        System.out.print("Enter student name: ");
                        String studentName = scanner.nextLine();
                        System.out.print("Enter student email: ");
                        String studentEmail = scanner.nextLine();
                        studentDAO.addStudent(studentName, studentEmail);
                        System.out.println("Student added successfully!");
                        break;

                    case 4:
                        System.out.print("Enter student email: ");
                        String email = scanner.nextLine();
                        int studentId = studentDAO.getStudentId(email);
                        System.out.print("Enter book ID to issue: ");
                        int bookId = scanner.nextInt();
                        bookDAO.issueBook(studentId, bookId);
                        System.out.println("Book issued successfully!");
                        break;

                    case 5:
                        System.out.print("Enter student email: ");
                        int returnStudentId = studentDAO.getStudentId(scanner.nextLine());
                        System.out.print("Enter book ID to return: ");
                        bookDAO.returnBook(returnStudentId, scanner.nextInt());
                        System.out.println("Book returned successfully!");
                        break;

                    case 6:
                        bookDAO.generateReport().forEach(System.out::println);
                        break;

                    case 7:
                        scanner.close();
                        return;
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

