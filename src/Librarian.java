public class Librarian extends Person {

    public Librarian(int id, String name) {
        super(id, name);
    }

    public void displayInfo() {
        System.out.println("Librarian: " + name);
    }
}
