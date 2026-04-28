import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedBooks();

        System.out.println("=== Library Management System ===");
        System.out.println();

        boolean running = true;
        while (running) {
            System.out.println("--- Select Role ---");
            System.out.println("1. Member");
            System.out.println("2. Librarian");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();
            System.out.println();

            switch (choice) {
                case 1:
                    memberSession();
                    break;
                case 2:
                    librarianSession();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }

        System.out.println("System closed.");
        scanner.close();
    }

    private static void memberSession() {
        Member member = memberLogin();
        if (member == null) return;

        boolean running = true;
        while (running) {
            System.out.println("--- Member Menu ---");
            System.out.println("1. View available books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. View my borrowed books");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();
            System.out.println();

            switch (choice) {
                case 1:
                    viewAvailableBooks();
                    break;
                case 2:
                    borrowBook(member);
                    break;
                case 3:
                    returnBook(member);
                    break;
                case 4:
                    viewMyBooks(member);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye, " + member.getName() + ".");
    }

    private static Member memberLogin() {
        System.out.print("Enter your Member ID (or 0 to register): ");
        int id = readInt();

        if (id == 0) {
            return memberRegister();
        }

        if (library.memberExists(id)) {
            Member member = library.getMember(id);
            System.out.println("Welcome back, " + member.getName() + ".");
            System.out.println();
            return member;
        }

        System.out.println("Member ID not found.");
        return memberLogin();
    }

    private static Member memberRegister() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        int newId = library.nextMemberId();
        Member member = new Member(newId, name);
        library.addMember(member);

        System.out.println("Registration successful. Your Member ID is: " + newId);
        System.out.println("Welcome, " + name + ".");
        System.out.println();
        return member;
    }

    private static void viewAvailableBooks() {
        Map<Integer, Book> available = library.getAvailableBooks();
        if (available.isEmpty()) {
            System.out.println("No books are currently available.");
            return;
        }
        System.out.println("Available books:");
        for (Book book : available.values()) {
            System.out.println("  [" + book.getId() + "] " + book.getTitle() + " - " + book.getAuthor());
        }
    }

    private static void borrowBook(Member member) {
        viewAvailableBooks();
        if (library.getAvailableBooks().isEmpty()) return;

        System.out.print("Enter Book ID to borrow: ");
        int bookId = readInt();

        try {
            library.borrowBook(bookId, member.getId());
            System.out.println("You have successfully borrowed the book.");
        } catch (BookNotFoundException | BookNotAvailableException | MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnBook(Member member) {
        if (member.getBorrowedBooks().isEmpty()) {
            System.out.println("You have no books to return.");
            return;
        }

        System.out.println("Your borrowed books:");
        for (Book book : member.getBorrowedBooks()) {
            System.out.println("  [" + book.getId() + "] " + book.getTitle());
        }

        System.out.print("Enter Book ID to return: ");
        int bookId = readInt();

        try {
            library.returnBook(bookId, member.getId());
            System.out.println("Book returned successfully.");
        } catch (BookNotFoundException | BookNotAvailableException | MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewMyBooks(Member member) {
        if (member.getBorrowedBooks().isEmpty()) {
            System.out.println("You have not borrowed any books.");
            return;
        }
        System.out.println("Your borrowed books:");
        for (Book book : member.getBorrowedBooks()) {
            System.out.println("  [" + book.getId() + "] " + book.getTitle() + " - " + book.getAuthor());
        }
    }

    private static void librarianSession() {
        Librarian librarian = librarianLogin();
        if (librarian == null) return;

        boolean running = true;
        while (running) {
            System.out.println("--- Librarian Menu ---");
            System.out.println("1. Add a book");
            System.out.println("2. Remove a book");
            System.out.println("3. View all books");
            System.out.println("4. View borrowed books");
            System.out.println("5. View all members");
            System.out.println("6. View transaction history");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();
            System.out.println();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    viewAllBooks();
                    break;
                case 4:
                    viewBorrowedBooks();
                    break;
                case 5:
                    viewAllMembers();
                    break;
                case 6:
                    viewTransactionHistory();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye, " + librarian.getName() + ".");
    }

    private static Librarian librarianLogin() {
        System.out.print("Enter your Librarian ID (or 0 to register): ");
        int id = readInt();

        if (id == 0) {
            return librarianRegister();
        }

        if (library.librarianExists(id)) {
            Librarian librarian = library.getLibrarian(id);
            System.out.println("Welcome back, " + librarian.getName() + ".");
            System.out.println();
            return librarian;
        }

        System.out.println("Librarian ID not found.");
        return librarianLogin();
    }

    private static Librarian librarianRegister() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        int newId = library.nextLibrarianId();
        Librarian librarian = new Librarian(newId, name);
        library.addLibrarian(librarian);

        System.out.println("Registration successful. Your Librarian ID is: " + newId);
        System.out.println("Welcome, " + name + ".");
        System.out.println();
        return librarian;
    }

    private static void addBook() {
        System.out.print("Enter Book ID: ");
        int id = readInt();
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter author: ");
        String author = scanner.nextLine().trim();

        try {
            library.findBook(id);
            System.out.println("A book with ID " + id + " already exists.");
        } catch (BookNotFoundException e) {
            library.addBook(new Book(id, title, author));
            System.out.println("Book added successfully.");
        }
    }

    private static void removeBook() {
        viewAllBooks();
        if (library.getAllBooks().isEmpty()) return;

        System.out.print("Enter Book ID to remove: ");
        int bookId = readInt();

        try {
            library.removeBook(bookId);
            System.out.println("Book removed successfully.");
        } catch (BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllBooks() {
        Map<Integer, Book> books = library.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in the system.");
            return;
        }
        System.out.println("All books:");
        for (Book book : books.values()) {
            String status = book.isAvailable()
                    ? "Available"
                    : "Borrowed by " + book.getBorrowedBy().getName();
            System.out.println("  [" + book.getId() + "] " + book.getTitle()
                    + " - " + book.getAuthor() + " (" + status + ")");
        }
    }

    private static void viewBorrowedBooks() {
        Map<Integer, Book> borrowed = library.getBorrowedBooks();
        if (borrowed.isEmpty()) {
            System.out.println("No books are currently borrowed.");
            return;
        }
        System.out.println("Borrowed books:");
        for (Book book : borrowed.values()) {
            System.out.println("  [" + book.getId() + "] " + book.getTitle()
                    + " - borrowed by " + book.getBorrowedBy().getName()
                    + " (Member ID: " + book.getBorrowedBy().getId() + ")");
        }
    }

    private static void viewAllMembers() {
        Map<Integer, Member> members = library.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        System.out.println("Registered members:");
        for (Member member : members.values()) {
            System.out.println("  [" + member.getId() + "] " + member.getName()
                    + " | Books borrowed: " + member.getBorrowedBooks().size());
        }
    }

    private static void viewTransactionHistory() {
        if (library.getTransactions().isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        System.out.println("Transaction history:");
        for (TransactionRecord record : library.getTransactions()) {
            System.out.println("  [" + record.getTimestamp() + "] "
                    + record.getMemberName() + " " + record.getAction()
                    + " " + record.getBookTitle());
        }
    }

    private static void seedBooks() {
        if (!library.getAllBooks().isEmpty()) return;
        library.addBook(new Book(1, "Java Basics", "John Doe"));
        library.addBook(new Book(2, "Clean Code", "Robert Martin"));
        library.addBook(new Book(3, "Design Patterns", "Gang of Four"));
        library.addBook(new Book(4, "The Pragmatic Programmer", "Andrew Hunt"));
        library.addBook(new Book(5, "Effective Java", "Joshua Bloch"));
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
