/**
 * The Elevator class is a thread that runs an elevator
 */
package mun.concurrent.assignment.two;

import org.jetbrains.annotations.NotNull;

import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/**
 * The elevator class is a thread that moves up and down, and stops at each floor that has a request
 */
public class Elevator extends Thread {
    public Direction requestedDirection;
    public Direction currentDirection;

    public ElevatorState currentState;
    public Integer currentFloor;

    public Semaphore elevatorLock;
    public Integer maxCapacity;
    public Integer elevatorNumber;
    public int totalTimeWithRider = 0;

    private int timeBetweenFloors = 5_000/100;
    private int timeToStop = 15_000/100;

    public Vector<Request> requests = new Vector<Request>();
    public Vector<Integer> visit = new Vector<Integer>();

    public Elevator(int maxCap, int elevatorNum) {
        elevatorLock = new Semaphore(maxCap, false);
        currentState = ElevatorState.IDLE;
        currentFloor = 1;
        maxCapacity = maxCap;
        elevatorNumber = elevatorNum;
        currentDirection = Direction.UP;
    }

    /**
     * If the elevator is available, return true, otherwise return false.
     *
     * @return A boolean value that indicates whether the elevator is available.
     */
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

    /**
     * > This function adds the request to the requests list, sets the requestDirection to the direction of the request,
     * adds the start and end floors of the request to the visit list, sorts the visit list, and adds the end floor of the
     * request to the visitDrop list
     *
     * @param request the request that is being made
     * @return A boolean value.
     */
    public boolean makeRequest(Request request) {
        requests.add(request);
        visit.add(request.startFloor);
        visit.add(request.endFloor);
        requestedDirection = request.direction;
        visit = orderList(visit, requestedDirection);
        return true;
    }

    /**
     * This function takes in a vector of integers and a direction, and returns a vector of integers in the order that the
     * elevator should visit the floors
     *
     * @param visitFloors a vector of integers that represent the floors that the elevator needs to visit.
     * @param direction the direction of the elevator
     * @return A vector of integers.
     */
    private @NotNull Vector<Integer> orderList(Vector<Integer> visitFloors, Direction direction){
        Vector<Integer> orderedResult = new Vector<Integer>();
        if (direction == Direction.DOWN){
            for(int i = 5; i > 0; i --) {
                if (visitFloors.contains(i)){
                    orderedResult.add(i);
                }
            }
        } else if (direction == Direction.UP) {
            for(int i = 1; i < 6; i ++) {
                if (visitFloors.contains(i)){
                    orderedResult.add(i);
                }
            }
        }
        return orderedResult;
    }

    /**
     * If the elevatorLock is available, acquire it and return true. If it's not available, wait 100 milliseconds and try
     * again. If it's still not available, return false.
     *
     * @return A boolean value.
     */
    public boolean givePermit() {
        try {
            return elevatorLock.tryAcquire(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Release the permit for the elevator lock.
     */
    private void releasePermit() {
        elevatorLock.release(1);
    }

    /**
     * Returns true if the queue is empty, false otherwise.
     *
     * @return The size of the requests arraylist.
     */
    public boolean isEmpty() {
        return requests.size() == 0;
    }

    /**
     * Returns true if the number of requests is less than the max capacity
     *
     * @return The boolean value of whether the size of the requests list is less than the maxCapacity.
     */
    public boolean notFull() {
        return requests.size() < maxCapacity;
    }

    /**
     * If the elevator is going up, it is en route to a floor if it is currently below the floor of the pending request. If
     * the elevator is going down, it is en route to a floor if it is currently above the floor of the pending request
     *
     * @param pendingRequest The request that is being checked to see if it is en route.
     * @return A boolean value.
     */
    public boolean enRoute(Request pendingRequest) {
        if (requests.size() > 0){
            switch (pendingRequest.direction) {
                case UP:
                    return pendingRequest.startFloor >= requests.elementAt(0).startFloor && pendingRequest.startFloor <= requests.elementAt(0).endFloor;
                case DOWN:
                    return pendingRequest.startFloor <= requests.elementAt(0).startFloor && pendingRequest.startFloor >= requests.elementAt(0).endFloor;
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * If the current floor is greater than 1, decrement the current floor and set the current state to DOWN. Otherwise,
     * set the current state to UP
     */
    private void decrementFloor() {
        if (currentFloor > 1) {
            currentFloor--;
            this.currentDirection = Direction.DOWN;
            try { sleep(timeBetweenFloors); } catch (InterruptedException ignored) { }
        } else {
            this.currentDirection = Direction.UP;
        }
    }

    /**
     * If the current floor is less than 5, increment the current floor and set the current state to UP. Otherwise, set the
     * current state to DOWN
     */
    private void incrementFloor() {
        if (currentFloor < 5) {
            currentFloor++;
            this.currentDirection = Direction.UP;
            try { sleep(timeBetweenFloors); } catch (InterruptedException ignored) { }
        } else {
            this.currentDirection = Direction.DOWN;
        }
    }

    private void holdAndRelease() {
        if (visit.contains(currentFloor)){
            // This only works with a max capacity of 1 or 2
            for(int i = 0; i < requests.size(); i++){
                if (requests.elementAt(i).endFloor == currentFloor){
                    requests.remove(i);
                    releasePermit();
                }
            }
            totalTimeWithRider += 15;
            visit.remove(currentFloor);
            try { sleep(timeToStop); } catch (InterruptedException ignored) { }
        }

    }

    private void moveFloor() {
        totalTimeWithRider += 5;
        if (currentDirection == Direction.DOWN) {
            decrementFloor();
        } else if (currentDirection == Direction.UP) {
            incrementFloor();
        }
    }

    /**
     * Prints the current state of the elevator
     */
    private void printState() {
        System.out.print("Elevator " + elevatorNumber + " is currently at Floor " + currentFloor + " " + currentState + " " + currentDirection + "\n" + visit.toString() + " Total Time: " +
                totalTimeWithRider + "\n");
    }

    /**
     * The elevator will move up or down depending on the request direction, and will stop at each floor that has a request
     */
    public void run() {
        while (true) {
            try {
                while (!isEmpty()) {
                    currentState = ElevatorState.MOVING;

                    // Get to start floor
                    while (currentFloor != requests.elementAt(0).startFloor){
                        if (currentFloor > requests.elementAt(0).startFloor) {
                            decrementFloor();
                        } else {
                            incrementFloor();
                        }
                        printState();
                    }

                    // once at start floor traverse list of visit floors
                    while (!visit.isEmpty()){
                        holdAndRelease();
                        moveFloor();
                        printState();
                    }
                }
            } catch (Exception ignored) { }
            this.currentState = ElevatorState.IDLE;
        }
    }
}
