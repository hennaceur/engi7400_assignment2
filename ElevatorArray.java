package mun.concurrent.assignment.two;

public class ElevatorArray {
    static int numElevator;
    static Elevator[] elevators;

    //Initialize a semaphore for each elevator with max capacity of people
    public ElevatorArray(int numberElevator, int maxCap) {
        numElevator = numberElevator;
        elevators = new Elevator[numberElevator];

        for (int i = 0; i < numElevator; i++) {
            elevators[i] = new Elevator(maxCap);
        }
    }

    //try to acquire an available elevator
    public boolean acquireElevator(ElevatorRiderFactory rider) {
        for (int i = 0; i < numElevator; i++) {
            if (elevators[i].hasPermits()) {
                boolean isRightDirection = rider.directionRequested == elevators[i].currState || elevators[i].currState == state.STATIONARY;

                boolean meetsCriteria = isRightDirection && true;
                if (meetsCriteria) {
                    elevators[i].givePermit();
                    elevators[i].makeRequest(rider.currentFloor, rider.destinationFloor);
                    return true;
                }
            }
        }
        return false;
    }

    public void releaseAll() {
        for (int i = 0; i < numElevator; i++) {
            elevators[i].elevatorLock.release();
        }
    }
}




