package mun.concurrent.assignment.two;

public class Request {
    public int startFloor;
    public int endFloor;
    public state direction;

    public Request(int start, int end) {
        this.startFloor = start;
        this.endFloor = end;
        direction = startFloor > endFloor ? state.DOWN : state.UP;
    }
}
