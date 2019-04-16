import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Passenger extends Thread {

    private Scheduler scheduler = null;

    private boolean checkFormat(String target) {
        final String polyRegex = "^" +
                "\\d+-FROM-\\d+-TO-\\d+" +
                "$";
        Pattern pattern = Pattern.compile(polyRegex);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public Passenger(Scheduler shared) {
        scheduler = shared;
    }

    @Override
    public void run() {
        Scanner inputScanner = new Scanner(System.in);
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
                        "(?<id>\\d+)-FROM-(?<src>\\d+)-TO-(?<dst>\\d+)";
                Pattern pattern = Pattern.compile(requestRegex);
                Matcher req = pattern.matcher(request);
                while (req.find()) {
                    /*
                    scheduler.add(Integer.valueOf(req.group("src")),
                            Integer.valueOf(req.group("dst")),
                            Integer.valueOf(req.group("id")));
                            */
                }
            } else {
                System.exit(0);
            }
            try {
                sleep(400);
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        }
    }
}
