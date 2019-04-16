
public class Dispatch {
    private char assign = '-';
    private Request main = null;
    private Request back = null;

    public Dispatch(Request req, Request push, char e) {
        main = req;
        back = push;
        assign = e;
    }

    public Request getMain() {
        return main;
    }

    public Request getBack() {
        return back;
    }

    public boolean check(char e) {
        return (assign == e);
    }
}
