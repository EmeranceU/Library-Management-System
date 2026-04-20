public class Main {
    public static void main(String[] args) {

        Library library = new Library();

        // Create books
        Book book1 = new Book(1, "Java Basics", "John Doe");
        Book book2 = new Book(2, "OOP Concepts", "Jane Smith");

        // Add books
        library.addBook(book1);
        library.addBook(book2);

        // Create member
        Member member1 = new Member(1, "Alice");

        // Add member
        library.addMember(member1);

        // Borrow book
        library.borrowBook(1);

        // Return book
        library.returnBook(1);
    }
}
