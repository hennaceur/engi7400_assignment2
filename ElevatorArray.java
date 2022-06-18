package mun.concurrent.assignment.two;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.function.Predicate;

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
    public Semaphore acquireElevator(ElevatorRiderFactory rider) {
        for (int i = 0; i < numElevator; i++) {
            try {
                boolean hasPermits = elevators[i].elevatorLock.tryAcquire(100, TimeUnit.MILLISECONDS); //deals with capacity
                boolean isRightDirection = rider.directionRequested == elevators[i].currState || elevators[i].currState == state.STATIONARY;
                boolean isClosestFloor = true;
                boolean isNotHandled = true; // if another elevator already picked up rider
                boolean isInRoute = true;

                if (hasPermits) {
                    if (isRightDirection && isClosestFloor && isNotHandled && isInRoute) {
						elevators[i].currState = rider.directionRequested;
						return elevators[i].elevatorLock;
                    } else {
                        elevators[i].elevatorLock.release(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void releaseRider(Semaphore elevatorLock) {
		elevatorLock.release(1);
    }

    public void releaseAll() {
		for (int i = 0; i < numElevator; i++) {
            elevators[i].elevatorLock.release();
		}
    }
}




