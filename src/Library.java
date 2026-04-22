import java.util.ArrayList;

public class Library {

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    private Book findBook(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        throw new BookNotFoundException("Book not found");
    }

    public void borrowBook(int bookId, Member member) {
        if (member == null) {
            throw new MemberNotFoundException("Member not found");
        }

        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is already borrowed");
        }

        book.setAvailable(false);
        book.setBorrowedBy(member);
        System.out.println(member.getName() + " borrowed: " + book.getTitle());
    }

    public void borrowBook(int bookId) {
        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is already borrowed");
        }

        book.setAvailable(false);
        System.out.println("Book borrowed: " + book.getTitle());
    }

    public void returnBook(int bookId) {
        Book book = findBook(bookId);

        book.setAvailable(true);
        book.setBorrowedBy(null);
        System.out.println("Book returned: " + book.getTitle());
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book.getId() + " - " + book.getTitle() +
                    " (" + (book.isAvailable() ? "Available" : "Borrowed") + ")");
        }
    }
}