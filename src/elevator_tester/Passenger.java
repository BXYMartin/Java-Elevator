package elevator_tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Passenger {

    private HashMap<Integer, Elevator> elevators = new HashMap<>();


    private boolean checkFormat(String target) {
        final String polyRegex = "^" +
                "\\[[ .\\d]+](OUT-\\d+-[+-]?\\d+|OPEN-[+-]?\\d+|CLOSE-[+-]?\\d+|IN-\\d+-[+-]?\\d+|ARRIVE-[+-]?\\d+)-[A-C]" +
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
        ArrayList<Integer> a = new ArrayList<>(Arrays.asList(-3, -2, -1, 1, 15, 16, 17, 18, 19, 20));
        ArrayList<Integer> b = new ArrayList<>(Arrays.asList(-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        ArrayList<Integer> c = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15));

        Elevator elevatorA = new Elevator("A", plan, 6, 400, a);
        Elevator elevatorB = new Elevator("B", plan, 8, 500, b);
        Elevator elevatorC = new Elevator("C", plan, 7, 600, c);
        plan.run();
        while (true) {
            try {
                // Get line and remove any blank element
                request = inputScanner.nextLine();
            } catch (Exception e) {
                System.exit(0);
            }
            if(request.equals("END")) {
                plan.check(elevatorA.check(), elevatorB.check(), elevatorC.check());
                System.out.print("Success!");
                break;
            }
            if (checkFormat(request)) {
                Pattern pattern;
                Matcher req;
                final String outRegex =
                        "\\[(?<time>[ .\\d]+)]OUT-(?<id>\\d+)-(?<floor>[+-]?\\d+)-(?<num>[A-C])";
                pattern = Pattern.compile(outRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    switch(req.group("num")){
                        case "A":
                            elevatorA.out(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("id")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "B":
                            elevatorB.out(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("id")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "C":
                            elevatorC.out(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("id")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        default:
                            System.out.println("Wrong Elevator Number " + req.group("num"));
                    }
                }
                final String inRegex =
                        "\\[(?<time>[ .\\d]+)]IN-(?<id>\\d+)-(?<floor>[+-]?\\d+)-(?<num>[A-C])";
                pattern = Pattern.compile(inRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    switch(req.group("num")){
                        case "A":
                            elevatorA.in(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("id")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "B":
                            elevatorB.in(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("id")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "C":
                            elevatorC.in(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("id")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        default:
                            System.out.println("Wrong Elevator Number " + req.group("num"));
                    }
                }
                final String openRegex =
                        "\\[(?<time>[ .\\d]+)]OPEN-(?<floor>[+-]?\\d+)-(?<num>[A-C])";
                pattern = Pattern.compile(openRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    switch(req.group("num")){
                        case "A":
                            elevatorA.open(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "B":
                            elevatorB.open(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "C":
                            elevatorC.open(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        default:
                            System.out.println("Wrong Elevator Number " + req.group("num"));
                    }
                }
                final String closeRegex =
                        "\\[(?<time>[ .\\d]+)]CLOSE-(?<floor>[+-]?\\d+)-(?<num>[A-C])";
                pattern = Pattern.compile(closeRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    switch(req.group("num")){
                        case "A":
                            elevatorA.close(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "B":
                            elevatorB.close(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "C":
                            elevatorC.close(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        default:
                            System.out.println("Wrong Elevator Number " + req.group("num"));
                    }
                }
                final String arriveRegex =
                        "\\[(?<time>[ .\\d]+)]ARRIVE-(?<floor>[+-]?\\d+)-(?<num>[A-C])";
                pattern = Pattern.compile(arriveRegex);
                req = pattern.matcher(request);
                while (req.find()) {
                    switch(req.group("num")){
                        case "A":
                            elevatorA.arrive(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "B":
                            elevatorB.arrive(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        case "C":
                            elevatorC.arrive(Double.valueOf(req.group("time")),
                                    Integer.valueOf(req.group("floor")));
                            break;
                        default:
                            System.out.println("Wrong Elevator Number " + req.group("num"));
                    }
                }
            } else {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        }
    }
}