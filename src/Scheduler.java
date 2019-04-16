import com.oocourse.elevator1.PersonRequest;

import java.util.LinkedList;

import static java.lang.Math.abs;

public class Scheduler {
    private final LinkedList<PersonRequest> schedule = new LinkedList<>();
    private final LinkedList<Plan> smartSchedule = new LinkedList<>();

    public Scheduler() {
    }

    public void smartAdd(PersonRequest request) {
        synchronized (smartSchedule) {
            if(request == null) {
                smartSchedule.add(null);
                return;
            }
            int min = abs(request.getFromFloor() - request.getToFloor());
            int index = -1;
                for (int i = 0;i < smartSchedule.size();i++) {
                    Plan plan = smartSchedule.get(i);
                    int temp = plan.calcRoute(request.getFromFloor()
                            , request.getToFloor());
                    if (temp != -1 && min > temp) {
                        min = temp;
                        index = i;
                    }
                }
                if (index == -1) {
                    smartSchedule.add(new Plan(request.getFromFloor()
                            , request.getToFloor(), request.getPersonId()));
                }
                else {
                    smartSchedule.get(index).insertRoute(request.getFromFloor()
                            , request.getToFloor(), request.getPersonId());
                }
            }
    }

    public Plan smartPlan() {
        synchronized (smartSchedule) {
            if (smartSchedule.isEmpty())
            {
                return null;
            }
            else {
                return smartSchedule.removeFirst();
            }
        }
    }

    public Plan smartPeek() {
        synchronized (smartSchedule) {
            if (smartSchedule.isEmpty()) {
                return null;
            }
            else {
                return smartSchedule.getFirst();
            }
        }
    }

    public boolean smartEmpty() {
        synchronized (smartSchedule) {
            return smartSchedule.isEmpty();
        }
    }

    public void add(PersonRequest request) {
        synchronized (schedule) {
            schedule.add(request);
        }
    }

    public PersonRequest plan() {
        synchronized (schedule) {
            if (schedule.isEmpty())
            {
                return null;
            }
            else {
                return schedule.removeFirst();
            }
        }
    }

    public PersonRequest peek() {
        synchronized (schedule) {
            if (schedule.isEmpty()) {
                return null;
            }
            else {
                return schedule.getFirst();
            }
        }
    }

    public boolean empty() {
        synchronized (schedule) {
            return schedule.isEmpty();
        }
    }
}
