package models;

public class Librarian extends Person {

    public Librarian(int id, String name) {
        super(id, name);
    }

    @Override
    public void displayInfo() {
        System.out.println("Librarian: " + name);
    }
}
