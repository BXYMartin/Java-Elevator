import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class Plan {
    private HashMap<Integer, Vector<Integer>> floorArrival = new HashMap<>();
    private HashMap<Integer, Vector<Integer>> floorBoard = new HashMap<>();
    private LinkedList<Integer> plan = new LinkedList<>();
    private int destination = 0;
    public boolean finished = false;

    private int index = 0;

    public void next() {
        if (plan.get(index) == destination) {
            finished = true;
        }
        index++;
    }

    public Plan(int src, int dst, int num) {
        plan.add(src);
        plan.add(dst);
        destination = dst;
        Vector<Integer> out = new Vector<>();
        out.add(num);
        floorArrival.put(dst, out);
        Vector<Integer> in = new Vector<>();
        in.add(num);
        floorBoard.put(src, in);
    }

    private int floors(int src, int dst) {
        if (src * dst < 0) {
            return Math.abs(src - dst) - 1;
        }
        return Math.abs(src - dst);
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
    }

    public int get() {
        return plan.get(index);
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

    public LinkedList<Integer> getPlan() {
        return plan;
    }

    public HashMap<Integer, Vector<Integer>> getFloorArrival() {
        return floorArrival;
    }

    public HashMap<Integer, Vector<Integer>> getFloorBoard() {
        return floorBoard;
    }

    private int direction() {
        return plan.getLast() - plan.getFirst();
    }

    public boolean merge(Plan comp, int floor, boolean arrive) {
        if (direction() * comp.direction() > 0) {
            if ((direction() > 0 && floor <= comp.getPlan().getFirst() && plan.getLast() >= comp.getPlan().getLast()) ||
                    (direction() < 0 && floor >= comp.getPlan().getFirst() && plan.getLast() <= comp.getPlan().getFirst()) ||
                    (arrive && index == 0 && direction() > 0 && plan.getLast() >= comp.getPlan().getLast()) ||
                    (arrive && index == 0 && direction() < 0 && plan.getLast() <= comp.getPlan().getFirst())) {
                for (Integer i : comp.getPlan()) {
                    if (!plan.contains(i)) {
                        plan.add(plan.size() - 1, i);
                    }
                }
                plan.sort(new Comparator<Integer>() {
                    public int compare(Integer one, Integer two) {
                        return (two - one) * direction() * -1;
                    }
                }
                );
                comp.getFloorArrival().forEach((k, v) ->
                        floorArrival.merge(k, v, (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }));
                comp.getFloorBoard().forEach((k, v) ->
                        floorBoard.merge(k, v, (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }));
                return true;
            }
        }
        return false;
    }
}
