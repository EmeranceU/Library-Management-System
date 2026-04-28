package services;

import exceptions.BookNotAvailableException;
import exceptions.BookNotFoundException;
import models.Book;
import models.Member;
import storage.FileManager;

import java.util.HashMap;
import java.util.Map;

public class LibraryService {

    private Map<Integer, Book> books;
    private final MemberService memberService;
    private final FileManager fileManager;

    public LibraryService(MemberService memberService, FileManager fileManager) {
        this.memberService = memberService;
        this.fileManager = fileManager;
        this.books = fileManager.loadBooks();
        fileManager.loadBorrowingRecords(books, memberService.getAllMembers());
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
        fileManager.saveBooks(books);
    }

    public Book findBook(int bookId) {
        if (!books.containsKey(bookId)) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        return books.get(bookId);
    }

    public Map<Integer, Book> getAvailableBooks() {
        Map<Integer, Book> available = new HashMap<>();
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            if (entry.getValue().isAvailable()) {
                available.put(entry.getKey(), entry.getValue());
            }
        }
        return available;
    }

    public void borrowBook(int bookId, int memberId) {
        Member member = memberService.getMember(memberId);
        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is already borrowed.");
        }

        book.setAvailable(false);
        book.setBorrowedBy(member);
        member.borrowBook(book);
        saveAll();
    }

    public void returnBook(int bookId, int memberId) {
        Member member = memberService.getMember(memberId);
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
        saveAll();
    }

    private void saveAll() {
        fileManager.saveBooks(books);
        fileManager.saveBorrowingRecords(memberService.getAllMembers());
    }
}
