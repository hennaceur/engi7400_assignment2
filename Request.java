package mun.concurrent.assignment.two;

/**
 * A request is a pair of start floor and end floor, and the direction is determined by the start floor and end floor
 */
public class Request {
    public int startFloor;
    public int endFloor;
    public Direction direction;

    public Request(int start, int end) {
        this.startFloor = start;
        this.endFloor = end;
        direction = startFloor > endFloor ? Direction.DOWN : Direction.UP;
    }
}
