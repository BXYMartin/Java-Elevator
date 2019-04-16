package elevator_tester;

import java.util.*;

import static java.lang.Math.abs;

public class Elevator {
    private String number = "";
    private int floor = 1;
    private boolean open = false;
    private double openTime = 0;
    private double opTime = 0;
    private double speed = 0;
    private int max = 0;
    private int cur = 0;
    private ArrayList<Integer> avail;

    private HashMap<Integer, Boolean> people = new HashMap<>();

    private Plan plan;

    public Elevator(String name, Plan p, int load, double s, ArrayList<Integer> floors) {
        number = name;
        plan = p;
        max = load;
        speed = s/1001;
        avail = floors;
    }

    public void open(double time, int f){
        if(!avail.contains(f)) {
            System.out.println("Elevator " + number + " Opened On Unavailable Floor " + f + "!");
            System.exit(0);
        }
        if(floor != f) {
            System.out.println("Elevator " + number + " Opened On Wrong Floor " + f + " Should Be " + floor + "!");
            System.exit(0);
        }
        if(open) {
            System.out.println("Elevator " + number + "Opened Twice On " + f + "!");
            System.exit(0);
        }
        open = true;
        floor = f;
        openTime = time;
    }

    public void close(double time, int f) {
        if(floor != f) {
            System.out.println("Elevator " + number + " Closed On Wrong Floor " + f + " Should Be " + floor + "!");
            System.exit(0);
        }
        if(!open) {
            System.out.println("Elevator " + number + "Closed Twice On " + f + "!");
            System.exit(0);
        }
        if(time-openTime < 400/1001) {
            System.out.println("Elevator " + number + " Closed Too Fast On " + f + "!");
            System.exit(0);
        }
        open = false;
        opTime = time;
    }

    public void in(double time, int id, int f) {
        if(floor != f) {
            System.out.println("Passenger Entered Elevator " + number + " On Wrong Floor!");
            System.exit(0);
        }
        if(!open) {
            System.out.println("Passenger " + id + " Entered On " + f + " When Door Closed!");
            System.exit(0);
        }
        if(people.containsKey(id) && people.get(id)) {
            System.out.println("Passenger " + id + " Already Entered Elevator " + number + "!");
            System.exit(0);
        }
        plan.in(id, f);
        cur++;
        if (cur > max) {
            System.out.println("Elevator " + number + " Overloaded With " + cur + "!");
            System.exit(0);
        }
        people.put(id, true);
    }

    public void out(double time, int id, int f) {
        if(floor != f) {
            System.out.println("Passenger " + id + " Leaved Elevator " + number + " On Wrong Floor " + f + "!");
            System.exit(0);
        }
        if(!open) {
            System.out.println("Passenger " + id + " Leaved When Door Closed On Floor " + f + "!");
            System.exit(0);
        }
        if(!people.containsKey(id) || !people.get(id)) {
            System.out.println("Passenger " + id + " Not In Elevator " + number + "!");
            System.exit(0);
        }
        plan.out(id, f);
        cur--;
        people.put(id, false);
    }

    private boolean reach(int a, int b) {
        if(abs(a - b) != 1) {
            if((a == 1 && b == -1) || (a == -1 && b == 1)) {
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void arrive(double time, int f) {
        if(reach(floor, f)) {
            System.out.println("Elevator " + number + " Moving Strangely From " + floor + " To " + f + "!");
            System.exit(0);
        }
        if(time - opTime < speed) {
            System.out.println("Time " + time + " Last Time " + opTime);
            System.out.println("Elevator " + number + " Moving Too Fast From " + floor + " To " + f + "!");
            System.exit(0);
        }
        if(!(f >= -3 && f <= 20 && f != 0)) {
            System.out.println("Elevator " + number + " Moved To Wrong Floor " + f + "!");
            System.exit(0);
        }
        opTime = time;
        floor = f;
    }
    
    public Set<Integer> check() {
        if(open) {
            System.out.println("Elevator " + number + " Not Closed In The End!");
            System.exit(0);
        }
        for (Map.Entry<Integer, Boolean> entry:people.entrySet()) {
            if(entry.getValue()) {
                System.out.println("Passenger " + entry.getKey() + " Not Delivered Successfully In Elevator " + number + "!");
                System.exit(0);
            }
        }
        return people.keySet();
    }
}
