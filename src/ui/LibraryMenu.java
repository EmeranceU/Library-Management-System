package ui;

import exceptions.BookNotAvailableException;
import exceptions.BookNotFoundException;
import exceptions.MemberNotFoundException;
import models.Book;
import models.Member;
import services.LibraryService;
import services.MemberService;

import java.util.Map;
import java.util.Scanner;

public class LibraryMenu {

    private final LibraryService libraryService;
    private final MemberService memberService;
    private final Scanner scanner;

    public LibraryMenu(LibraryService libraryService, MemberService memberService) {
        this.libraryService = libraryService;
        this.memberService = memberService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Library Management System ===");
        System.out.println();

        Member member = login();
        showMenu(member);

        scanner.close();
    }

    private Member login() {
        System.out.print("Enter your Member ID (or 0 to register): ");
        int id = readInt();

        if (id == 0) {
            return register();
        }

        if (memberService.memberExists(id)) {
            Member member = memberService.getMember(id);
            System.out.println("Welcome back, " + member.getName() + ".");
            System.out.println();
            return member;
        }

        System.out.println("Member ID not found.");
        return login();
    }

    private Member register() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        int newId = memberService.nextMemberId();
        Member member = new Member(newId, name);
        memberService.addMember(member);

        System.out.println("Registration successful. Your Member ID is: " + newId);
        System.out.println("Welcome, " + name + ".");
        System.out.println();
        return member;
    }

    private void showMenu(Member member) {
        boolean running = true;
        while (running) {
            System.out.println("--- Menu ---");
            System.out.println("1. View available books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. View my borrowed books");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();
            System.out.println();

            switch (choice) {
                case 1 -> viewAvailableBooks();
                case 2 -> borrowBook(member);
                case 3 -> returnBook(member);
                case 4 -> viewBorrowedBooks(member);
                case 5 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye, " + member.getName() + ".");
    }

    private void viewAvailableBooks() {
        Map<Integer, Book> available = libraryService.getAvailableBooks();
        if (available.isEmpty()) {
            System.out.println("No books are currently available.");
            return;
        }
        System.out.println("Available books:");
        for (Book book : available.values()) {
            System.out.println("  [" + book.getId() + "] " + book.getTitle() + " - " + book.getAuthor());
        }
    }

    private void borrowBook(Member member) {
        viewAvailableBooks();
        if (libraryService.getAvailableBooks().isEmpty()) return;

        System.out.print("Enter Book ID to borrow: ");
        int bookId = readInt();

        try {
            libraryService.borrowBook(bookId, member.getId());
            System.out.println("You have successfully borrowed the book.");
        } catch (BookNotFoundException | BookNotAvailableException | MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnBook(Member member) {
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
            libraryService.returnBook(bookId, member.getId());
            System.out.println("Book returned successfully.");
        } catch (BookNotFoundException | BookNotAvailableException | MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewBorrowedBooks(Member member) {
        if (member.getBorrowedBooks().isEmpty()) {
            System.out.println("You have not borrowed any books.");
            return;
        }
        System.out.println("Your borrowed books:");
        for (Book book : member.getBorrowedBooks()) {
            System.out.println("  [" + book.getId() + "] " + book.getTitle() + " - " + book.getAuthor());
        }
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
