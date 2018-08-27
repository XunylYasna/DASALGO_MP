package MP;

public class Vertice {
    int id;
    String Address;

    public Vertice(int id, String address) {
        this.id = id;
        Address = address;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return Address;
    }
}
