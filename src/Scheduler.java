import com.oocourse.elevator2.PersonRequest;

import java.util.LinkedList;

public class Scheduler {
    private final LinkedList<Plan> schedule = new LinkedList<>();

    public Scheduler() {
    }

    public void ask(Plan current, int floor, boolean arrive) {
        synchronized (schedule) {
            if (current.finished) {
                return;
            }
            for (int i = 0; i < schedule.size(); i++) {
                Plan plan = schedule.get(i);
                if (plan == null) {
                    continue;
                }
                if (current.merge(plan, floor, arrive)) {
                    schedule.remove(i);
                    i--;
                }
            }
        }
    }

    public void add(PersonRequest request) {
        synchronized (schedule) {
            if (request == null) {
                schedule.add(null);
                return;
            }
            Plan plan = new Plan(request.getFromFloor(), request.getToFloor(), request.getPersonId());
            schedule.add(plan);
            schedule.notifyAll();
        }
    }

    public Plan plan() {
        synchronized (schedule) {
            while (schedule.isEmpty()) {
                try {
                    schedule.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted!");
                }
            }
            return schedule.removeFirst();
        }
    }

    public boolean empty() {
        synchronized (schedule) {
            return schedule.isEmpty();
        }
    }
}
