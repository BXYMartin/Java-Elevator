import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import static java.lang.Math.abs;

public class smartElevator extends Thread {
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
    private int index = 0;

    public smartElevator(Scheduler shared, String desc) {
        command = shared;
        message = desc;
    }

    private void stop(Plan plan, int index) {
        if (plan.getInId(index) != null) {
            for (int i:plan.getInId(index)) {
                TimableOutput.println("IN-"
                        + i + "-"
                        + plan.getIndex(index));
            }
        }

        if (plan.getOutId(index) != null) {
            for (int i : plan.getOutId(index)) {
                TimableOutput.println("OUT-"
                        + i + "-"
                        + plan.getIndex(index));
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                switch (status) {
                    case IDLE:
                        if (!command.smartEmpty()) {
                            plan = command.smartPlan();
                            if (plan != null) {
                                status = Status.RUNNING;
                            }
                            else { System.exit(0); }
                        }
                        index = 0;
                        break;
                    case RUNNING:
                        sleep(500 * abs(plan.getIndex(index) - floor));
                        floor = plan.getIndex(index);
                        status = Status.OPENING;
                        break;
                    case OPENING:
                        TimableOutput.println("OPEN-" + floor);
                        if (plan.over(index)) {
                            stop(plan, index);
                            if (!command.smartEmpty() && command.smartPeek() != null
                                    && floor == command.smartPeek().getIndex(0)) {
                                plan = command.smartPlan();
                                status = Status.RUNNING;
                                stop(plan, 0);
                                index = 0;
                            } else {
                                status = Status.IDLE;
                                index = -1;
                            }
                        } else {
                            stop(plan, index);
                            status = Status.RUNNING;
                        }
                        sleep(500);
                        TimableOutput.println("CLOSE-" + floor);
                        index++;
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
