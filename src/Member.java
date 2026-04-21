public class Member extends Person {

    public Member(int id, String name) {
        super(id, name);
    }
    public String getName() {
        return name;
    }

    @Override
    public void displayInfo() {
        System.out.println("Member: " + name);
    }
}