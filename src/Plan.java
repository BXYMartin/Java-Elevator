import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import static java.lang.Math.abs;

public class Plan {
    private HashMap<Integer, Vector<Integer>> floorArrival = new HashMap<>();
    private HashMap<Integer, Vector<Integer>> floorBoard = new HashMap<>();
    private LinkedList<Integer> plan = new LinkedList<>();
    private HashMap<Integer, Request> back = new HashMap<>();
    private int on = 0;
    private int index = 0;

    public void off() {
        on--;
    }

    public int current() {
        return on;
    }

    public Request request(int id) {
        return back.get(id);
    }

    public void put(Request p) {
        if (p != null) {
            back.put(p.getPersonId(), p);
        }
    }

    public void next() {
        index++;
    }

    public Plan(Dispatch dispatch) {
        int src = dispatch.getMain().getFromFloor();
        int dst = dispatch.getMain().getToFloor();
        put(dispatch.getBack());
        plan.add(src);
        plan.add(dst);
        Vector<Integer> out = new Vector<>();
        int num = dispatch.getMain().getPersonId();
        out.add(num);
        floorArrival.put(dst, out);
        Vector<Integer> in = new Vector<>();
        in.add(num);
        floorBoard.put(src, in);
        on++;
    }

    private void insert(HashMap<Integer, Vector<Integer>> target
            , int floor, int id) {
        if (target.get(floor) == null) {
            Vector<Integer> passenger = new Vector<>();
            passenger.add(id);
            target.put(floor, passenger);
        } else {
            target.get(floor).add(id);
        }
    }

    public void insertRoute(int src, int dst, int num) {
        if (!plan.contains(src)) {
            plan.add(src);
        }
        if (!plan.contains(dst)) {
            plan.add(dst);
        }
        plan.sort(new Comparator<Integer>() {
            public int compare(Integer one, Integer two) {
                return (two - one) * (dst - src) * -1;
            }
        }
        );
        insert(floorArrival, dst, num);
        insert(floorBoard, src, num);
        on++;
    }

    public int get() {
        return plan.get(index);
    }

    public int getLast() {
        return plan.getLast();
    }

    public int eta(char e) {
        int f = 0;
        switch (e) {
            case 'A':
                f += 400 * abs(getLast() - get());
                break;
            case 'B':
                f += 500 * abs(getLast() - get());
                break;
            case 'C':
                f += 600 * abs(getLast() - get());
                break;
            default:
                return 0;
        }
        for (Integer i:plan) {
            if ((direction() > 0 && i > get()) ||
                    (direction() < 0 && i < get())) {
                f += 400;
            }
        }
        return f;
    }

    public Vector<Integer> getOutId() {
        return floorArrival.get(plan.get(index));
    }

    public Vector<Integer> getInId() {
        return floorBoard.get(plan.get(index));
    }

    public boolean over() {
        if (index == plan.size() - 1) {
            return true;
        }
        return false;
    }

    public int direction() {
        return plan.getLast() - plan.getFirst();
    }

    private int direction(Request p) {
        return p.getToFloor() - p.getFromFloor();
    }

    public boolean merge(Dispatch dispatch, int floor, boolean arrive) {
        Request p = dispatch.getMain();
        if (direction() * direction(p) > 0) {
            if ((arrive && index == 0) ||
                    (direction() > 0 && floor <= p.getFromFloor()) ||
                    (direction() < 0 && floor >= p.getFromFloor())) {
                insertRoute(p.getFromFloor(), p.getToFloor(), p.getPersonId());
                put(dispatch.getBack());
                return true;
            }
        }
        return false;
    }
}
