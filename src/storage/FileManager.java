package storage;

import models.Book;
import models.Member;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private static final String BOOKS_FILE = "books.txt";
    private static final String MEMBERS_FILE = "members.txt";
    private static final String BORROW_FILE = "borrowing.txt";

    public void saveBooks(Map<Integer, Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books.values()) {
                int borrowedById = book.getBorrowedBy() != null ? book.getBorrowedBy().getId() : -1;
                writer.write(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + ","
                        + book.isAvailable() + "," + borrowedById);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save books: " + e.getMessage());
        }
    }

    public void saveMembers(Map<Integer, Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_FILE))) {
            for (Member member : members.values()) {
                writer.write(member.getId() + "," + member.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save members: " + e.getMessage());
        }
    }

    public void saveBorrowingRecords(Map<Integer, Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROW_FILE))) {
            for (Member member : members.values()) {
                for (Book book : member.getBorrowedBooks()) {
                    writer.write(book.getId() + "," + member.getId());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to save borrowing records: " + e.getMessage());
        }
    }

    public Map<Integer, Book> loadBooks() {
        Map<Integer, Book> books = new HashMap<>();
        File file = new File(BOOKS_FILE);
        if (!file.exists()) return books;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;
                int id = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                String author = parts[2].trim();
                boolean available = Boolean.parseBoolean(parts[3].trim());
                Book book = new Book(id, title, author);
                book.setAvailable(available);
                books.put(id, book);
            }
        } catch (IOException e) {
            System.out.println("Failed to load books: " + e.getMessage());
        }
        return books;
    }

    public Map<Integer, Member> loadMembers() {
        Map<Integer, Member> members = new HashMap<>();
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) return members;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                members.put(id, new Member(id, name));
            }
        } catch (IOException e) {
            System.out.println("Failed to load members: " + e.getMessage());
        }
        return members;
    }

    public void loadBorrowingRecords(Map<Integer, Book> books, Map<Integer, Member> members) {
        File file = new File(BORROW_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                int bookId = Integer.parseInt(parts[0].trim());
                int memberId = Integer.parseInt(parts[1].trim());
                Book book = books.get(bookId);
                Member member = members.get(memberId);
                if (book != null && member != null) {
                    book.setBorrowedBy(member);
                    book.setAvailable(false);
                    member.borrowBook(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load borrowing records: " + e.getMessage());
        }
    }
}
