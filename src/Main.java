public class Main {
    public static void main(String[] args) {

        Library library = new Library();

        Member m1 = new Member(1, "Alice");
        Member m2 = new Member(2, "Charlie");
        Librarian l1 = new Librarian(3, "Bob");

        library.addMember(m1);
        library.addMember(m2);

        library.addBook(new Book(1, "Java Basics", "John Doe"));
        library.addBook(new Book(2, "Clean Code", "Robert Martin"));
        library.addBook(new Book(3, "Design Patterns", "Gang of Four"));

        System.out.println("=== Library Management System ===");
        System.out.println();

        Person[] people = { m1, m2, l1 };
        for (Person p : people) {
            p.displayInfo();
        }
        System.out.println();

        try {
            library.borrowBook(1, 1);
            library.borrowBook(2, 1);
            library.borrowBook(3, 2);
            library.borrowBook(1, 2);
        } catch (BookNotAvailableException | BookNotFoundException | MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();

        try {
            library.returnBook(1, 1);
            library.borrowBook(1, 2);
        } catch (BookNotAvailableException | BookNotFoundException | MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- Final State ---");
        library.displayBooks();
        System.out.println();
        library.displayMembers();
    }
}
