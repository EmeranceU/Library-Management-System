public class Main {
    public static void main(String[] args) {

        Library library = new Library();

        Member m1 = new Member(1, "Alice");
        library.addMember(m1);

        Book b1 = new Book(1, "Java Basics", "John Doe");
        library.addBook(b1);

        Person[] people = {
                new Member(1, "Alice"),
                new Librarian(2, "Bob")
        };

        for (Person p : people) {
            p.displayInfo();
        }

        try {
            library.borrowBook(1, m1);
            library.borrowBook(1, m1);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        try {
            library.returnBook(2);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        library.displayBooks();
    }
}