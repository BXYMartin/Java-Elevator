package elevator_tester;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Passenger {

    private HashMap<Integer, Elevator> elevators = new HashMap<>();


    private boolean checkFormat(String target) {
        final String polyRegex = "^" +
                "\\[[ .\\d]+](OUT-\\d+-[+-]?\\d+|OPEN-[+-]?\\d+|CLOSE-[+-]?\\d+|IN-\\d+-[+-]?\\d+|ARRIVE-[+-]?\\d+)" +
                "$";
        Pattern pattern = Pattern.compile(polyRegex);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public Passenger() {

    }

    public void run() {
        Scanner inputScanner = new Scanner(System.in);
        String request = null;
        Plan plan = new Plan(inputScanner);
        Elevator elevator = new Elevator("One", plan);
        plan.run();
        while (true) {
            try {
                // Get line and remove any blank element
                request = inputScanner.nextLine();
            } catch (Exception e) {
                System.exit(0);
            }
            if(request.equals("END")) {
                plan.check(elevator.check());
                System.out.print("Success!");
                break;
            }
            if (checkFormat(request)) {
                Pattern pattern;
                Matcher req;
                final String outRegex =
                        "\\[(?<time>[ .\\d]+)]OUT-(?<id>\\d+)-(?<floor>[+-]?\\d+)";
                pattern = Pattern.compile(outRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    elevator.out(Double.valueOf(req.group("time")),
                            Integer.valueOf(req.group("id")),
                            Integer.valueOf(req.group("floor")));
                }
                final String inRegex =
                        "\\[(?<time>[ .\\d]+)]IN-(?<id>\\d+)-(?<floor>[+-]?\\d+)";
                pattern = Pattern.compile(inRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    elevator.in(Double.valueOf(req.group("time")),
                            Integer.valueOf(req.group("id")),
                            Integer.valueOf(req.group("floor")));
                }
                final String openRegex =
                        "\\[(?<time>[ .\\d]+)]OPEN-(?<floor>[+-]?\\d+)";
                pattern = Pattern.compile(openRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    elevator.open(Double.valueOf(req.group("time")),
                            Integer.valueOf(req.group("floor")));
                }
                final String closeRegex =
                        "\\[(?<time>[ .\\d]+)]CLOSE-(?<floor>[+-]?\\d+)";
                pattern = Pattern.compile(closeRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    elevator.close(Double.valueOf(req.group("time")),
                            Integer.valueOf(req.group("floor")));
                }
                final String arriveRegex =
                        "\\[(?<time>[ .\\d]+)]ARRIVE-(?<floor>[+-]?\\d+)";
                pattern = Pattern.compile(arriveRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    elevator.arrive(Double.valueOf(req.group("time")),
                            Integer.valueOf(req.group("floor")));
                }
            } else {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        }
    }
}