import models.Book;
import services.LibraryService;
import services.MemberService;
import storage.FileManager;
import ui.LibraryMenu;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        MemberService memberService = new MemberService(fileManager);
        LibraryService libraryService = new LibraryService(memberService, fileManager);

        seedBooks(libraryService);

        new LibraryMenu(libraryService, memberService).start();
    }

    private static void seedBooks(LibraryService libraryService) {
        if (!libraryService.getAvailableBooks().isEmpty()) return;
        libraryService.addBook(new Book(1, "Java Basics", "John Doe"));
        libraryService.addBook(new Book(2, "Clean Code", "Robert Martin"));
        libraryService.addBook(new Book(3, "Design Patterns", "Gang of Four"));
        libraryService.addBook(new Book(4, "The Pragmatic Programmer", "Andrew Hunt"));
        libraryService.addBook(new Book(5, "Effective Java", "Joshua Bloch"));
    }
}
