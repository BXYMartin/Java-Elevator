import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Path {
    private Route[][] graph = new Route[23][23];
    private Route[][] path = new Route[graph.length][graph.length];
    private int floorA = 1;
    private int floorB = 1;
    private int floorC = 1;

    public Dispatch request(Request p) {
        floydWarshall();
        //printPath(p.getFromFloor(), p.getToFloor());
        return getPath(p.getFromFloor(), p.getToFloor(), p.getPersonId());
    }

    public void test() {
        //init();
        floydWarshall();
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("starting vertex: ");
        int from = scanner.nextInt();
        System.out.println("final vertex: ");
        int to = scanner.nextInt();
        */
        //printPath(from, to);
        Random r = new Random();
        printPath(index2floor(r.nextInt(20)), index2floor(r.nextInt(20)));
    }

    public void updateStatus(char e, Plan p, int max) {
        switch (e) {
            case 'A':
                floorA = p.get();
                break;
            case 'B':
                floorB = p.get();
                break;
            case 'C':
                floorC = p.get();
                break;
            default:
                System.out.println("Error Elevator Code!");
        }
    }

    public void updateStatus(char e, int f) {
        switch (e) {
            case 'A':
                floorA = f;
                break;
            case 'B':
                floorB = f;
                break;
            case 'C':
                floorC = f;
                break;
            default:
                System.out.println("Error Elevator Code!");
        }
    }

    private int floor2index(int floor) {
        if (floor > 0) {
            return floor + 2;
        } else {
            return floor + 3;
        }
    }

    private int index2floor(int index) {
        if (index >= 3) {
            return index - 2;
        } else {
            return index - 3;
        }
    }

    private void addPath(int from, int to, char e) {
        graph[floor2index(from)][floor2index(to)].elevator().add(e);
    }

    private int eta(char e, int i, int j) {
        switch (e) {
            case 'A':
                return 400 * abs(i - floorA);
            case 'B':
                return 500 * abs(i - floorB);
            case 'C':
                return 600 * abs(i - floorC);
            default:
                return 0;
        }
    }

    private int routeCost(int i, int j) {
        int least = 2000000;
        for (char e : graph[i][j].elevator()) {
            switch (e) {
                case 'A':
                    least = min(least, 400 * abs(j - i) + 400 + eta(e, i, j));
                    break;
                case 'B':
                    least = min(least, 500 * abs(j - i) + 400 + eta(e, i, j));
                    break;
                case 'C':
                    least = min(least, 600 * abs(j - i) + 400 + eta(e, i, j));
                    break;
                case 'R':
                    least = min(least, graph[i][j].cost());
                    break;
                default:
                    least = min(least, 2000000);
            }
        }
        return least;
    }

    public void init() {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                graph[i][j] = new Route();
                path[i][j] = new Route();
            }
        }
        int[] a = {-3, -2, -1, 1, 15, 16, 17, 18, 19, 20};
        int[] b = {-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] c = {1, 3, 5, 7, 9, 11, 13, 15};
        for (int from : a) {
            for (int to : a) {
                addPath(from, to, 'A');
            }
        }
        for (int from : b) {
            for (int to : b) {
                addPath(from, to, 'B');
            }
        }
        for (int from : c) {
            for (int to : c) {
                addPath(from, to, 'C');
            }
        }
    }

    private void floydWarshall() {
        for (int i = 0; i != path.length; i++) {
            for (int j = 0; j != path.length; j++) {
                graph[j][i].elevator().remove('R');
                path[j][i].setParam(graph[j][i].elevator(), i);
            }
        }
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                for (int k = 0; k < graph.length; k++) {
                    if (routeCost(i, j) + routeCost(j, k) < routeCost(i, k)) {
                        graph[i][k].elevator().add('R');
                        graph[i][k].setParam(graph[i][k].elevator(),
                                routeCost(i, j) + routeCost(j, k));
                        path[i][k].setParam(graph[j][k].elevator(), j);
                    }
                }
            }
        }
    }

    private char getRoute(HashSet<Character> elevator, int i, int j) {
        int least = 2000000;
        char save = 'R';
        for (char e : elevator) {
            switch (e) {
                case 'A':
                    if (least > 400 * abs(j - i) + 400 + eta(e, i, j)) {
                        least = 400 * abs(j - i) + 400 + eta(e, i, j);
                        save = e;
                    }
                    continue;
                case 'B':
                    if (least > 500 * abs(j - i) + 400 + eta(e, i, j)) {
                        least = 500 * abs(j - i) + 400 + eta(e, i, j);
                        save = e;
                    }
                    continue;
                case 'C':
                    if (least > 600 * abs(j - i) + 400 + eta(e, i, j)) {
                        least = 600 * abs(j - i) + 400 + eta(e, i, j);
                        save = e;
                    }
                    continue;
                case 'R':
                    if (least > graph[i][j].cost()) {
                        least = graph[i][j].cost();
                        save = e;
                    }
                    continue;
                default:
                    continue;
            }
        }
        return save;
    }

    private void printPath(int from, int to) {
        LinkedList<Integer> list = new LinkedList<>();
        LinkedList<Character> using = new LinkedList<>();
        int temp;
        temp = floor2index(to);
        while (temp != path[floor2index(from)][temp].cost()) {
            using.addFirst(getRoute(path[path[floor2index(from)][temp].cost()]
                    [temp].elevator(),
                    path[floor2index(from)][temp].cost(), temp));
            list.addFirst(temp);
            temp = path[floor2index(from)][temp].cost();
        }
        list.addFirst(temp);
        using.addFirst(getRoute(path[floor2index(from)][temp].elevator(),
                floor2index(from), temp));
        System.out.print(from);
        int i = 0;
        for (int integer : list) {
            System.out.print("-" + using.get(i++) + "->"
                    + index2floor(integer));
        }
        System.out.print(" price = " + routeCost(floor2index(from),
                floor2index(to)) + "\n");
    }

    private Dispatch getPath(int from, int to, int id) {
        int temp;
        temp = floor2index(to);
        while (temp != path[floor2index(from)][temp].cost()) {
            temp = path[floor2index(from)][temp].cost();
        }
        Dispatch dispatch = null;
        if (index2floor(temp) == to) {
            dispatch = new Dispatch(new Request(from, index2floor(temp), id)
                    , null
                    , getRoute(path[floor2index(from)][temp].elevator()
                    , floor2index(from), temp));
        } else {
            dispatch = new Dispatch(new Request(from, index2floor(temp), id)
                    , new Request(index2floor(temp), to, id)
                    , getRoute(path[floor2index(from)][temp].elevator()
                    , floor2index(from), temp));
        }

        return dispatch;
    }
}