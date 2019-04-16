public class Request {
    private int from;
    private int to;
    private int id;

    public Request(int f, int t, int i) {
        from = f;
        to = t;
        id = i;
    }

    public int getFromFloor() {
        return from;
    }

    public int getToFloor() {
        return to;
    }

    public int getPersonId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (id == ((Request) obj).getPersonId());
    }
}
