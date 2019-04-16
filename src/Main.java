import com.oocourse.TimableOutput;

public class Main {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Scheduler scheduler = new Scheduler();
        Passenger passenger = new Passenger(scheduler);
        Printer printer = new Printer();

        passenger.start();
        SmartElevator elevator0 = new SmartElevator(scheduler, printer,
                'A', 6, 400);
        elevator0.start();
        SmartElevator elevator1 = new SmartElevator(scheduler, printer,
                'B', 8, 500);
        elevator1.start();
        SmartElevator elevator2 = new SmartElevator(scheduler, printer,
                'C', 7, 600);
        elevator2.start();
    }
}
