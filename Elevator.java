package mun.concurrent.assignment.two;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Elevator extends Thread {
    public state currState;
    public int currFloor;
    public Semaphore elevatorLock;
    public Vector<Request> requests = new Vector<Request>();

    public Elevator(int maxCap) {
        elevatorLock = new Semaphore(maxCap, false);
        currState = state.STATIONARY;
        currFloor = 0;
    }

    public boolean hasPermits() {
        try{
            boolean has = elevatorLock.tryAcquire(100, TimeUnit.MILLISECONDS);
            if (has){
                elevatorLock.release(1);
            }
            return has;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void makeRequest(Request request) {
        requests.add(request);
    }

    public void givePermit(){
        try{
            elevatorLock.tryAcquire(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releasePermit(){
        elevatorLock.release(1);
    }

    public void run(){
        while(true){
            while(requests.size() > 0){
                // run shit
                // remove request from list
            }
            this.currState = state.STATIONARY;
        }
    }
}

/*
closest floor

pick up on the way riders - direction is the same and within the floor choice
direction = direction and (switch case) otherwise reject immediately

no current requests - set state to STATIONARY

notes:
maintain currFloor
 */
