package elevator_tester;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plan {

    private HashMap<Integer, Integer> dst = new HashMap<>();
    private HashMap<Integer, Integer> src = new HashMap<>();
    Scanner inputScanner = null;

    private boolean checkFormat(String target) {
        final String polyRegex = "^" +
                "\\d+-FROM-[+-]?\\d+-TO-[+-]?\\d+" +
                "$";
        Pattern pattern = Pattern.compile(polyRegex);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public Plan(Scanner scanner) {
        inputScanner = scanner;
    }

    public void in(int id, int floor) {
        if(!src.containsKey(id)) {
            System.out.println("Unknown ID Recognized!");
            System.exit(0);
        }
        if(src.get(id) != floor) {
            System.out.println("elevator_tester.Passenger " + id + " Board On Wrong Floor!");
            System.exit(0);
        }
    }

    public void out(int id, int floor) {
        if(!dst.containsKey(id)) {
            System.out.println("Unknown ID Recognized!");
            System.exit(0);
        }
        if(dst.get(id) != floor) {
            System.out.println("elevator_tester.Passenger " + id + " Leave On Wrong Floor!");
            System.exit(0);
        }
    }

    public void run() {
        String request = null;
        while (true) {
            try {
                // Get line and remove any blank element
                request = inputScanner.nextLine();
            } catch (Exception e) {
                System.exit(0);
            }
            if (checkFormat(request)) {
                final String requestRegex =
                        "(?<id>\\d+)-FROM-(?<src>[+-]?\\d+)-TO-(?<dst>[+-]?\\d+)";
                Pattern pattern = Pattern.compile(requestRegex);
                Matcher req = pattern.matcher(request);
                while (req.find()) {
                    src.put(Integer.valueOf(req.group("id")), Integer.valueOf(req.group("src")));
                    dst.put(Integer.valueOf(req.group("id")), Integer.valueOf(req.group("dst")));
                }
            } else {
                break;
            }
        }
    }

    public void check(Set<Integer> set) {
        for(Integer i:src.keySet()) {
            if(!set.contains(i)) {
                System.out.println("Passenger " + i + " Not Board Successfully!");
                System.exit(0);
            }
        }
    }
}
