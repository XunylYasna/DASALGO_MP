package MP;

public class Entry {
    String postOffice;
    String from;
    String goingto;
    float distance;
    boolean twoway;

    public Entry(String postOffice, String from, String goingto, float distance) {
        this.postOffice = postOffice;
        this.from = from;
        this.goingto = goingto;
        this.distance = distance;
        this.twoway = true;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public String getFrom() {
        return from;
    }

    public String getGoingto() {
        return goingto;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isTwoway() {
        return twoway;
    }
}
