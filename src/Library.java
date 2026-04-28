import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Library {

    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private Map<Integer, Librarian> librarians = new HashMap<>();
    private List<TransactionRecord> transactions = new ArrayList<>();

    private static final String BOOKS_FILE = "books.txt";
    private static final String MEMBERS_FILE = "members.txt";
    private static final String LIBRARIANS_FILE = "librarians.txt";
    private static final String BORROWING_FILE = "borrowing.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Library() {
        loadAll();
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
        saveBooks();
    }

    public void removeBook(int bookId) {
        Book book = findBook(bookId);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is currently borrowed and cannot be removed.");
        }
        books.remove(bookId);
        saveBooks();
    }

    public Book findBook(int bookId) {
        if (!books.containsKey(bookId)) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        return books.get(bookId);
    }

    public Map<Integer, Book> getAllBooks() {
        return books;
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

    public Map<Integer, Book> getBorrowedBooks() {
        Map<Integer, Book> borrowed = new HashMap<>();
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            if (!entry.getValue().isAvailable()) {
                borrowed.put(entry.getKey(), entry.getValue());
            }
        }
        return borrowed;
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
        saveBooks();
        saveBorrowing();
        recordTransaction(member.getName(), book.getTitle(), "borrowed");
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
        saveBooks();
        saveBorrowing();
        recordTransaction(member.getName(), book.getTitle(), "returned");
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
        saveMembers();
    }

    public Member getMember(int memberId) {
        if (!members.containsKey(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found.");
        }
        return members.get(memberId);
    }

    public boolean memberExists(int memberId) {
        return members.containsKey(memberId);
    }

    public int nextMemberId() {
        return members.isEmpty() ? 1 : Collections.max(members.keySet()) + 1;
    }

    public Map<Integer, Member> getAllMembers() {
        return members;
    }

    public void addLibrarian(Librarian librarian) {
        librarians.put(librarian.getId(), librarian);
        saveLibrarians();
    }

    public Librarian getLibrarian(int librarianId) {
        if (!librarians.containsKey(librarianId)) {
            throw new MemberNotFoundException("Librarian with ID " + librarianId + " not found.");
        }
        return librarians.get(librarianId);
    }

    public boolean librarianExists(int librarianId) {
        return librarians.containsKey(librarianId);
    }

    public int nextLibrarianId() {
        return librarians.isEmpty() ? 1 : Collections.max(librarians.keySet()) + 1;
    }

    public List<TransactionRecord> getTransactions() {
        return transactions;
    }

    private void recordTransaction(String memberName, String bookTitle, String action) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        TransactionRecord record = new TransactionRecord(memberName, bookTitle, action, timestamp);
        transactions.add(record);
        saveTransactions();
    }

    private void loadAll() {
        loadBooks();
        loadMembers();
        loadLibrarians();
        loadBorrowing();
        loadTransactions();
    }

    private void saveBooks() {
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

    private void saveMembers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_FILE))) {
            for (Member member : members.values()) {
                writer.write(member.getId() + "," + member.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save members: " + e.getMessage());
        }
    }

    private void saveLibrarians() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LIBRARIANS_FILE))) {
            for (Librarian librarian : librarians.values()) {
                writer.write(librarian.getId() + "," + librarian.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save librarians: " + e.getMessage());
        }
    }

    private void saveBorrowing() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWING_FILE))) {
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

    private void saveTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (TransactionRecord record : transactions) {
                writer.write(record.getMemberName() + "," + record.getBookTitle() + ","
                        + record.getAction() + "," + record.getTimestamp());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save transactions: " + e.getMessage());
        }
    }

    private void loadBooks() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) return;
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
    }

    private void loadMembers() {
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) return;
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
    }

    private void loadLibrarians() {
        File file = new File(LIBRARIANS_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                librarians.put(id, new Librarian(id, name));
            }
        } catch (IOException e) {
            System.out.println("Failed to load librarians: " + e.getMessage());
        }
    }

    private void loadBorrowing() {
        File file = new File(BORROWING_FILE);
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

    private void loadTransactions() {
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;
                String memberName = parts[0].trim();
                String bookTitle = parts[1].trim();
                String action = parts[2].trim();
                String timestamp = parts[3].trim();
                transactions.add(new TransactionRecord(memberName, bookTitle, action, timestamp));
            }
        } catch (IOException e) {
            System.out.println("Failed to load transactions: " + e.getMessage());
        }
    }
}
