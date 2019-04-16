import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import static java.lang.Math.abs;

public class Elevator extends Thread {
    private enum Status {
        IDLE,
        RUNNING,
        OPENING;
    }

    private Scheduler command = null;
    private String message = null;
    private Status status = Status.IDLE;
    private Plan plan = null;
    private boolean onBoard = false;
    private int floor = 1;

    public Elevator(Scheduler shared, String desc) {
        command = shared;
        message = desc;
    }

    private int sign(int num) {
        if (num >= 0) {
            return 1;
        }
        return -1;
    }

    private void stop(Plan plan) {
        if (plan.getInId() != null) {
            for (int i : plan.getInId()) {
                TimableOutput.println("IN-"
                        + i + "-"
                        + plan.get());
            }
        }

        if (plan.getOutId() != null) {
            for (int i : plan.getOutId()) {
                TimableOutput.println("OUT-"
                        + i + "-"
                        + plan.get());
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                switch (status) {
                    case IDLE:
                        plan = command.plan();
                        if (plan != null) {
                            status = Status.RUNNING;
                        }
                            else { System.exit(0); }
                        break;
                    case RUNNING:
                        int end;
                        end = plan.get();
                        for (int i = floor; i != end;
                             i = i + sign(end - floor)) {
                            if (i + sign(end - floor) != 0) {
                                sleep(400);
                                TimableOutput.println("ARRIVE-" + (i +
                                        sign(end - floor)));
                                command.ask(plan, (i +
                                        sign(end - floor)), true);
                                end = plan.get();
                            }
                        }
                        floor = plan.get();
                        status = Status.OPENING;
                        break;
                    case OPENING:
                        TimableOutput.println("OPEN-" + floor);
                        command.ask(plan, floor, false);
                        if (plan.over()) {
                            stop(plan);
                            status = Status.IDLE;
                            onBoard = false;
                        } else {
                            stop(plan);
                            status = Status.RUNNING;
                            onBoard = true;
                        }
                        sleep(400);
                        TimableOutput.println("CLOSE-" + floor);
                        plan.next();
                        break;
                    default:
                        break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }

    }
}
