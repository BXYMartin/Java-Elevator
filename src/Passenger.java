import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

import java.io.IOException;

public class Passenger extends Thread {

    private Scheduler scheduler = null;

    public Passenger(Scheduler shared) {
        scheduler = shared;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                scheduler.finish();
                break;
            } else {
                // a new valid request
                scheduler.smartAdd(new Request(request.getFromFloor(),
                        request.getToFloor(), request.getPersonId()));
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            System.out.println("IO Error!");
        }
    }
}
