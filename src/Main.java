import com.oocourse.TimableOutput;
import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Scheduler scheduler = new Scheduler();
        Elevator elevator = new Elevator(scheduler, "Elevator 1");
        elevator.start();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                scheduler.add(null);
                break;
            } else {
                // a new valid request
                scheduler.add(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            System.out.println("IO Error!");
        }
    }
}
