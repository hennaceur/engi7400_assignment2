package mun.concurrent.assignment.two;
import java.util.concurrent.Semaphore;

public class Elevator {
    public state currState;
    public int currFloor;
    public Semaphore elevatorLock;

    public Elevator(int maxCap) {
        elevatorLock = new Semaphore(maxCap, false);
        currState = state.STATIONARY;
        currFloor = 0;
    }
}