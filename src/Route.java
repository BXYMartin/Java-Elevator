import java.util.HashSet;

public class Route {
    private HashSet<Character> elevator = new HashSet<>();
    private int cost;

    public Route() {
        cost = Integer.MAX_VALUE;
    }

    public HashSet<Character> elevator() {
        return elevator;
    }

    public int cost() {
        return cost;
    }

    public void setParam(HashSet<Character> e, int c) {
        cost = c;
        elevator = e;
    }

}
