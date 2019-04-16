import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

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
    private PersonRequest plan = null;
    private boolean onBoard = false;
    private int floor = 1;

    public Elevator(Scheduler shared, String desc) {
        command = shared;
        message = desc;
    }

    @Override
    public void run() {
        try {
            while (true) {
                switch (status) {
                    case IDLE:
                        if (!command.empty()) {
                            plan = command.plan();
                            if (plan != null) {
                                status = Status.RUNNING;
                            }
                            else { System.exit(0); }
                        }
                        break;
                    case RUNNING:
                        if (onBoard) {
                            sleep(500 * abs(plan.getToFloor() - floor));
                            floor = plan.getToFloor();
                            status = Status.OPENING;
                        } else {
                            sleep(500 * abs(plan.getFromFloor() - floor));
                            floor = plan.getFromFloor();
                            status = Status.OPENING;
                        }
                        break;
                    case OPENING:
                        TimableOutput.println("OPEN-" + floor);
                        if (onBoard) {
                            TimableOutput.println("OUT-" +
                                    plan.getPersonId() + "-"
                                    + plan.getToFloor());
                            if (!command.empty() && command.peek() != null
                                    && floor == command.peek().getFromFloor()) {
                                plan = command.plan();
                                status = Status.RUNNING;
                                onBoard = true;
                                TimableOutput.println("IN-"
                                        + plan.getPersonId() + "-"
                                        + plan.getFromFloor());

                            } else {
                                status = Status.IDLE;
                                onBoard = false;
                            }
                        } else {
                            TimableOutput.println("IN-"
                                    + plan.getPersonId() + "-"
                                    + plan.getFromFloor());
                            status = Status.RUNNING;
                            onBoard = true;
                        }
                        sleep(500);
                        TimableOutput.println("CLOSE-" + floor);
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
