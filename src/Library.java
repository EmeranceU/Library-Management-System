import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();

    // Add book
    public void addBook(Book book) {
        books.add(book);
    }

    // Add member
    public void addMember(Member member) {
        members.add(member);
    }

    // Borrow book
    public void borrowBook(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId && book.isAvailable()) {
                book.setAvailable(false);
                System.out.println("Book borrowed: " + book.getTitle());
                return;
            }
        }
        System.out.println("Book not available.");
    }

    // Return book
    public void returnBook(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                book.setAvailable(true);
                System.out.println("Book returned: " + book.getTitle());
                return;
            }
        }
    }
}
