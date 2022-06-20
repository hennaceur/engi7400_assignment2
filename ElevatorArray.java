package mun.concurrent.assignment.two;

/**
 * ElevatorArray is a class that manages a collection of Elevator objects
 */
public class ElevatorArray extends Thread {
    static int numElevator;
    static Elevator[] elevators;

    //Initialize a semaphore for each elevator with max capacity of people
    public ElevatorArray(int numberElevator, int maxCap) {
        numElevator = numberElevator;
        elevators = new Elevator[numberElevator];

        for (int i = 0; i < numElevator; i++) {
            elevators[i] = new Elevator(maxCap, i + 1);
        }
    }

    /**
     * For each elevator, add the total time with rider to the total time.
     *
     * @return The total time that the elevators have been in use.
     */
    public int getTravelTime() {
        int totalTime = 0;
        for (Elevator elevator : elevators) {
            totalTime += elevator.totalTimeWithRider;
        }
        return totalTime;
    }

    //try to acquire an available elevator

    /**
     * If the elevator is going in the right direction, not full, and is empty or going to the same floor as the rider,
     * then give the rider a permit and make the request
     *
     * @param rider The rider that is requesting the elevator
     * @return A boolean value.
     */
    public boolean acquireElevator(ElevatorRiderFactory rider) {
        for (int i = 0; i < numElevator; i++) {
            if (elevators[i].hasPermits()) {
                boolean isRightDirection = rider.request.direction == elevators[i].requestedDirection || elevators[i].currentState == ElevatorState.IDLE;
                boolean notFull = elevators[i].notFull();
                boolean isValidPickUp = elevators[i].isEmpty() || elevators[i].enRoute(rider.request);

                if (isRightDirection && notFull && isValidPickUp) {
                    return elevators[i].givePermit() && elevators[i].makeRequest(rider.request);
                }
            }
        }
        return false;
    }

    /**
     * Release all the locks and stop all the elevators.
     */
    public void releaseAll() {
        for (int i = 0; i < numElevator; i++) {
            elevators[i].elevatorLock.release();
            elevators[i].interrupt();
        }
    }

    /**
     * The run function starts the elevator threads
     */
    public void run() {
        for (int i = 0; i < numElevator; i++) {
            elevators[i].start();
        }
    }
}




