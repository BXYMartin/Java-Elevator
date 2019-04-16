import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class Plan {
    private HashMap<Integer, Vector<Integer>> floorArrival = new HashMap<>();
    private HashMap<Integer, Vector<Integer>> floorBoard = new HashMap<>();
    private LinkedList<Integer> plan = new LinkedList<>();

    public Plan(int src, int dst, int num) {
        plan.add(src);
        plan.add(dst);
        Vector<Integer> out = new Vector<>();
        out.add(num);
        floorArrival.put(dst, out);
        Vector<Integer> in = new Vector<>();
        in.add(num);
        floorBoard.put(src, in);
    }

    public int calcRoute(int src, int dst) {
        int cost = 0;
        if ((plan.getLast() - plan.getFirst()) * (dst - src) > 0) {
            if ((dst - src) > 0) {
                if (dst > plan.getLast()) {
                    cost += dst - plan.getLast();
                }
                if (src < plan.getFirst()) {
                    cost += plan.getFirst() - src;
                }
            }
            if ((dst - src) < 0) {
                if (dst < plan.getLast()) {
                    cost += plan.getLast() - dst;
                }
                if (src > plan.getFirst()) {
                    cost += src - plan.getFirst();
                }
            }
            if (!plan.contains(src)) {
                cost += 1;
            }
            if (!plan.contains(dst)) {
                cost += 1;
            }
        } else {
            return -1;
        }
        return cost;
    }

    private void insert(HashMap<Integer, Vector<Integer>> target
            , int floor, int id) {
        if (target.get(floor) == null) {
            Vector<Integer> passenger = new Vector<>();
            passenger.add(id);
            target.put(floor, passenger);
        }
        else {
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
    }

    public int getIndex(int i) {
        return plan.get(i);
    }

    public Vector<Integer> getOutId(int i) {
        return floorArrival.get(plan.get(i));
    }

    public Vector<Integer> getInId(int i) {
        return floorBoard.get(plan.get(i));
    }

    public boolean over(int i) {
        if (i == plan.size() - 1) {
            return true;
        }
        return false;
    }
}
