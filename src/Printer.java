import com.oocourse.TimableOutput;

public class Printer {
    public synchronized void println(String output) {
        TimableOutput.println(output);
    }
}
