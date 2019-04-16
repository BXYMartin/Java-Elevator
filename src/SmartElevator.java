
public class SmartElevator extends Thread {
    private enum Status {
        IDLE,
        RUNNING,
        OPENING;
    }

    private Scheduler command = null;
    private Printer printer = null;
    private char message = '-';
    private Status status = Status.IDLE;
    private Plan plan = null;
    private int floor = 1;
    private int max = 0;
    private int speed = 0;

    private int sign(int num) {
        if (num >= 0) {
            return 1;
        }
        return -1;
    }

    public SmartElevator(Scheduler shared, Printer out,
                         char desc, int load, int s) {
        command = shared;
        printer = out;
        message = desc;
        max = load;
        speed = s;
    }

    private void stop(Plan plan) {
        if (plan.getOutId() != null) {
            for (int i : plan.getOutId()) {
                printer.println("OUT-"
                        + i + "-"
                        + plan.get() + "-" + message);
                plan.off();
                if (plan.request(i) != null) {
                    command.smartAdd(plan.request(i));
                }
            }
        }
        command.updatePlan(plan, floor, false, max, message);
        if (plan.getInId() != null) {
            for (int i : plan.getInId()) {
                printer.println("IN-"
                        + i + "-"
                        + plan.get() + "-" + message);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            switch (status) {
                case IDLE:
                    plan = command.smartPlan(floor, message);
                    if (plan != null) {
                        status = Status.RUNNING;
                    } else {
                        stop();
                    }
                    break;
                case RUNNING:
                    int end = plan.get();
                    for (int i = floor; i != end; ) {
                        i = i + sign(end - i);
                        if (i != 0) {
                            try {
                                sleep(speed);
                            } catch (InterruptedException e) {
                                System.out.println("Interrupted!");
                            }
                            printer.println("ARRIVE-" + i + "-" + message);
                            command.updatePlan(plan, i, true, max, message);
                            end = plan.get();
                        }
                    }
                    status = Status.OPENING;
                    floor = end;
                    break;
                case OPENING:
                    printer.println("OPEN-" + floor + "-" + message);
                    try {
                        sleep(400);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted!");
                    }
                    stop(plan);
                    if (plan.over()) {
                        if ((plan =
                                command.smartPeek(floor, message)) != null) {
                            status = Status.RUNNING;
                            stop(plan);
                            plan.next();
                        } else {
                            status = Status.IDLE;
                        }
                    } else {
                        status = Status.RUNNING;
                        plan.next();
                    }
                    printer.println("CLOSE-" + floor + "-" + message);
                    break;
                default:
                    break;
            }
        }

    }
}
