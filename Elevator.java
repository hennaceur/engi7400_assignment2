package mun.concurrent.assignment.two;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Elevator extends Thread {
    public state currState;
    public Integer currFloor;
    public Semaphore elevatorLock;
    public Vector<Request> requests = new Vector<Request>();
    public int maxCapacity;
    public Set<Integer> visitDrop = new HashSet<Integer>();
    public Set<Integer> visit = new HashSet<Integer>();

    int totalTime = 0;

    public Elevator(int maxCap) {
        elevatorLock = new Semaphore(maxCap, false);
        currState = state.STATIONARY;
        currFloor = 1;
        maxCapacity = maxCap;
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
        currState = request.direction;
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
        }
    }

    private void incrementFloor() {
        if (currFloor < 5){
            currFloor++;
        }
    }

    private void visitFloor() {
        int sleepTime = 0;
        if (visit.contains(currFloor)) {
            totalTime += 15;
            sleepTime = 150;
            if (visitDrop.contains(currFloor)){
                requests.remove(0);
                releasePermit();
                visitDrop.remove(currFloor);
            }
            visit.remove(currFloor);

        } else {
            totalTime += 5;
            sleepTime = 50;
        }
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true) {
            while (!isEmpty()) {
                while (!visit.isEmpty()) {
                    switch (currState) {
                        case UP:
                            visitFloor();
                            incrementFloor();
                            break;
                        case DOWN:
                            visitFloor();
                            decrementFloor();
                            break;
                        default:
                            break;
                    }
                }
            }
            this.currState = state.STATIONARY;
        }
    }
}
