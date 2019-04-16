import com.oocourse.elevator3.PersonRequest;

import java.util.HashSet;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Scheduler {
    private final LinkedList<PersonRequest> schedule = new LinkedList<>();
    private final LinkedList<Request> smartSchedule = new LinkedList<>();
    private final HashSet<Request> waitList = new HashSet<>();
    private Path path = new Path();

    private boolean finished = false;

    public Scheduler() {
        path.init();
    }

    public void finish() {
        synchronized (smartSchedule) {
            finished = true;
            smartSchedule.notifyAll();
        }
    }

    private void stay(Request q) {
        synchronized (waitList) {
            if (q != null) {
                waitList.add(q);
            }
        }
    }

    private void leave(Request q) {
        synchronized (waitList) {
            if (q != null) {
                waitList.remove(q);
            }
        }
    }

    public void updatePlan(Plan plan, int floor, boolean arrive,
                           int max, char e) {
        synchronized (smartSchedule) {
            path.updateStatus(e, plan, max);
            for (int i = 0; i < smartSchedule.size(); i++) {
                Request comp = smartSchedule.get(i); // Add Path Support
                if (comp != null) {
                    Dispatch dispatch = path.request(comp);
                    if (dispatch.check(e) && plan.current() < max &&
                            plan.merge(dispatch, floor, arrive)) {
                        stay(dispatch.getBack());
                        smartSchedule.remove(i);
                        smartSchedule.notifyAll();
                        i--;
                    }
                }
            }
            path.updateStatus(e, plan, max);
        }
    }

    public void smartAdd(Request request) {
        synchronized (smartSchedule) {
            smartSchedule.add(request);
            leave(request);
            smartSchedule.notifyAll();
        }
    }

    public Plan smartPlan(int floor, char e) {
        while (true) {
            synchronized (smartSchedule) {
                path.updateStatus(e, floor);
                while (smartSchedule.isEmpty() && !finished) {
                    try {
                        smartSchedule.wait();
                    } catch (InterruptedException c) {
                        System.out.println("Interrupted!");
                    }
                }

                if (finished && smartSchedule.size() == 0 &&
                        waitList.size() == 0) {
                    smartSchedule.notifyAll();
                    return null;
                }

                for (Request req : smartSchedule) {
                    Dispatch dispatch = path.request(req);
                    if (dispatch.check(e)) {
                        stay(dispatch.getBack());
                        smartSchedule.remove(req);
                        smartSchedule.notifyAll();
                        return new Plan(dispatch);
                    }
                }
                /*
                try {
                    smartSchedule.notifyAll();
                    smartSchedule.wait();
                } catch (InterruptedException c) {
                    System.out.println("Interrupted!");
                }
                */
            }

            try {
                sleep(500);
            } catch (InterruptedException c) {
                System.out.println("Interrupted!");
            }

        }
    }

    public Plan smartPeek(int floor, char e) {
        synchronized (smartSchedule) {
            path.updateStatus(e, floor);
            for (Request req : smartSchedule) {
                Dispatch dispatch = path.request(req);
                if (dispatch.check(e) &&
                        dispatch.getMain().getFromFloor() == floor) {
                    stay(dispatch.getBack());
                    smartSchedule.remove(req);
                    smartSchedule.notifyAll();
                    return new Plan(dispatch);
                }
            }
            return null;
        }
    }
}
