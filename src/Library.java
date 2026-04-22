import java.util.HashMap;
import java.util.Map;

public class Library {

    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    public Member getMember(int memberId) {
        if (!members.containsKey(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found.");
        }
        return members.get(memberId);
    }

    private Book findBook(int bookId) {
        if (!books.containsKey(bookId)) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        return books.get(bookId);
    }

    public void borrowBook(int bookId, int memberId) {
        Member member = getMember(memberId);
        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is already borrowed.");
        }

        book.setAvailable(false);
        book.setBorrowedBy(member);
        member.borrowBook(book);

        System.out.println(member.getName() + " borrowed: " + book.getTitle());
    }

    public void returnBook(int bookId, int memberId) {
        Member member = getMember(memberId);
        Book book = findBook(bookId);

        if (book.isAvailable()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is not currently borrowed.");
        }

        if (!member.getBorrowedBooks().contains(book)) {
            throw new BookNotFoundException("Member " + member.getName() + " did not borrow this book.");
        }

        book.setAvailable(true);
        book.setBorrowedBy(null);
        member.returnBook(book);

        System.out.println(member.getName() + " returned: " + book.getTitle());
    }

    public void displayBooks() {
        for (Book book : books.values()) {
            String status = book.isAvailable()
                    ? "Available"
                    : "Borrowed by " + book.getBorrowedBy().getName();
            System.out.println("  [" + book.getId() + "] " + book.getTitle() +
                    " - " + book.getAuthor() + " (" + status + ")");
        }
    }

    public void displayMembers() {
        for (Member member : members.values()) {
            System.out.println("  [" + member.getId() + "] " + member.getName() +
                    " | Books borrowed: " + member.getBorrowedBooks().size());
        }
    }
}
