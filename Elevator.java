package mun.concurrent.assignment.two;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Elevator extends Thread {
    public state currState;
    public state requestDirection;
    public Integer currFloor;
    public Semaphore elevatorLock;
    public Vector<Request> requests = new Vector<Request>();
    public int maxCapacity;
    public Set<Integer> visitDrop = new HashSet<Integer>();
    public Set<Integer> visit = new HashSet<Integer>();
    public int elevatorNum;
    int totalTimeWithRider = 0;
    boolean here = false;

    public Elevator(int maxCap, int elNum) {
        elevatorLock = new Semaphore(maxCap, false);
        currState = state.STATIONARY;
        currFloor = 1;
        maxCapacity = maxCap;
        elevatorNum = elNum;
    }

    public boolean hasPermits() {
        try {
            boolean has = elevatorLock.tryAcquire(100, TimeUnit.MILLISECONDS);
            if (has) {
                elevatorLock.release(1);
            }
            return has;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean makeRequest(Request request) {
        requests.add(request);
        requestDirection = request.direction;
        visit.add(request.startFloor);
        visit.add(request.endFloor);
        try { visit = visit.stream().sorted().collect(Collectors.toSet()); } catch (Exception ignored) { }
        visitDrop.add(request.endFloor);
        return true;
    }

    public boolean givePermit() {
        try {
            return elevatorLock.tryAcquire(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void releasePermit() {
        elevatorLock.release(1);
    }

    public boolean isEmpty(){
        return requests.size() == 0;
    }

    public boolean notFull(){
        return requests.size() < maxCapacity;
    }

    public boolean enRoute(Request pendingRequest) {
        switch (pendingRequest.direction) {
            case UP: return currFloor <= pendingRequest.startFloor;
            case DOWN: return currFloor >= pendingRequest.startFloor;
            default: return false;
        }
    }

    private void decrementFloor() {
        if (currFloor > 1){
            currFloor--;
            this.currState = state.DOWN;
        }
        else {
            this.currState = state.UP;
        }
    }

    private void incrementFloor() {
        if (currFloor < 5){
            currFloor++;
            this.currState = state.UP;
        }
        else {
            this.currState = state.DOWN;
        }
    }

    private void visitFloor() {
        int sleepTime = 0;
        if (visit.contains(currFloor)) {
            totalTimeWithRider += 15;
            sleepTime = 15000;
            if (visitDrop.contains(currFloor)){
                requests.remove(0);
                releasePermit();
                visitDrop.remove(currFloor);
            }
            visit.remove(currFloor);
        } else {
            totalTimeWithRider += 5;
            sleepTime = 5000;
        }
        try {
            sleep(sleepTime/100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void traverseFloor() {
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void caseForUp() {
        if (currFloor > requests.elementAt(0).startFloor && !here) {
            decrementFloor();
            traverseFloor();
            if (currFloor == requests.elementAt(0).startFloor) {
                here = true;
            }
        } else {
            if (currFloor == requests.elementAt(0).startFloor) {
                here = true;
            }
            visitFloor();
            incrementFloor();
        }
    }

    private void caseForDown() {
        if (currFloor < requests.elementAt(0).startFloor && !here) {
            incrementFloor();
            traverseFloor();
            if (currFloor == requests.elementAt(0).startFloor) {
                here = true;
            }
        } else {
            if (currFloor == requests.elementAt(0).startFloor) {
                here = true;
            }
            visitFloor();
            decrementFloor();
        }
    }

    private void printState() {
        System.out.print("Elevator " + elevatorNum + " is currently at Floor " + currFloor + " moving " + currState + "\n" + visit.toString() + "\n");
    }

    public void run() {
        while (true) {
            try {
                while (!isEmpty()) {
                    here = false;
                    while (!visit.isEmpty()) {
//                        printState();
                        switch (requestDirection) {
                            case UP:
                                caseForUp();
                                break;
                            case DOWN:
                                caseForDown();
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (Exception ignored) { }
            this.currState = state.STATIONARY;
        }
    }
}
