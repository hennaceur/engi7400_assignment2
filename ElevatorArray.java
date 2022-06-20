package mun.concurrent.assignment.two;

public class ElevatorArray extends Thread{
    static int numElevator;
    static Elevator[] elevators;

    //Initialize a semaphore for each elevator with max capacity of people
    public ElevatorArray(int numberElevator, int maxCap) {
        numElevator = numberElevator;
        elevators = new Elevator[numberElevator];

        for (int i = 0; i < numElevator; i++) {
            elevators[i] = new Elevator(maxCap, i+1);
        }
    }

    //try to acquire an available elevator
    public boolean acquireElevator(ElevatorRiderFactory rider) {
        for (int i = 0; i < numElevator; i++) {
            if (elevators[i].hasPermits()) {
                boolean isRightDirection = rider.request.direction == elevators[i].requestDirection || elevators[i].currState == state.STATIONARY;
                boolean notFull = elevators[i].notFull();
                boolean isValidPickUp = elevators[i].isEmpty() || elevators[i].enRoute(rider.request);

                if (isRightDirection && notFull && isValidPickUp) {
                    return elevators[i].givePermit() && elevators[i].makeRequest(rider.request);
                }
            }
        }
        return false;
    }

    public void run() {
        for (int i = 0; i < numElevator; i++) {
            elevators[i].start();
        }
    }

    public void releaseAll() {
        for (int i = 0; i < numElevator; i++) {
            elevators[i].elevatorLock.release();
            elevators[i].stop();
        }
    }
}




