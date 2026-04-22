import java.util.ArrayList;
import java.util.List;

public class Member extends Person {

    private List<Book> borrowedBooks = new ArrayList<>();

    public Member(int id, String name) {
        super(id, name);
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public void displayInfo() {
        System.out.println("Member: " + name);
    }
}